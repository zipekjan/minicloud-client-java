/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.zipek.minicloud.tests;

import cz.zipek.minicloud.Manager;
import cz.zipek.minicloud.Session;
import cz.zipek.minicloud.api.External;
import cz.zipek.minicloud.api.Listener;
import cz.zipek.minicloud.api.events.LoginEvent;
import cz.zipek.minicloud.sync.SyncEvent;
import cz.zipek.minicloud.sync.SyncFolder;
import cz.zipek.minicloud.sync.events.SyncChecksumFailedEvent;
import cz.zipek.minicloud.sync.events.SyncMkdirFailedEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Kamen
 */
public class SyncFolderTest implements Listener {
	private SyncFolder sync;
	
	public SyncFolderTest() {}
	
	public void testSync() {
		Manager.external = new External();
		Manager.external.addListener(this);
		
		JSONObject data = null;
		String json = String.format(
				"{\"local\":\"%s\", \"remote\": \"%s\", \"last\": %s}",
				"d:\\\\Zaloha\\\\Test\\\\",
				"test",
				"0"
		);
		try {
			data = new JSONObject(json);
		} catch (JSONException ex) {
			Logger.getLogger(SyncFolderTest.class.getName()).log(Level.SEVERE, json, ex);
		}
		try {
			sync = new SyncFolder(data);
		} catch (JSONException ex) {
			Logger.getLogger(SyncFolderTest.class.getName()).log(Level.SEVERE, null, ex);
		}
		sync.addListener(this);
	}
	
	@Override
	public void handleEvent(Object event, Object sender) {
		if (event instanceof LoginEvent) {
			sync.sync();
		}
		if (event instanceof SyncEvent) {
			if (event instanceof SyncMkdirFailedEvent) {
				SyncMkdirFailedEvent syncMkdirFailedEvent = (SyncMkdirFailedEvent) event;
				Logger.getLogger("SYNC").log(Level.SEVERE, "Failed to create ''{0}''", syncMkdirFailedEvent.getFolder());
			} else if (event instanceof SyncChecksumFailedEvent) {
				SyncChecksumFailedEvent syncChecksumFailedEvent = (SyncChecksumFailedEvent) event;
				Logger.getLogger("SYNC").log(Level.SEVERE, "Failed to checksum ''{0}''", syncChecksumFailedEvent.getPath());
			}
		}
	}
}
