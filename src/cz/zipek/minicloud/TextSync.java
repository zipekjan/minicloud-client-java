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

import cz.zipek.minicloud.api.Base64;
import cz.zipek.minicloud.api.Event;
import cz.zipek.minicloud.api.External;
import cz.zipek.minicloud.api.Listener;
import cz.zipek.minicloud.api.ServerInfo;
import cz.zipek.minicloud.api.User;
import cz.zipek.minicloud.api.download.DownloadEvent;
import cz.zipek.minicloud.api.download.events.DownloadAllDoneEvent;
import cz.zipek.minicloud.api.download.events.DownloadFailedEvent;
import cz.zipek.minicloud.api.download.events.DownloadFileDoneEvent;
import cz.zipek.minicloud.api.download.events.DownloadFileStartedEvent;
import cz.zipek.minicloud.api.events.ServerInfoEvent;
import cz.zipek.minicloud.api.events.UnauthorizedEvent;
import cz.zipek.minicloud.api.events.UserEvent;
import cz.zipek.minicloud.api.upload.UploadEvent;
import cz.zipek.minicloud.api.upload.events.UploadAllDoneEvent;
import cz.zipek.minicloud.api.upload.events.UploadFailedEvent;
import cz.zipek.minicloud.api.upload.events.UploadFileDoneEvent;
import cz.zipek.minicloud.api.upload.events.UploadFileStartedEvent;
import cz.zipek.minicloud.sync.SyncEvent;
import cz.zipek.minicloud.sync.SyncFolder;
import cz.zipek.minicloud.sync.events.SyncChecksumFailedEvent;
import cz.zipek.minicloud.sync.events.SyncDone;
import cz.zipek.minicloud.sync.events.SyncDownloadEvent;
import cz.zipek.minicloud.sync.events.SyncExternalEvent;
import cz.zipek.minicloud.sync.events.SyncMkdirFailedEvent;
import cz.zipek.minicloud.sync.events.SyncUploadEvent;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class managers command line synchronization
 * 
 * @author Jan Zípek
 */
public class TextSync implements Listener {
	private final List<SyncFolder> folders = new ArrayList<>();
	private int currentFolder = -1;
	
	private final String auth;
	private final String key;
	
	private User user;
	private ServerInfo info;
	
	private final External api;
	
	/**
	 * 
	 * @param api
	 * @param folders folders to sync
	 * @param auth
	 * @param key
	 */
	public TextSync(External api, List<SyncFolder> folders, String auth, String key) {
		this.folders.addAll(folders);
		
		this.api = api;
		this.auth = auth;
		this.key = key;
		
		this.api.setAuth(auth);
		this.api.addListener(this);
	}
	
	/**
	 * Starts the synchronization. Can only be called when synchronization isn't already started.
	 */
	public void start() {
		api.getServerInfo();
	}
	
	/**
	 * Moves to next folder
	 */
	private void next() {
		currentFolder++;
		if (currentFolder < folders.size()) {
			sync(folders.get(currentFolder));
		} else {
			log(System.out, "DONE Synchronization done");
		}
	}
	
	/**
	 * Starts synchronization for specific folder
	 * 
	 * @param folder folder to sync
	 */
	private void sync(SyncFolder folder) {
		log(System.out, "STRT Synchronizing " + folder.getLocal().getAbsolutePath());
		
		folder.setTimeOffset((int)info.getOffset());
		folder.setExternal(api);
		folder.setEncryption(Settings.getEncryption());
		folder.addListener(this);
		folder.sync();
	}
	
	/**
	 * Prints output
	 * 
	 * @param stream	stream to prin to
	 * @param text		text to print
	 */
	private void log(PrintStream stream, String text) {
		stream.println(text);
	}

	@Override
	public void handleEvent(Object event, Object sender) {
		if (event instanceof Event) {
			handleEvent((Event)event);
		} else if (event instanceof SyncDownloadEvent) {
			handleEvent(((SyncDownloadEvent)event).getEvent());
		} else if (event instanceof SyncUploadEvent) {
			handleEvent(((SyncUploadEvent)event).getEvent());
		} else if (event instanceof SyncExternalEvent) {
			handleEvent(((SyncExternalEvent)event).getEvent());
		} else if (event instanceof SyncEvent) {
			handleEvent((SyncEvent)event, (SyncFolder)sender);
		}
	}
	
	/**
	 * Handles API events
	 * 
	 * @param event  API event
	 */
	public void handleEvent(Event event) {
		if (event instanceof UnauthorizedEvent) {
			log(System.err, "AUTH Authorization failed");
		}
		
		if (event instanceof ServerInfoEvent) {
			info = ((ServerInfoEvent)event).getServerInfo();
			api.getUser();
		}
		
		if (event instanceof UserEvent) {
			try {
				user = ((UserEvent)event).getUser();
				user.setKey(Base64.decode(key));
				
				if (currentFolder == -1) {
					next();
				}
			} catch (IOException ex) {
				Logger.getLogger(TextSync.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}
	
	/**
	 * Handles download specific events
	 * 
	 * @param event download event
	 */
	public void handleEvent(DownloadEvent event) {
		if (event instanceof DownloadFileStartedEvent) {
			DownloadFileStartedEvent e = (DownloadFileStartedEvent) event;
			log(System.out, "DWST Downloading " + e.getFile().getPath());
		} else if (event instanceof DownloadFileDoneEvent) {
			DownloadFileDoneEvent e = (DownloadFileDoneEvent) event;
			log(System.out, "DWDN Downloaded " + e.getFile().getPath());
		} else if (event instanceof DownloadAllDoneEvent) {
			log(System.out, "DWAL All files downloaded");
		} else if (event instanceof DownloadFailedEvent) {
			DownloadFailedEvent e = (DownloadFailedEvent) event;
			log(System.err, "DWER Download failed: " + e.getException());
		}
	}
	
	/**
	 * Handles upload specific events
	 * 
	 * @param event upload event
	 */
	public void handleEvent(UploadEvent event) {
		if (event instanceof UploadFileStartedEvent) {
			UploadFileStartedEvent e = (UploadFileStartedEvent) event;
			log(System.out, "UPST Uploading "+ e.getFile().getAbsolutePath());
		} else if (event instanceof UploadFileDoneEvent) {
			UploadFileDoneEvent e = (UploadFileDoneEvent) event;
			log(System.out, "UPDN Uploaded "+ e.getFile().getAbsolutePath());
		} else if (event instanceof UploadAllDoneEvent) {
			log(System.out, "UPAL All files uploaded");
		} else if (event instanceof UploadFailedEvent) {
			log(System.out, "UPER Upload failed");
		}
	}
	
	/**
	 * Handles synchronization specific events
	 * 
	 * @param event	 sync folder event
	 * @param folder folder that called event
	 */
	public void handleEvent(SyncEvent event, SyncFolder folder) {
		if (event instanceof SyncMkdirFailedEvent) {
			SyncMkdirFailedEvent e = (SyncMkdirFailedEvent) event;
			log(System.err, "ERRO Failed to create '" + e.getFolder() + "'");
		} else if (event instanceof SyncChecksumFailedEvent) {
			SyncChecksumFailedEvent e = (SyncChecksumFailedEvent) event;
			log(System.err, "ERRO Failed to checksum '" + e.getPath() + "'");
		} else if (event instanceof SyncDone) {
			log(System.out, "SYDO Synchronization of '" + folder.getLocal().getAbsolutePath() + "' completed");
		}
	}
}
