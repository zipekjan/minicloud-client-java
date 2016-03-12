/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.zipek.miniupload.api.upload.events;

import cz.zipek.miniupload.api.upload.UploadEvent;
import cz.zipek.miniupload.api.upload.UploadItem;
import java.io.File;

/**
 *
 * @author Kamen
 */
public class UploadFileStartedEvent extends UploadEvent {
	private final File file;
	private final UploadItem item;
	
	public UploadFileStartedEvent(UploadItem item) {
		this.file = item.getFile();
		this.item = item;
	}
	
	/**
	 * @return the file
	 */
	public File getFile() {
		return file;
	}

	/**
	 * @return the item
	 */
	public UploadItem getItem() {
		return item;
	}
	
	public cz.zipek.miniupload.api.File getRemote() {
		return item.getExisting();
	}
	
	public String getTarget() {
		return item.getTarget();
	}
}
