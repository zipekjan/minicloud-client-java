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
import java.lang.reflect.Field;
import java.security.NoSuchAlgorithmException;
import java.security.Permission;
import java.security.PermissionCollection;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.Cipher;
import javax.swing.JOptionPane;
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

		removeCryptographyRestrictions();
		
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
	
	public static boolean checkKeyLength() {
		try {
			return Cipher.getMaxAllowedKeyLength("AES") >= 256;
		} catch (NoSuchAlgorithmException ex) {
			return false;
		}
	}
	
	/**
	 * Start text synchronization.
	 * 
	 * @param key sync key
	 */
	private static void startSync(String key) {
		String[] values = key.split(":");
		String auth = values[0] + ":" + values[1];
		String k = values[2];
		
		external.setServer(Settings.getServer());
		
		TextSync sync = new TextSync(external, Settings.getSyncFolders(), auth, k);
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
	
	private static void removeCryptographyRestrictions() {
		Logger logger = Logger.getLogger(Manager.class.getName());
		
		if (!isRestrictedCryptography()) {
			logger.fine("Cryptography restrictions removal not needed");
			return;
		}
		try {
			/*
			 * Do the following, but with reflection to bypass access checks:
			 *
			 * JceSecurity.isRestricted = false;
			 * JceSecurity.defaultPolicy.perms.clear();
			 * JceSecurity.defaultPolicy.add(CryptoAllPermission.INSTANCE);
			 */
			final Class<?> jceSecurity = Class.forName("javax.crypto.JceSecurity");
			final Class<?> cryptoPermissions = Class.forName("javax.crypto.CryptoPermissions");
			final Class<?> cryptoAllPermission = Class.forName("javax.crypto.CryptoAllPermission");

			final Field isRestrictedField = jceSecurity.getDeclaredField("isRestricted");
			isRestrictedField.setAccessible(true);
			isRestrictedField.set(null, false);

			final Field defaultPolicyField = jceSecurity.getDeclaredField("defaultPolicy");
			defaultPolicyField.setAccessible(true);
			final PermissionCollection defaultPolicy = (PermissionCollection) defaultPolicyField.get(null);

			final Field perms = cryptoPermissions.getDeclaredField("perms");
			perms.setAccessible(true);
			((Map<?, ?>) perms.get(defaultPolicy)).clear();

			final Field instance = cryptoAllPermission.getDeclaredField("INSTANCE");
			instance.setAccessible(true);
			defaultPolicy.add((Permission) instance.get(null));

			logger.fine("Successfully removed cryptography restrictions");
		} catch (final Exception e) {
			logger.log(Level.WARNING, "Failed to remove cryptography restrictions", e);
		}
	}

	private static boolean isRestrictedCryptography() {
		// This simply matches the Oracle JRE, but not OpenJDK.
		return "Java(TM) SE Runtime Environment".equals(System.getProperty("java.runtime.name"));
	}
}
