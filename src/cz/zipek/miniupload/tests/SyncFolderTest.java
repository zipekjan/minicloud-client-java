/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.zipek.miniupload.tests;

import cz.zipek.miniupload.Manager;
import cz.zipek.miniupload.Session;
import cz.zipek.miniupload.api.External;
import cz.zipek.miniupload.api.Listener;
import cz.zipek.miniupload.api.events.LoginEvent;
import cz.zipek.miniupload.sync.SyncEvent;
import cz.zipek.miniupload.sync.SyncFolder;
import cz.zipek.miniupload.sync.events.SyncChecksumFailedEvent;
import cz.zipek.miniupload.sync.events.SyncMkdirFailedEvent;
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
		Manager.external.login("MEN-X", "honza".toCharArray());
		
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
			Session.setId(((LoginEvent)event).getSessionId());
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
