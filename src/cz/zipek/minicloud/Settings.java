/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
 * @author Kamen
 */
public class Settings {
	///@var filename default settings file
	private static String filename = "settings.json";
	
	///@var username default username
	private static String username = "";
	///@var syncFolders synchronization folders
	private static final List<SyncFolder> syncFolders = new ArrayList<>();
	
	///@var listeners settings changes listeners
	protected static final List<Listener> listeners = new ArrayList<>();
	///@var toRemove listeners to be removed after all event is handled by all listeners
	protected static final List<Listener> toRemove = new ArrayList<>();
	
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
		username = tryToGetString(data, "username", "");
			
		//Load sync folders
		JSONArray folders = data.getJSONArray("folders");
		for(int i = 0, l = folders.length(); i < l; i++) {
			syncFolders.add(new SyncFolder(folders.getJSONObject(i)));
		}
	}
	
	/**
	 * Helper function. Tries to load from json and return it, on fail it returns notfound string.
	 * 
	 * @param source	from where to load
	 * @param key		key of string to load
	 * @param notfound  what to return when key doesn't exists
	 * @return content
	 * @throws JSONException 
	 */
	private static String tryToGetString(JSONObject source, String key, String notfound) throws JSONException {
		return source.has(key) ? source.getString(key) : notfound;
	}
	
	/**
	  * Helper function. Tries to load from json and return it, on fail it returns notfound string.
	 * 
	 * @param source	from where to load
	 * @param key		key of string to load
	 * @param notfound  what to return when key doesn't exists
	 * @return content
	 * @throws JSONException 
	 */
	private static boolean tryToGetBoolean(JSONObject source, String key, boolean notfound) throws JSONException {
		return source.has(key) ? source.getBoolean(key) : notfound;
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
}
