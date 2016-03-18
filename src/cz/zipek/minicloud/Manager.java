/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.zipek.minicloud;

import cz.zipek.minicloud.api.Event;
import cz.zipek.minicloud.api.External;
import cz.zipek.minicloud.api.Listener;
import cz.zipek.minicloud.api.events.ServerInfoEvent;
import cz.zipek.minicloud.api.events.UnauthorizedEvent;
import cz.zipek.minicloud.api.events.UserEvent;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import org.json.JSONException;

/**
 *
 * @author zipekjan
 */
public class Manager {
	
	public static External external;
	
	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		//API
		external = new External();
		
		//Load settings if exists
		try {
			Settings.load("settings.json");
		} catch (IOException | JSONException ex) {
			Logger.getLogger(Manager.class.getName()).log(Level.SEVERE, null, ex);
		}
		
		//Maybe text?
		boolean sync = false;
		String syncKey = "";
		if (args.length > 0) {
			if (args.length != 2 || !args[0].equalsIgnoreCase("sync")) {
				System.err.println("USAGE: SYNC [KEY]\n\tKEY - User key");
 			} else {
				sync = true;
				syncKey = args[1];
			}
		} else {
			startUI();
		}
		
		if (sync) {
			startSync(syncKey);
		}
	}
	
	/**
	 * Start text synchronization.
	 * 
	 * @param key sync key
	 */
	private static void startSync(String key) {
		external.setAuth(key);
		
		TextSync sync = new TextSync(Settings.getSyncFolders());
		sync.start();
	}
	
	/**
	 * Starts UI component
	 */
	private static void startUI() {
		// Set pretty UI
		try {
			UIManager.setLookAndFeel(
				UIManager.getSystemLookAndFeelClassName()
			);
		} catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
			Logger.getLogger(External.class.getName()).log(Level.SEVERE, null, e); // handle exception
		}
		
		// Check some basic events
		external.addListener(new Listener<Event>() {
			@Override
			public void handleEvent(Event event, Object sender) {
				if (event instanceof UnauthorizedEvent) {
					Forms.hide();
					Forms.showLogin();
				} else if (event instanceof UserEvent) {
					Session.setUser(((UserEvent)event).getUser());
				} else if (event instanceof ServerInfoEvent) {
					Session.setServer(((ServerInfoEvent)event).getServerInfo());
				}
			}
		});

		//Show main form
		java.awt.EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				Forms.showLogin();
			}
		});
	}
}
