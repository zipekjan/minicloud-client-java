/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.zipek.minicloud;

import cz.zipek.minicloud.api.ServerInfo;
import cz.zipek.minicloud.api.User;

/**
 *
 * @author Kamen
 */
public class Session {

	private static User user;
	private static ServerInfo server;

	/**
	 * @return the user
	 */
	public static User getUser() {
		return user;
	}

	/**
	 * @param aUser the user to set
	 */
	public static void setUser(User aUser) {
		user = aUser;
	}

	/**
	 * @return the server
	 */
	public static ServerInfo getServer() {
		return server;
	}

	/**
	 * @param aServer the server to set
	 */
	public static void setServer(ServerInfo aServer) {
		server = aServer;
	}
	
	
}
