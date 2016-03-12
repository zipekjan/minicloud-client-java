/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.zipek.minicloud.api.download;

import cz.zipek.minicloud.api.File;

/**
 *
 * @author Kamen
 */
public class DownloadItem {
	private final File file;
	private final String target;
	
	public DownloadItem(File file, String target) {
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
