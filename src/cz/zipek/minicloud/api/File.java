/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.zipek.minicloud.api;

import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Kamen
 */
public class File {
	private final External source;
	
	private int id;
	private long size;
	private String name;
	private String path;
	private String extension;
	
	private Path parent;
	
	private String checksum;
	
	private Date mktime;
	private Date mdtime;

	public File(External source, JSONObject data) {
		this.source = source;
		
		try {
			id = data.getInt("id");
			
			size = data.getLong("size");
			name = data.getString("filename");
			path = data.optString("path", null);
			checksum = data.optString("checksum", null);
			
			mktime = new Date(data.getLong("mktime") * 1000);
			mdtime = new Date(data.getLong("mktime") * 1000);

			extension = "";
			if (name != null && name.length() > 0 && name.contains(".")) {
				String[] parts = name.split("\\.");
				extension = parts[parts.length - 1];
			}
		} catch (JSONException ex) {
			Logger.getLogger(File.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	
	public String getDownloadLink() {
		return String.format("?action=download&id=%s", getSource().getApiUrl(), getId());
	}
	
	/**
	 * @return the path
	 */
	public String getFolderPath() {
		return path;
	}
	
	public String getPath() {
		if (parent == null)
			return "/" + getName();
		return "/" + parent.getPath() + getName();
	}
	
	public String getRelativePath(Path relative) {
		return getRelativePath(relative.getPath());
	}
	
	public String getRelativePath(String relative) {
		if (parent == null)
			return getName();
		return parent.getRelativePath(relative) + "/" + getName();
	}
	
	private String clearPath(String path) {
		if (path.length() > 0 || path.charAt(0) != '/') {
			path = "/" + path;
		}
		path = path.replaceAll("\\/{2,}", "/");
		return path;
	}
	
	/**
	 * @return the folder
	 */
	public Path getParent() {
		return parent;
	}

	/**
	 * @param path the folder to set
	 */
	public void setParent(Path path) {
		parent = path;
	}

	/**
	 * @return the extension
	 */
	public String getExtension() {
		return extension;
	}

	/**
	 * @return the source
	 */
	public External getSource() {
		return source;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @return the size
	 */
	public long getSize() {
		return size;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the checksum
	 */
	public String getChecksum() {
		return checksum;
	}

	/**
	 * @return the mktime
	 */
	public Date getMktime() {
		return mktime;
	}

	/**
	 * @return the mdtime
	 */
	public Date getMdtime() {
		return mdtime;
	}
	
}
