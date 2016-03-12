/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.zipek.minicloud.api.upload.events;

import cz.zipek.minicloud.api.upload.UploadEvent;

/**
 *
 * @author Kamen
 */
public class UploadThreadSentEvent extends UploadEvent {
	private final String response;
	
	public UploadThreadSentEvent(String response) {
		this.response = response;
	}

	/**
	 * @return the response
	 */
	public String getResponse() {
		return response;
	}
}