/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.zipek.minicloud.api;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Kamen
 */
public class Folder {
	private String name;
	
	private Folder parent;
	
	private final List<Folder> folders = new ArrayList<>();
	private final List<File> files = new ArrayList<>();
	
	public Folder(String name) {
		this.name = name;
	}
	
	/**
	 *
	 * @param folder
	 */
	public void add(Folder folder) {
		Folder found = null;
		for(Folder f : getFolders()) {
			if (f.getName().equals(folder.getName())) {
				found = f;
				break;
			}
		}
		
		if (found == null) {
			folder.setParent(this);
			getFolders().add(folder);
		} else {
			found.addFiles(folder.getFiles());
			found.addFolders(folder.getFolders());
		}
	}
	
	public void add(File file) {
		file.setFolder(this);
		getFiles().add(file);
	}
	
	public void addFolders(List<Folder> folders) {
		for(Folder folder : folders) {
			add(folder);
		}
	}
	
	public void addFiles(List<File> files) {
		for(File file : files) {
			add(file);
		}
	}
	
	public List<Folder> getAllFolders() {
		List<Folder> all = new ArrayList<>();
		for(Folder f : folders) {
			all.add(f);
			all.addAll(f.getAllFolders());
		}
		return all;
	}
	
	public List<File> getAllFiles() {
		List<File> all = new ArrayList<>();
		all.addAll(files);
		for(Folder f : folders) {
			all.addAll(f.getAllFiles());
		}
		return all;
	}

	/**
	 * @return total size of folder
	 */
	public long getSize() {
		long size = 0;
		for(File f : getAllFiles()) {
			size += f.getSize();
		}
		return size;
	}
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the parent
	 */
	public Folder getParent() {
		return parent;
	}

	/**
	 * @param parent the parent to set
	 */
	public void setParent(Folder parent) {
		this.parent = parent;
	}

	/**
	 * @return the folders
	 */
	public List<Folder> getFolders() {
		return folders;
	}

	/**
	 * @return the files
	 */
	public List<File> getFiles() {
		return files;
	}
	
	public String getPath() {
		StringBuilder path = new StringBuilder(getName());
		
		//Root folder has name '/'
		if (!getName().equals("/"))
			path.append("/");
		
		if (getParent() != null && getParent().getName().length() != 0) {
			path.insert(0, getParent().getPath());
		}
		return path.toString();
	}
	
	public Folder findFolder(String name) {
		for(Folder f : folders) {
			if (f.getName().equals(name)) {
				return f;
			}
		}
		return null;
	}
	
	public Folder findPath(String path) {
		return findPath(path, true);
	}
	
	public Folder findPath(String path, boolean fuzzy) {
		while (path.length() > 0 && path.charAt(0) == '/') {
			path = path.substring(1);
		}
		
		String[] parts = path.split("/");
		Folder current = this;
		for(String part : parts) {
			if (part.length() == 0)
				continue;
			
			Folder found = current.findFolder(part);
			if (found != null) {
				current = found;
			} else {
				if (!fuzzy)
					return null;
				break;
			}
		}
		
		return current;
	}
}
