/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.zipek.minicloud.api;

import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Kamen
 */
public class ServerInfo {
	
	private final External source;
	
	private final String name;
	private final String description;
	private final String logo;
	
	private boolean niceUrl;
	
	public ServerInfo(External from, JSONObject info) throws JSONException {
		source = from;
		name = info.optString("name", "");
		description = info.optString("description", "");
		logo = info.optString("logo", null);
	}

	/**
	 * @return server name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return server description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @return server logo url
	 */
	public String getLogo() {
		return logo;
	}

	/**
	 * @return support for nice urls
	 */
	public boolean hasNiceUrl() {
		return niceUrl;
	}

	/**
	 * @return the source
	 */
	public External getSource() {
		return source;
	}
	
}
