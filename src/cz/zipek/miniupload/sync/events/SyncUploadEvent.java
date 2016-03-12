/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.zipek.miniupload.sync.events;

import cz.zipek.miniupload.api.upload.UploadEvent;
import cz.zipek.miniupload.sync.SyncEvent;

/**
 *
 * @author Kamen
 */
public class SyncUploadEvent extends SyncEvent {
	private final UploadEvent event;

	public SyncUploadEvent(UploadEvent event) {
		this.event = event;
	}

	/**
	 * @return the event
	 */
	public UploadEvent getEvent() {
		return event;
	}
}
