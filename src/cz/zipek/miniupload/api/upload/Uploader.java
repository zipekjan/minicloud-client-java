/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.zipek.miniupload.api.upload;

import cz.zipek.miniupload.api.Eventor;
import cz.zipek.miniupload.api.External;
import cz.zipek.miniupload.api.File;
import cz.zipek.miniupload.api.Listener;
import cz.zipek.miniupload.api.upload.events.UploadAllDoneEvent;
import cz.zipek.miniupload.api.upload.events.UploadFailedEvent;
import cz.zipek.miniupload.api.upload.events.UploadFileDoneEvent;
import cz.zipek.miniupload.api.upload.events.UploadFileStartedEvent;
import cz.zipek.miniupload.api.upload.events.UploadProgressEvent;
import cz.zipek.miniupload.api.upload.events.UploadThreadProgressEvent;
import cz.zipek.miniupload.api.upload.events.UploadThreadSentEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Kamen
 */
public class Uploader extends Eventor<UploadEvent> implements Listener {	
	private final List<UploadItem> items = new ArrayList<>();
	private final External source;
	private final String sessionId;
	private String targetFolder;
	
	private UploadThread thread;
	
	public Uploader(External source, String session_id) {
		this.source = source;
		this.sessionId = session_id;
	}
	
	public void add(java.io.File file) {
		items.add(new UploadItem(file));
	}
	
	public void add(java.io.File local, File remote) {
		items.add(new UploadItem(local, remote));
	}
	
	public void add(java.io.File local, String target) {
		items.add(new UploadItem(local, target));
	}
	
	public void start(String target) {
		if (thread == null && items.size() > 0) {
			targetFolder = target;
			uploadFile(items.get(0));
		}
	}
	
	public void stop() {
		if (thread != null) {
			thread.interrupt();
			thread = null;
		}
	}

	private void nextFile() {
		if (!items.isEmpty()) {
			items.remove(0);
			if (!items.isEmpty()) {
				uploadFile(items.get(0));
			} else {
				fireEvent(new UploadAllDoneEvent());
			}
		}
	}
	
	private void uploadFile(UploadItem file) {
		fireEvent(new UploadFileStartedEvent(file));
		
		if (file.getExisting() == null) {
			String target = file.getTarget();
			if (target == null) {
				target = targetFolder;
			}
			thread = new UploadThread(this, file.getFile(), target);
		} else {
			thread = new UploadThread(this, file.getFile(), file.getExisting());
		}
		
		thread.addListener(this);
		thread.start();
	}
	
	@Override
	public void handleEvent(Object event, Object sender) {
		if (event instanceof UploadFailedEvent) {
			fireEvent((UploadFailedEvent)event);
			thread = null;
		} else if (event instanceof UploadThreadProgressEvent) {
			UploadThreadProgressEvent e = (UploadThreadProgressEvent)event;
			UploadItem item = items.get(0);
			
			fireEvent(new UploadProgressEvent(item, e.getSent(), e.getTotal()));
		} else if (event instanceof UploadThreadSentEvent) {
			UploadThreadSentEvent e = (UploadThreadSentEvent)event;
			UploadItem item = items.get(0);
			
			JSONObject data = null;
			try {
				data = new JSONObject(e.getResponse());
			} catch (JSONException ex) {
				Logger.getLogger(Uploader.class.getName()).log(Level.SEVERE, null, ex);
			}
			
			fireEvent(new UploadFileDoneEvent(item, data));
			nextFile();
		}
	}
	
	/**
	 * @return the items
	 */
	public List<UploadItem> getItems() {
		return items;
	}

	/**
	 * @return the source
	 */
	public External getSource() {
		return source;
	}

	/**
	 * @return the sessionId
	 */
	public String getSessionId() {
		return sessionId;
	}
}
