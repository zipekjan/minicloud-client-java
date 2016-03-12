/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.zipek.minicloud.api.upload;

import cz.zipek.minicloud.api.File;

/**
 *
 * @author Kamen
 */
public class UploadItem {
	private final java.io.File file;
	private final File existing;
	private final String target;
	
	public UploadItem(java.io.File file) {
		this.file = file;
		this.target = null;
		this.existing = null;
	}
	
	
	public UploadItem(java.io.File file, String target) {
		this.file = file;
		this.target = target;
		this.existing = null;
	}
	
	public UploadItem(java.io.File file, File existing) {
		this.file = file;
		this.existing = existing;
		this.target = null;
	}

	/**
	 * @return the file
	 */
	public java.io.File getFile() {
		return file;
	}

	/**
	 * @return the existing
	 */
	public File getExisting() {
		return existing;
	}

	/**
	 * @return the target
	 */
	public String getTarget() {
		return target;
	}
}
