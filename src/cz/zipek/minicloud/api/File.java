package cz.zipek.minicloud.api;

import cz.zipek.minicloud.Tools;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Representation of file on minicloud server.
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
	private String encryption;
	
	private Path parent;
	
	private String checksum;
	
	private Date mktime;
	private Date mdtime;
	
	private boolean isPublic;

	public File(External source, JSONObject data) {
		this.source = source;
		
		try {
			id = data.getInt("id");
			
			size = data.getLong("size");
			name = data.getString("filename");
			path = data.optString("path", null);
			checksum = data.optString("checksum", null);
			encryption = data.optString("encryption", null);
			
			mktime = new Date(data.getLong("mktime") * 1000);
			mdtime = new Date(data.getLong("mktime") * 1000);

			isPublic = data.optBoolean("public", false);
			
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
	 * Returns public download link.
	 * This link can be used by any user.
	 * File must be set to public for this link to work.
	 * 
	 * @return public download link
	 */
	public String getPublicDownloadLink() {
		return getPublicDownloadLink(false);
	}
	
	/**
	 * Returns public download link.
	 * This link can be used by any user.
	 * File must be set to public for this link to work.
	 * Pretty format must be supported by server to work. You can determine server support from ServerInfo.
	 * 
	 * @param pretty pretty format (must be supported by server)
	 * @return public download link
	 */
	public String getPublicDownloadLink(boolean pretty) {
		try {
			String format = "%s?action=download_file&id=%s&hash=%s&filename=%s";
			if (pretty) {
				format = "%s/download/%s/%s/%s";
			}
			
			return String.format("%s?action=download_file&id=%s&hash=%s&filename=%s", getSource().getApiUrl(), getId(), Tools.md5(getId() + getChecksum()), getName());
		} catch (NoSuchAlgorithmException | UnsupportedEncodingException ex) {
			Logger.getLogger(File.class.getName()).log(Level.SEVERE, null, ex);
			return null;
		}
	}
	
	/**
	 * Returns internal download link.
	 * This link can be used only by authorized clients.
	 * 
	 * @return internal download link
	 */
	public String getDownloadLink() {
		return String.format("%s?action=download_file&id=%s", getSource().getApiUrl(), getId());
	}
	
	/**
	 * Returns string representation of parent path.
	 * This could be used to determine file parent.
	 * 
	 * @return string representation of parent path
	 */
	public String getFolderPath() {
		return path;
	}
	
	/**
	 * Returns path string for this file.
	 * Path string always begins with / marking root of storage.
	 * 
	 * @return path string of this file
	 */
	public String getPath() {
		if (parent == null)
			return "/" + getName();
		return "/" + parent.getPath() + getName();
	}
	
	/**
	 * Builds file path string relative to specified path.
	 * Only supports paths that are parent to this file.
	 * 
	 * @param relative path result should be relative to
	 * @return relative path to specified parent path
	 */
	public String getRelativePath(Path relative) {
		return getRelativePath(relative.getPath());
	}
	
	/**
	 * Builds file path string relative to specified path.
	 * Only supports paths that are parent to this file.
	 * 
	 * @param relative path result should be relative to
	 * @return relative path to specified parent path
	 */
	public String getRelativePath(String relative) {
		if (parent == null)
			return getName();
		return parent.getRelativePath(relative) + "/" + getName();
	}
	
	/**
	 * Normalizes path string.
	 * Path will begin with /, all double slashes will be replaced with single slash.
	 * 
	 * @param path path to be normalized
	 * @return normalized path
	 */
	private String clearPath(String path) {
		if (path.length() > 0 || path.charAt(0) != '/') {
			path = "/" + path;
		}
		path = path.replaceAll("\\/{2,}", "/");
		return path;
	}
	
	/**
	 * Returns parent object.
	 * 
	 * @return parent path object
	 */
	public Path getParent() {
		return parent;
	}

	/**
	 * Sets parent object.
	 * This method is used internally to set file parent.
	 * Shouldn't be called by user, but could be if user wants to change
	 * file parent.
	 * This change doesn't affect server, you will need to call updateFile
	 * on API for this change to be applied to server.
	 * 
	 * @param path the folder to set
	 */
	public void setParent(Path path) {
		parent = path;
	}

	/**
	 * Returns file extension without dot.
	 * This can be used to determine file mime type.
	 * 
	 * @return extension without dot
	 */
	public String getExtension() {
		return extension;
	}

	/**
	 * Returns API used to fetch this file info.
	 * 
	 * @return api used to fetch this file info
	 */
	public External getSource() {
		return source;
	}

	/**
	 * Sets file name.
	 * This method doesn't have effect on server.
	 * You need to call updateFile method for changes to take effect.
	 * 
	 * @param name name of file
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Returns file ID.
	 * ID is used to definetly identify file on server.
	 * 
	 * @return file ID
	 */
	public int getId() {
		return id;
	}

	/**
	 * Returns file size in bytes.
	 * Returns unencrypted file size.
	 * 
	 * @return size in bytes
	 */
	public long getSize() {
		return size;
	}

	/**
	 * Returns filename.
	 * 
	 * @return name of file
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns MD5 checksum of unenrypted file.
	 * While MD5 is not enforced, MD5 should be used
	 * to ensure compatibility with multiple clients.
	 * Unecrypted contents are used to make synchronization easier.
	 * 
	 * @return md5 checksum of unecrypted contents
	 */
	public String getChecksum() {
		return checksum;
	}

	/**
	 * Returns date of file creation.
	 * Date when file was uploaded to server.
	 * 
	 * @return date of file creation
	 */
	public Date getMktime() {
		return mktime;
	}

	/**
	 * Returns date of last file modification.
	 * This date represents last time this file was uploaded to server.
	 * 
	 * @return date of last file modification
	 */
	public Date getMdtime() {
		return mdtime;
	}

	/**
	 * Returns encryption string.
	 * This string contains informations about file encryption.
	 * Empty or null string indicates that file is not encrypted.
	 * While format is not enforced in any way, it should follow Cipher format:
	 *    ALGORITHM/MODE/PADDING
	 * 
	 * Example: AES/CBC/PKCS5Padding
	 * 
	 * @return encryption string
	 */
	public String getEncryption() {
		return encryption;
	}
	
	/**
	 * Returns if file is public.
	 * Public files can be shared with anyone and shouldn't be encrypted.
	 * 
	 * @return is file public
	 */
	public boolean isPublic() {
		return isPublic;
	}
}
