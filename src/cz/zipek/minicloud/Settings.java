/* 
 * The MIT License
 *
 * Copyright 2016 Jan Zípek <jan at zipek.cz>.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package cz.zipek.minicloud;

import cz.zipek.minicloud.api.Listener;
import cz.zipek.minicloud.events.SyncFolderAddedEvent;
import cz.zipek.minicloud.events.SyncFolderModifiedEvent;
import cz.zipek.minicloud.events.SyncFolderRemovedEvent;
import cz.zipek.minicloud.sync.SyncFolder;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Stores persistent application settings.
 * 
 * @author Jan Zípek <jan at zipek.cz>
 */
public class Settings {
	///@var filename default settings file
	private static String filename = "settings.json";
	
	///@var username default username
	private static String username = "";
	///@var syncFolders synchronization folders
	private static final List<SyncFolder> syncFolders = new ArrayList<>();
	
	///@var encryption encyption settings
	private static String encryption;
	
	///@var encryptionOptions possible encryption values
	private static final String[] encryptionOptions = new String[] {
		null,
		"AES/CBC/PKCS5Padding",
		"Blowfish/CBC/PKCS5Padding"
	};
	
	private static final int defaultEncryption = 1;
	
	///@var server server url
	protected static String server = "";
	
	///@var listeners settings changes listeners
	protected static final List<Listener> listeners = new ArrayList<>();
	///@var toRemove listeners to be removed after all event is handled by all listeners
	protected static final List<Listener> toRemove = new ArrayList<>();
	
	/**
	 * Initialize required default parameters.
	 */ 
	static {
		encryption = encryptionOptions[defaultEncryption];
	}
	
	/**
	 * Adds new listener
	 * 
	 * @param listener 
	 */
	public static synchronized void addListener(Listener<SettingsEvent> listener) {
		listeners.add(listener);
	}

	/**
	 * Removes listener
	 * Don't call this method from handler!
	 * 
	 * @param listener 
	 */
	public static synchronized void removeListener(Listener<SettingsEvent> listener) {
		listeners.remove(listener);
	}
	
	/**
	 * This method should be called inside handler method
	 * 
	 * @param listener 
	 */
	public static void removeListenerLater(Listener<SettingsEvent> listener) {
		toRemove.add(listener);
	}
	
	/**
	 * Fires event to all listeners
	 * 
	 * @param event
	 */
	protected static synchronized void fireEvent(SettingsEvent event) {
		if (toRemove.size() > 0) {
			for(Listener l : toRemove) {
				listeners.remove(l);
			}
			toRemove.clear();
		}
		
		for(Listener listener : listeners) {
			listener.handleEvent(event, null);
		}
		
		if (toRemove.size() > 0) {
			for(Listener l : toRemove) {
				listeners.remove(l);
			}
			toRemove.clear();
		}
	}
	
	/**
	 * Loads settings from file
	 * 
	 * @param file
	 * @throws IOException
	 * @throws JSONException 
	 */
	public static void load(String file) throws IOException, JSONException {
		//Save filename for future use
		filename = file;
		
		String json = "";
        
        List<String> lines = Files.readAllLines(Paths.get(file), StandardCharsets.UTF_8);
        for(String line : lines) {
            json += line;
		}
		
		//Parse data
		JSONObject data = new JSONObject(json);
		
		//Load basic data
		username = data.optString("username", "");
		server = data.optString("server", "");
		encryption = data.optString("encryption", encryptionOptions[defaultEncryption]);
		
		//Load sync folders
		JSONArray folders = data.getJSONArray("folders");
		for(int i = 0, l = folders.length(); i < l; i++) {
			syncFolders.add(new SyncFolder(folders.getJSONObject(i)));
		}
	}
	
	/**
	 * Loads settings from saved filename. 
	 * 
	 * @throws IOException
	 * @throws JSONException 
	 */
	public static void load() throws IOException, JSONException {
		load(filename);
	}
	
