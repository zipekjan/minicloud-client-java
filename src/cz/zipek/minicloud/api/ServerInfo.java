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
	
	protected String name;
	protected String description;
	protected String logo;
	
	protected boolean niceUrl;
	
	public ServerInfo(JSONObject info) throws JSONException {
		name = info.getString("name");
		description = info.getString("description");
		logo = info.getString("logo");
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
	
}
