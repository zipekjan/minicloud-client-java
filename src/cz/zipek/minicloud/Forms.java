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

import cz.zipek.minicloud.api.FileVersion;
import cz.zipek.minicloud.api.User;
import cz.zipek.minicloud.forms.AdminFrame;
import cz.zipek.minicloud.forms.AdminUserFrame;
import cz.zipek.minicloud.forms.Delete;
import cz.zipek.minicloud.forms.Download;
import cz.zipek.minicloud.forms.FileAvailabilityFrame;
import cz.zipek.minicloud.forms.Login;
import cz.zipek.minicloud.forms.SyncFolderFrame;
import cz.zipek.minicloud.forms.FileFrame;
import cz.zipek.minicloud.forms.FileVersionsFrame;
import cz.zipek.minicloud.forms.Sync;
import cz.zipek.minicloud.forms.Upload;
import cz.zipek.minicloud.forms.Move;
import cz.zipek.minicloud.forms.Main;
import cz.zipek.minicloud.forms.NewUserFrame;
import cz.zipek.minicloud.forms.SettingsFrame;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import cz.zipek.minicloud.sync.SyncFolder;
import java.util.HashMap;
import java.util.Map;

/**
 * Controls all frames in application.
 * 
 * @author zipekjan
 */
public class Forms {
	private static final List<JFrame> frames = new ArrayList<>();
	private static final Map<Integer, FileFrame> files = new HashMap<>();
	private static final Map<Integer, FileVersionsFrame> fileVersions = new HashMap<>();
	private static final Map<Integer, SyncFolderFrame> syncFolders = new HashMap<>();
	private static final Map<Integer, FileAvailabilityFrame> availability = new HashMap<>();
	private static final Map<Integer, AdminUserFrame> adminUsers = new HashMap<>();
	
	private static SettingsFrame settings;
	private static SyncFolderFrame newSyncFolder;
	private static AdminUserFrame newAdminUser;
	private static Login login;
	private static Main main;
	private static NewUserFrame newUser;
	private static AdminFrame admin;
	
	/**
	 * Adds frame to frame control.
	 * 
	 * @param frame 
	 */
	public static void add(JFrame frame) {
		frames.add(frame);
	}
	
	/**
	 * Hides all frames.
	 */
	public static void hide() {
		for(JFrame f : frames) {
			f.setVisible(false);
		}
	}
	
	/**
	 * Removes frame from frame control.
	 * 
	 * @param frame 
	 */
	public synchronized static void remove(JFrame frame) {
		frames.remove(frame);
	}
	
	/**
	 * Shows login frame.
	 */
	public static void showLogin() {
		if (login == null) {
			login = new Login();
			frames.add(login);
		}
		
		login.setVisible(true);
	}
	
	/**
	 * Shows new user frame.
	 */
	public static void showNewUser() {
		if (newUser == null) {
			newUser = new NewUserFrame();
			frames.add(newUser);
		}
		
		newUser.setVisible(true);
	}
	
	/**
	 * Shows main frame.
	 */
	public static void showMain() {
		getMain().setVisible(true);
	}
	
	/**
	 * @param files files to be downloaded
	 */
	public static void showDownload(List<FileVersion> files) {
		Download frame = new Download(files);
		frames.add(frame);
		frame.setVisible(true);
	}
	
	/**
	 * @param files files to be deleted
	 */
	public static void showDelete(List<MetaItem> files) {
		Delete frame = new Delete(files);
		frames.add(frame);
		frame.setVisible(true);
	}
	
	/**
	 * @param files files to be moved
	 */
	public static void showMove(List<MetaItem> files) {
		Move frame = new Move(files);
		frames.add(frame);
		frame.setVisible(true);
	}
	
	/**
	 * Shows upload frame.
	 */
	public static void showUpload() {
		Upload frame = new Upload();
		frames.add(frame);
		frame.setVisible(true);
	}
	
	/**
	 * @param folders folders to be synchronized
	 */
	public static void showSync(List<SyncFolder> folders) {
		Sync frame = new Sync(folders);
		frames.add(frame);
		frame.setVisible(true);
	}
	
	/**
	 * @param listener
	 * @param file file to be shown
	 * @param pub
	 */
	public static void showAvailability(FileFrame listener, cz.zipek.minicloud.api.File file, boolean pub) {
		FileAvailabilityFrame frame;
		
		if (availability.containsKey(file.getId())) {
			frame = availability.get(file.getId());
		} else {
			frame = new FileAvailabilityFrame();
			availability.put(file.getId(), frame);
			frames.add(frame);
		}
		
		frame.setFile(listener, file, pub);
		frame.setVisible(true);
	}
	
	/**
	 * @param file file to be shown
	 */
	public static void showFile(cz.zipek.minicloud.api.File file) {
		FileFrame frame;
		
		if (!files.containsKey(file.getId())) {
			frame = new FileFrame(file);
			files.put(file.getId(), frame);
			frames.add(frame);
		} else {
			frame = files.get(file.getId());
		}
		
		frame.updateFile(file);
		frame.setVisible(true);
	}
	
	/**
	 * @param file file to be shown
	 */
	public static void showFileVersions(cz.zipek.minicloud.api.File file) {
		FileVersionsFrame frame;
		
		if (!fileVersions.containsKey(file.getId())) {
			frame = new FileVersionsFrame();
			fileVersions.put(file.getId(), frame);
			frames.add(frame);
		} else {
			frame = fileVersions.get(file.getId());
		}
		
		frame.setFile(file);
		frame.setVisible(true);
	}
	
	/**
	 * Shows settings frame.
	 */
	public static void showSettings() {
		if (settings == null) {
			settings = new SettingsFrame();
			add(settings);
		}
		
		if (!settings.isVisible())
			settings.reloadData();
		settings.setVisible(true);
	}
	
	/**
	 * Shows settings frame.
	 */
	public static void showAdmin() {
		if (admin == null) {
			admin = new AdminFrame();
			add(admin);
		}
		
		if (!admin.isVisible())
			admin.reloadData();
		
		admin.setVisible(true);
	}
	
	/**
	 * @param folder sync folder to be shown
	 */
	public static void showSyncFolder(SyncFolder folder) {
		if (folder == null) {
			if (newSyncFolder == null) {
				newSyncFolder = new SyncFolderFrame(null);
			}
			newSyncFolder.setFolder(null);
			newSyncFolder.setVisible(true);
		} else {
			SyncFolderFrame frame;
		
			if (!syncFolders.containsKey(folder.hashCode())) {
				frame = new SyncFolderFrame(folder);
				syncFolders.put(folder.hashCode(), frame);
				frames.add(frame);
			} else {
				frame = syncFolders.get(folder.hashCode());
			}
			
			frame.setFolder(folder);
			frame.setVisible(true);
		}
	}

	/**
	 * @return the main frame
	 */
	public static Main getMain() {
		if (main == null) {
			main = new Main();
			frames.add(getMain());
		}
		return main;
	}

	public static void showAdminUser(User user) {
		if (user == null) {
			if (newAdminUser == null) {
				newAdminUser = new AdminUserFrame();
			}
			newAdminUser.setUser(null);
			newAdminUser.setVisible(true);
		} else {
			AdminUserFrame frame;
		
			if (!adminUsers.containsKey(user.getId())) {
				frame = new AdminUserFrame();
				adminUsers.put(user.getId(), frame);
				add(frame);
			} else {
				frame = adminUsers.get(user.getId());
			}
			
			frame.setUser(user);
			frame.setVisible(true);
		}
	}
}
