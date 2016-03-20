/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.zipek.minicloud.api.upload;

import cz.zipek.minicloud.api.File;
import java.io.InputStream;

/**
 *
 * @author Kamen
 */
public class UploadItem {
	private final java.io.File file;
	private final File existing;
	private final String target;
	private final InputStream stream;
	
	private String filename;
	private long size;
	
	public UploadItem(java.io.File file) {
		this.file = file;
		this.target = null;
		this.existing = null;
		this.stream = null;
	}
	
	
	public UploadItem(java.io.File file, String target) {
		this.file = file;
		this.target = target;
		this.existing = null;
		this.stream = null;
	}
	
	public UploadItem(java.io.File file, File existing) {
		this.file = file;
		this.existing = existing;
		this.target = null;
		this.stream = null;
	}
	
	public UploadItem(InputStream stream, File existing) {
		this(stream, existing.getName(), existing.getSize(), existing);
	}
	
	public UploadItem(InputStream stream, String filename, long size, File existing) {
		this.file = null;
		this.existing = existing;
		this.target = null;
		this.stream = stream;
		
		this.filename = filename;
		this.size = size;
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
	
	public InputStream getStream() {
		return stream;
	}
	
	public String getFilename() {
		return filename;
	}
	
	public long getSize() {
		return size;
	}
	
}
