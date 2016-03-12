/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.zipek.miniupload.api.events;

import cz.zipek.miniupload.api.External;
import org.json.JSONObject;

/**
 *
 * @author Kamen
 */
public class LoginFailedEvent extends cz.zipek.miniupload.api.Event {	
	public LoginFailedEvent(External sender, JSONObject data, int code) {
		super(sender, data, code);
	}
}
