/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.zipek.minicloud.api.events;

import cz.zipek.minicloud.api.External;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Kamen
 */
public class SynckeyEvent extends cz.zipek.minicloud.api.Event {	
	private String syncKey;
	
	public SynckeyEvent(External sender, JSONObject data, int code) {
		super(sender, data, code);
		
		if (data != null) {
			try {
				syncKey = data.getJSONObject("body").getString("synckey");
			} catch(JSONException e) {
				Logger.getLogger(SynckeyEvent.class.getName()).log(Level.SEVERE, null, e);
			}
		}
	}
	
	/**
	 * @return the syncKey
	 */
	public String getSyncKey() {
		return syncKey;
	}
}