	/**
	 * Saves settings to file.
	 * 
	 * @param file
	 * @throws JSONException
	 * @throws FileNotFoundException
	 * @throws UnsupportedEncodingException 
	 */
	public static void save(String file) throws JSONException, FileNotFoundException, UnsupportedEncodingException {
		filename = file;
		
		JSONObject data = new JSONObject();
		data.put("username", username);
		data.put("server", server);
		data.put("encryption", encryption);

		JSONArray folders = new JSONArray();
		int index = 0;
		for(SyncFolder folder : syncFolders) {
			folders.put(index++, getSyncFolderData(folder));
		}
		
		data.put("folders", folders);
		
		try (PrintWriter writer = new PrintWriter(filename, "UTF-8")) {
            writer.println(data.toString());
        }
	}
	
	/**
	 * Serializes folder to JSONObject.
	 * 
	 * @param folder
	 * @return serialized folder
	 * @throws JSONException 
	 */
	private static JSONObject getSyncFolderData(SyncFolder folder) throws JSONException {
		JSONObject data = new JSONObject();
		data.put("local", folder.getLocal().getAbsolutePath());
		data.put("remote", folder.getRemote());
		data.put("last", folder.getLastSync() != null ? folder.getLastSync().getTime() : 0);
		data.put("max-size", folder.getMaxSize());
		data.put("regexp", folder.getRegexp() != null ? folder.getRegexp().toString() : null);
		return data;
	}
	
	/**
	 * Saves settings to saved filename.
	 * 
	 * @throws JSONException
	 * @throws FileNotFoundException
	 * @throws UnsupportedEncodingException 
	 */
	public static void save() throws JSONException, FileNotFoundException, UnsupportedEncodingException {
		save(filename);
	}
	
	/**
	 * Adds new synchronization folder.
	 * Fires SyncFolderAddedEvent.
	 * 
	 * @param folder 
	 */
	public static void add(SyncFolder folder) {
		syncFolders.add(folder);
		fireEvent(new SyncFolderAddedEvent(folder));
		tryToSave();
	}
	
	/**
	 * Remove synchronization folder.
	 * Fires SyncFolderRemovedEvent.
	 * 
	 * @param folder 
	 */
	public static void remove(SyncFolder folder) {
		syncFolders.remove(folder);
		fireEvent(new SyncFolderRemovedEvent(folder));
		tryToSave();
	}
	
	/**
	 * Tell settings sync folder has been modified.
	 * Fires SyncFolderModifiedEvent.
	 * 
	 * @param folder 
	 */
	public static void syncFolderModified(SyncFolder folder) {
		fireEvent(new SyncFolderModifiedEvent(folder));
		tryToSave();
	}
	
	/**
	 * Tries to save settings, but wont throw exception upon failture
	 * 
	 * @return success of saving
	 */
	public static boolean tryToSave() {
		try {
			save();
			return true;
		} catch (JSONException | FileNotFoundException | UnsupportedEncodingException ex) {
			Logger.getLogger(Settings.class.getName()).log(Level.SEVERE, null, ex);
		}
		return false;
	}

	/**
	 * @return default username
	 */
	public static String getUsername() {
		return username;
	}

	/**
	 * @param default_username the username to be default
	 */
	public static void setUsername(String default_username) {
		username = default_username;
	}

	/**
	 * @return the syncFolders
	 */
	public static List<SyncFolder> getSyncFolders() {
		return syncFolders;
	}

	/**
	 * @return the server
	 */
	public static String getServer() {
		return server;
	}

	/**
	 * @param aServer the server to set
	 */
	public static void setServer(String aServer) {
		server = aServer;
	}

	/**
	 * @return the encryption
	 */
	public static String getEncryption() {
		return encryption;
	}

	/**
	 * @param aEncryption the encryption to set
	 */
	public static void setEncryption(String aEncryption) {
		encryption = aEncryption;
	}

	/**
	 * @return the encryptionOptions
	 */
	public static String[] getEncryptionOptions() {
		return encryptionOptions;
	}
}
