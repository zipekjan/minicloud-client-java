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

	private String actionId = null;

	public Event(External sender, JSONObject data, String action_id) {
		this.sender = sender;
		this.data = data;
		this.actionId = action_id;
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
