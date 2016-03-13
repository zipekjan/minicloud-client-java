/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.zipek.minicloud.api.events;

import cz.zipek.minicloud.api.External;
import cz.zipek.minicloud.api.User;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Kamen
 */
public class UserEvent extends SuccessEvent {

	protected User user;
	
	public UserEvent(External sender, JSONObject data, int code) {
		super(sender, data, code);
		
		try {
			user = new User(sender, data);
		} catch(JSONException e) {
			Logger.getLogger(UserEvent.class.getName()).log(Level.SEVERE, null, e);
		}
	}

	/**
	 * @return the user
	 */
	public User getUser() {
		return user;
	}
	
}