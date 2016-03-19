/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.zipek.minicloud.api.upload;

import cz.zipek.minicloud.Tools;
import cz.zipek.minicloud.api.Listener;
import cz.zipek.minicloud.api.encryption.Encryptor;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;

/**
 *
 * @author Kamen
 */
public class UploadThread extends Thread implements Listener {
	protected final Uploader uploader;
	protected final File file;
	protected final cz.zipek.minicloud.api.File remote;
	protected final List<Listener> listeners = new ArrayList<>();
	protected final String targetFolder;
	protected final Encryptor encryptor;
	
	public synchronized void addListener(Listener listener) {
		listeners.add(listener);
	}
	
	public synchronized void removeListener(Listener listener) {
		listeners.remove(listener);
	}
	
	protected synchronized void fireEvent(Object event) {
		for(Listener listener : listeners) {
			listener.handleEvent(event, this);
		}
	}
	
	public UploadThread(Uploader uploader, File file, String target_folder, Encryptor encryptor) {
		super("Upload thread");
		
		this.uploader = uploader;
		this.file = file;
		this.targetFolder = target_folder;
		this.remote = null;
		this.encryptor = encryptor;
	}
	
	public UploadThread(Uploader uploader, File local, cz.zipek.minicloud.api.File remote, Encryptor encryptor) {
		super("Upload thread");
		
		this.uploader = uploader;
		this.file = local;
		this.remote = remote;
		this.targetFolder = null;
		this.encryptor = encryptor;
	}
	
	private String parseFolder(String folder) {
		return folder.replaceAll("\\/{2,}","/");
	}
	
	@Override
	public void run() {
		try {
			// Helper for sending big requests
			MultipartUtility sender = new MultipartUtility(
				uploader.getSource().getApiUrl(), "UTF-8",
				uploader.getSource().getAuth(), encryptor
			);
			
			// Listen to sender events
			sender.addListener(this);
			
			// Proper aciton
			sender.addFormField("action", "upload_file");

			// Path to upload files to
			if (targetFolder != null) {
				sender.addFormField("path", parseFolder(targetFolder));
			}
			
			// Add encryption info
			if (encryptor != null) {
				sender.addFormField("encryption[file]", encryptor.getConfig());
			}
			
			// Override existing file
			if (remote != null) {
				sender.addFormField("override[file]", remote.getId());
			}
			
			// Add file checksum (unencrypted = important)
			sender.addFormField("checksum[file]", Tools.md5Checksum(file));
			
			// Add file
			sender.addFilePart("file", file);
			
			// Start sending
			sender.finish();
		} catch (IOException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException | NoSuchAlgorithmException ex) {
			Logger.getLogger(UploadThread.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	@Override
	public void handleEvent(Object event, Object sender) {
		fireEvent(event);
	}
}
