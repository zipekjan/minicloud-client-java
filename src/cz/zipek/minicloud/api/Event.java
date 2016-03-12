/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.zipek.minicloud.api;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Kamen
 */
public class Event {
	protected final External sender;
	protected final JSONObject data;
	protected final int code;
	
	private String actionId = null;

	public Event(External sender, JSONObject data, int code) {
		this.sender = sender;
		this.code = code;
		this.data = data;
		
		if (data.has("action-id")) {
			try {
				this.actionId = data.getString("action-id");
			} catch (JSONException ex) {
				Logger.getLogger(Event.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}

	/**
	 * @return the code
	 */
	public int getCode() {
		return code;
	}

	/**
	 * @return the data
	 */
	public JSONObject getData() {
		return data;
	}

	/**
	 * @return the sender
	 */
	public External getSender() {
		return sender;
	}

	/**
	 * @return the actionId
	 */
	public String getActionId() {
		return actionId;
	}
}
