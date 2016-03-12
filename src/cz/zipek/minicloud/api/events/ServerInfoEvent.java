/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.zipek.minicloud.api.events;

import cz.zipek.minicloud.api.External;
import cz.zipek.minicloud.api.File;
import cz.zipek.minicloud.api.ServerInfo;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Kamen
 */
public class ServerInfoEvent extends cz.zipek.minicloud.api.Event {	
	
	protected ServerInfo serverInfo;
	
	public ServerInfoEvent(External sender, JSONObject data, int code) {
		super(sender, data, code);
		
		try {
			serverInfo = new ServerInfo(data);
		} catch(JSONException e) {
			Logger.getLogger(File.class.getName()).log(Level.SEVERE, null, e);
		}
	}

	/**
	 * @return server info
	 */
	public ServerInfo getServerInfo() {
		return serverInfo;
	}
	
}
