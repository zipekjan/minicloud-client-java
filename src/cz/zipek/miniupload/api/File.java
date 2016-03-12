/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.zipek.miniupload.api;

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
	private int downloads;
	private String name;
	private String note;
	private String password;
	private String link;
	private String path;
	private String extension;
	private String md5;
	private Date date;
	private Date downloaded;
	private Folder folder;

	public File(External source, JSONObject data) {
		this.source = source;
		
		try {
			id = data.getInt("id");
			size = data.getLong("size");
			downloads = data.getInt("downloads");
			name = data.getString("name");
			note = data.getString("note");
			password = data.getString("password");
			link = data.getString("link");
			path = data.getString("folder");
			md5 = data.getString("md5");
			date = new Date(data.getLong("date") * 1000);
			downloaded = data.getLong("downloaded") != 0 ? new Date(data.getLong("downloaded") * 1000) : null;
			
			extension = "";
			if (name != null && name.length() > 0 && name.contains(".")) {
				String[] parts = name.split("\\.");
				extension = parts[parts.length - 1];
			}
		} catch (JSONException ex) {
			Logger.getLogger(File.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	
	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the date
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * @return the size
	 */
	public long getSize() {
		return size;
	}

	/**
	 * @return the note
	 */
	public String getNote() {
		return note;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @return the downloads
	 */
	public int getDownloads() {
		return downloads;
	}

	/**
	 * @return the downloaded
	 */
	public Date getDownloaded() {
		return downloaded;
	}

	/**
	 * @return the link
	 */
	public String getLink() {
		return link;
	}

	public String getDownloadLink() {
		return String.format("%s/download/%s", getSource().getUrl(), getLink());
	}
	
	/**
	 * @return the path
	 */
	public String getFolderPath() {
		return path;
	}
	
	public String getPath() {
		if (folder == null)
			return "/" + path + "/" + name;
		return folder.getPath() + name;
	}
	
	private String clearPath(String path) {
		if (path.length() > 0 || path.charAt(0) != '/') {
			path = "/" + path;
		}
		path = path.replaceAll("\\/{2,}", "/");
		return path;
	}
	
	public String getRelativeFolderPath(Folder relative_to) {
		return getRelativeFolderPath(relative_to.getPath());
	}
	
	public String getRelativeFolderPath(String relative_to) {
		return folder.getPath().substring(clearPath(relative_to).length());
	}
	
	public String getRelativePath(Folder relative_to) {
		return getRelativePath(relative_to.getPath());
	}
	
	public String getRelativePath(String relative_to) {
		return getRelativeFolderPath(relative_to) + name;
	}

	/**
	 * @return the folder
	 */
	public Folder getFolder() {
		return folder;
	}

	/**
	 * @param folder the folder to set
	 */
	public void setFolder(Folder folder) {
		this.folder = folder;
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
	 * @param note the note to set
	 */
	public void setNote(String note) {
		this.note = note;
	}

	/**
	 * @return the md5
	 */
	public String getMd5() {
		return md5;
	}
}
