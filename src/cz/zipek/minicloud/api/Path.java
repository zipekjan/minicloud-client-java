/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.zipek.minicloud.api;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Kamen
 */
public class Path {
	
	private final External source;
	
	private final int id;
	private final int parent;
	
	private final String name;
	private final String path;
	
	private final String checksum;

	private final Date mktime;
	private final Date mdtime;
	
	private final List<File> files = new ArrayList<>();
	private final List<Path> paths = new ArrayList<>();
	
	public Path(External api, JSONObject data) throws JSONException {
		
		source = api;
		
		id = data.getInt("id");
		parent = data.getInt("parent");
		
		name = data.getString("name");
		path = data.getString("path");
		
		checksum = data.getString("checksum");
		
		mktime = new Date(data.getLong("mktime") * 1000);
		mdtime = new Date(data.getLong("mdtime") * 1000);
		
		JSONArray ch_files = data.getJSONArray("files");
		JSONArray ch_paths = data.getJSONArray("paths");
		
		for(int i = 0, l = ch_files.length(); i < l; i++) {
			files.add(new File(this.source, ch_files.getJSONObject(i)));
		}
		
		for(int i = 0, l = ch_paths.length(); i < l; i++) {
			paths.add(new Path(this.source, ch_paths.getJSONObject(i)));
		}
		
	}

	/**
	 * @return the source
	 */
	public External getSource() {
		return source;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @return the parent
	 */
	public int getParent() {
		return parent;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the path
	 */
	public String getPath() {
		return path;
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

	/**
	 * @return the files
	 */
	public List<File> getFiles() {
		return files;
	}

	/**
	 * @return the paths
	 */
	public List<Path> getPaths() {
		return paths;
	}
	
}
