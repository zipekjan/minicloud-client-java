/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.zipek.minicloud.api.download;

import cz.zipek.minicloud.api.File;
import cz.zipek.minicloud.api.FileVersion;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

/**
 *
 * @author Kamen
 */
public class DownloadItem {
	private final File file;
	private final FileVersion version;
	private final String encryption;
	private String target;
	
	public DownloadItem(File file, String target) {
		this.file = file;
		this.encryption = file.getEncryption();
		this.target = target;
		this.version = null;
	}
	
	public DownloadItem(File file, FileVersion version, String target) {
		this.file = file;
		this.encryption = file.getEncryption();
		this.target = target;
		this.version = version;
	}

	/**
	 * @return the file
	 */
	public File getFile() {
		return file;
	}
	
	public String getEncryption() {
		return this.encryption;
	}

	/**
	 * Prepares stream for download output.
	 * 
	 * @return download output stream
	 * @throws FileNotFoundException 
	 */
	public OutputStream getStream() throws FileNotFoundException {
		return new FileOutputStream(getTarget());
	}
	
	public void setTarget(String target) {
		this.target = target;
	}
	
	/**
	 * @return path to target file
	 */
	public String getTarget() {
		return target;
	}

	/**
	 * @return the version
	 */
	public FileVersion getVersion() {
		return version;
	}
}
