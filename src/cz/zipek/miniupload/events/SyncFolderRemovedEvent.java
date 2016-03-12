/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.zipek.miniupload.events;

import cz.zipek.miniupload.SettingsEvent;
import cz.zipek.miniupload.sync.SyncFolder;

/**
 *
 * @author Kamen
 */
public class SyncFolderRemovedEvent extends SettingsEvent {
	private final SyncFolder folder;

	public SyncFolderRemovedEvent(SyncFolder folder) {
		this.folder = folder;
	}

	/**
	 * @return the folder
	 */
	public SyncFolder getFolder() {
		return folder;
	}
}
