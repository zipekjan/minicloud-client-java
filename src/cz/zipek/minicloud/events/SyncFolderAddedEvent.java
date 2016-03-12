/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.zipek.minicloud.events;

import cz.zipek.minicloud.SettingsEvent;
import cz.zipek.minicloud.sync.SyncFolder;

/**
 *
 * @author Kamen
 */
public class SyncFolderAddedEvent extends SettingsEvent {
	private final SyncFolder folder;

	public SyncFolderAddedEvent(SyncFolder folder) {
		this.folder = folder;
	}

	/**
	 * @return the folder
	 */
	public SyncFolder getFolder() {
		return folder;
	}
}
