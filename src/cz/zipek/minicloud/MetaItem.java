/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.zipek.minicloud;

import cz.zipek.minicloud.api.External;
import cz.zipek.minicloud.api.File;
import cz.zipek.minicloud.api.Path;

/**
 *
 * @author Kamen
 */
public class MetaItem {
	private File file;
	private Path path;
	
	public MetaItem(File file) {
		this.file = file;
	}
	
	public MetaItem(Path path) {
		this.path = path;
	}
	
	public String getName() {
		return isFile() ? getFile().getName() : getPath().getName();
	}

	/**
	 * @return the file
	 */
	public File getFile() {
		return file;
	}

	/**
	 * @return the path
	 */
	public Path getPath() {
		return path;
	}
	
	public boolean isFile() {
		return file != null;
	}
	
	public boolean isPath() {
		return path != null;
	}
	
	public External getSource() {
		return isPath() ? getPath().getSource() : getFile().getSource();
	}
}
