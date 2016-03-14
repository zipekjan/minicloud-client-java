/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.zipek.minicloud.api.events;

import cz.zipek.minicloud.api.External;
import org.json.JSONObject;

/**
 *
 * @author Kamen
 */
public class LoginFailedEvent extends cz.zipek.minicloud.api.Event {	
	public LoginFailedEvent(External sender, JSONObject data, String action_id) {
		super(sender, data, action_id);
	}
}
