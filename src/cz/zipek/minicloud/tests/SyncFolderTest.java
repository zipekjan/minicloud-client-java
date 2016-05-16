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
package cz.zipek.minicloud.tests;

import cz.zipek.minicloud.Manager;
import cz.zipek.minicloud.api.External;
import cz.zipek.minicloud.api.Listener;
import cz.zipek.minicloud.api.events.SuccessEvent;
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
 * @author Jan Zípek
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
		
		if (event instanceof SuccessEvent) {
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
