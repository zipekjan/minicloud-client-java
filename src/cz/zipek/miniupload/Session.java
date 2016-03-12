/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.zipek.miniupload;

/**
 *
 * @author Kamen
 */
public class Session {
	private static String id;
	private static boolean valid;
	private static String syncKey;

	/**
	 * @return the id
	 */
	public static String getId() {
		return id;
	}

	/**
	 * @param aId the id to set
	 */
	public static void setId(String aId) {
		id = aId;
	}

	/**
	 * @return the valid
	 */
	public static boolean isValid() {
		return valid;
	}

	/**
	 * @param aValid the valid to set
	 */
	public static void setValid(boolean aValid) {
		valid = aValid;
	}

	/**
	 * @return the syncKey
	 */
	public static String getSyncKey() {
		return syncKey;
	}

	/**
	 * @param aSyncKey the syncKey to set
	 */
	public static void setSyncKey(String aSyncKey) {
		syncKey = aSyncKey;
	}
}
