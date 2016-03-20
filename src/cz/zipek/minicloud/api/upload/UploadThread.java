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
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidAlgorithmParameterException;
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
	protected final InputStream stream;
	protected final List<Listener> listeners = new ArrayList<>();
	protected final String targetFolder;
	protected final Encryptor encryptor;
	protected final boolean isPublic;
	
	protected String fileName;
	protected long size;
	
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
	
	public UploadThread(Uploader uploader, File file, String target_folder, boolean isPublic, Encryptor encryptor) {
		super("Upload thread");
		
		this.uploader = uploader;
		this.file = file;
		this.targetFolder = target_folder;
		this.remote = null;
		this.stream = null;
		this.encryptor = encryptor;
		this.isPublic = isPublic;
	}
	
	public UploadThread(Uploader uploader, File local, cz.zipek.minicloud.api.File remote, boolean isPublic, Encryptor encryptor) {
		super("Upload thread");
		
		this.uploader = uploader;
		this.file = local;
		this.remote = remote;
		this.stream = null;
		this.targetFolder = null;
		this.isPublic = isPublic;
		this.encryptor = encryptor;
	}
	
	public UploadThread(Uploader uploader, InputStream stream, String fileName, long size, cz.zipek.minicloud.api.File remote, boolean isPublic, Encryptor encryptor) {
		super("Upload thread");
		
		this.uploader = uploader;
		this.file = null;
		this.remote = remote;
		this.stream = stream;
		this.targetFolder = null;
		this.encryptor = encryptor;
		this.fileName = fileName;
		this.size = size;
		this.isPublic = isPublic;
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
			} else {
				sender.addFormField("encryption[file]", "");
			}
			
			// Override existing file
			if (remote != null) {
				sender.addFormField("replace[file]", remote.getId());
			}
			
			// Add file checksum (unencrypted)
			sender.addFormField("checksum[file]", file != null ? Tools.md5Checksum(file) : remote.getChecksum());
			
			// Add public param
			sender.addFormField("public[file]", isPublic ? '1' : '0');
			
			// Add file
			if (file != null) {
				sender.addFilePart("file", file);
			} else {
				sender.addFilePart("file", fileName, stream, size);
			}
			
			// Start sending
			sender.finish();
		} catch (IOException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException | NoSuchAlgorithmException | InvalidAlgorithmParameterException ex) {
			Logger.getLogger(UploadThread.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	@Override
	public void handleEvent(Object event, Object sender) {
		fireEvent(event);
	}
}
