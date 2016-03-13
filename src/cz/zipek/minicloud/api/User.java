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
public class User {
	
	protected External source;
	
	protected int id;
	
	protected String name;
	protected String email;
	
	protected String key;
	
	protected boolean admin;
	
	public User(External api, JSONObject data) throws JSONException {
		
		source = api;
		
		id = data.getInt("id");
		
		name = data.getString("name");
		email = data.getString("email");
		
		key = data.getString("key");
		
		admin = data.getBoolean("admin");
		
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
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @return the key
	 */
	public String getKey() {
		return key;
	}

	/**
	 * @return the admin
	 */
	public boolean isAdmin() {
		return admin;
	}
	
}
