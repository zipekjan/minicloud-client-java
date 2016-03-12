/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.zipek.miniupload.api.download.events;

import cz.zipek.miniupload.api.File;
import cz.zipek.miniupload.api.download.DownloadEvent;

/**
 *
 * @author Kamen
 */
public class DownloadFileStartedEvent extends DownloadEvent {
	private final File file;
	private final String target;
	
	public DownloadFileStartedEvent(File file, String target) {
		this.file = file;
		this.target = target;
	}

	/**
	 * @return the file
	 */
	public File getFile() {
		return file;
	}

	/**
	 * @return the target
	 */
	public String getTarget() {
		return target;
	}
}