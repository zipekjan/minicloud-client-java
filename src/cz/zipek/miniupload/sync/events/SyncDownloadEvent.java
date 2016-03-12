/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.zipek.miniupload.sync.events;

import cz.zipek.miniupload.api.download.DownloadEvent;
import cz.zipek.miniupload.sync.SyncEvent;

/**
 *
 * @author Kamen
 */
public class SyncDownloadEvent extends SyncEvent {
	private final DownloadEvent event;

	public SyncDownloadEvent(DownloadEvent event) {
		this.event = event;
	}

	/**
	 * @return the event
	 */
	public DownloadEvent getEvent() {
		return event;
	}
}
