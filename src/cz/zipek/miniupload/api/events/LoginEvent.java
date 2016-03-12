/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.zipek.miniupload.api.events;

import cz.zipek.miniupload.api.External;
import cz.zipek.miniupload.api.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Kamen
 */
public class LoginEvent extends cz.zipek.miniupload.api.Event {	
	private String sessionId;
	private String syncKey;
	
	public LoginEvent(External sender, JSONObject data, int code) {
		super(sender, data, code);
		
		this.sessionId = null;
		if (data != null) {
			try {
				sessionId = data.getJSONObject("body").getString("session_id");
				syncKey = data.getJSONObject("body").getString("synckey");
			} catch(JSONException e) {
				Logger.getLogger(LoginEvent.class.getName()).log(Level.SEVERE, null, e);
			}
		}
	}

	/**
	 * @return the sessionId
	 */
	public String getSessionId() {
		return sessionId;
	}

	/**
	 * @return the syncKey
	 */
	public String getSyncKey() {
		return syncKey;
	}
}
