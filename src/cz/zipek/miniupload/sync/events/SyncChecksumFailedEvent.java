/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.zipek.miniupload.sync.events;

import cz.zipek.miniupload.sync.SyncEvent;

/**
 *
 * @author Kamen
 */
public class SyncChecksumFailedEvent extends SyncEvent {
	private final String path;

	public SyncChecksumFailedEvent(String path) {
		this.path = path;
	}

	/**
	 * @return the path
	 */
	public String getPath() {
		return path;
	}
}
