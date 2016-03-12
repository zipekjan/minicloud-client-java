/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.zipek.miniupload;

import cz.zipek.miniupload.api.Event;
import cz.zipek.miniupload.api.Listener;
import cz.zipek.miniupload.api.download.DownloadEvent;
import cz.zipek.miniupload.api.download.events.DownloadAllDoneEvent;
import cz.zipek.miniupload.api.download.events.DownloadFailedEvent;
import cz.zipek.miniupload.api.download.events.DownloadFileDoneEvent;
import cz.zipek.miniupload.api.download.events.DownloadFileStartedEvent;
import cz.zipek.miniupload.api.events.PleaseLoginEvent;
import cz.zipek.miniupload.api.upload.UploadEvent;
import cz.zipek.miniupload.api.upload.events.UploadAllDoneEvent;
import cz.zipek.miniupload.api.upload.events.UploadFailedEvent;
import cz.zipek.miniupload.api.upload.events.UploadFileDoneEvent;
import cz.zipek.miniupload.api.upload.events.UploadFileStartedEvent;
import cz.zipek.miniupload.sync.SyncEvent;
import cz.zipek.miniupload.sync.SyncFolder;
import cz.zipek.miniupload.sync.events.SyncChecksumFailedEvent;
import cz.zipek.miniupload.sync.events.SyncDone;
import cz.zipek.miniupload.sync.events.SyncDownloadEvent;
import cz.zipek.miniupload.sync.events.SyncExternalEvent;
import cz.zipek.miniupload.sync.events.SyncMkdirFailedEvent;
import cz.zipek.miniupload.sync.events.SyncUploadEvent;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

/**
 * This class managers command line synchronization
 * 
 * @author Kamen
 */
public class TextSync implements Listener<SyncEvent> {
	private final List<SyncFolder> folders = new ArrayList<>();
	private int currentFolder = -1;
	
	/**
	 * 
	 * @param folders folders to sync
	 */
	public TextSync(List<SyncFolder> folders) {
		this.folders.addAll(folders);
	}
	
	/**
	 * Starts the synchronization. Can only be called when synchronization isn't already started.
	 */
	public void start() {
		if (currentFolder == -1) {
			next();
		}
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
	public void handleEvent(SyncEvent event, Object sender) {
		if (event instanceof SyncDownloadEvent) {
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
		if (event instanceof PleaseLoginEvent) {
			log(System.err, "AUTH Authorization failed");
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
