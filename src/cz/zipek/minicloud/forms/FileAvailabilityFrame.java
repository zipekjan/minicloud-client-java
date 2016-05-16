/* 
 * The MIT License
 *
 * Copyright 2016 Jan Zípek <jan at zipek.cz>.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package cz.zipek.minicloud.forms;

import cz.zipek.minicloud.Manager;
import cz.zipek.minicloud.Session;
import cz.zipek.minicloud.Settings;
import cz.zipek.minicloud.api.Event;
import cz.zipek.minicloud.api.File;
import cz.zipek.minicloud.api.Listener;
import cz.zipek.minicloud.api.download.DownloadEvent;
import cz.zipek.minicloud.api.download.Downloader;
import cz.zipek.minicloud.api.download.events.DownloadFailedEvent;
import cz.zipek.minicloud.api.events.ErrorEvent;
import cz.zipek.minicloud.api.events.SuccessEvent;
import cz.zipek.minicloud.api.upload.UploadEvent;
import cz.zipek.minicloud.api.upload.Uploader;
import cz.zipek.minicloud.api.upload.events.UploadFailedEvent;
import cz.zipek.minicloud.api.upload.events.UploadFileDoneEvent;
import cz.zipek.minicloud.api.upload.events.UploadProgressEvent;
import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.NoSuchPaddingException;
import javax.swing.JOptionPane;

/**
 *
 * @author Jan Zípek
 */
public class FileAvailabilityFrame extends javax.swing.JFrame implements Listener {

	private FileFrame listener;
	
	private File file;
	private boolean setPublic;

	private Uploader uploader;
	private Downloader downloader;
	
	private String actionId;
	
	public FileAvailabilityFrame() {
		initComponents();
	}
	
	public void setFile(FileFrame listener, File file, boolean pub) {
		
		// Default dialogs
		labelInfo.setVisible(true);
		labelAction.setText("Reupload");
		labelState.setText("Ready");
		progressState.setValue(0);
		
		// Save params
		this.listener = listener;
		this.file = file;
		setPublic = pub;
		
		labelPath.setText(file.getPath());

		// Determine correct action		
		if (setPublic) {
			if (file.isEncrypted()) {
				prepareReupload(false);
			} else {
				Manager.external.addListener(this);
					
				labelInfo.setVisible(false);
				labelAction.setText("Save changes");
				labelState.setText("Saving changes...");
				
				file.setPublic(setPublic);
				actionId = file.save();
			}
		} else {
			prepareReupload(true);
		}
	}
	
	private void prepareReupload(boolean encrypt) {
		
		try {
			uploader = new Uploader(Manager.external, encrypt ? Session.getUser().getEncryptor(Settings.getEncryption()) : null);
			downloader = new Downloader(Manager.external, Session.getUser());
			
			PipedInputStream input = new PipedInputStream();
			PipedOutputStream output = new PipedOutputStream(input);
			
			downloader.add(file.getVersion(), output);
			uploader.add(input, file, setPublic, false);
		} catch (NoSuchProviderException | NoSuchAlgorithmException | NoSuchPaddingException | IOException ex) {
			Logger.getLogger(FileAvailabilityFrame.class.getName()).log(Level.SEVERE, null, ex);
		}
		
	}
	
	@Override
	public void handleEvent(Object event, Object sender) {
		
		if (event instanceof DownloadEvent)
			handleDownload((DownloadEvent)event);
		else if (event instanceof UploadEvent)
			handleUpload((UploadEvent)event);
		else
			handleExternal((Event)event);
		
	}
	
	private void handleDownload(DownloadEvent event) {
			
		if (event instanceof DownloadFailedEvent) {
			labelState.setText("Download failed");
			
			JOptionPane.showMessageDialog(
				this,
				"File download failed.",
				"Problem",
				JOptionPane.ERROR_MESSAGE
			);
			
			downloader.stop();
			uploader.stop();
			
			buttonStart.setEnabled(true);
		}
		
	}
	
	private void handleUpload(UploadEvent event) {
		
		if (event instanceof UploadProgressEvent) {
			UploadProgressEvent e = (UploadProgressEvent) event;
			int done = (int)((e.getSent()* 100.0) / e.getTotal());
			
			progressState.setValue(done);
			labelState.setText("Reuploading ... " + done + "%");
		}
		
		if (event instanceof UploadFileDoneEvent) {
			labelState.setText("Reupload done. Saving result ...");
			progressState.setValue(100);
			
			actionId = file.save();
		}
		
		if (event instanceof UploadFailedEvent) {
			labelState.setText("Upload failed");
			
			JOptionPane.showMessageDialog(
				this,
				"File upload failed.",
				"Problem",
				JOptionPane.ERROR_MESSAGE
			);
			
			downloader.stop();
			uploader.stop();
			
			buttonStart.setEnabled(true);
		}
		
	}
	
	private void handleExternal(Event event) {
		
		if (event.getActionId().equals(actionId)) {
			if (event instanceof SuccessEvent) {
				
				JOptionPane.showMessageDialog(
						this,
						setPublic ? "File is now public." : "File is now private.",
						"Done",
						JOptionPane.INFORMATION_MESSAGE
				);
			
				setVisible(false);
				
			} else {
				
				JOptionPane.showMessageDialog(
						this,
						"Error occured when saving result. File is now corrupted. Error: " + ((ErrorEvent)event).getMessage(),
						"Done",
						JOptionPane.ERROR_MESSAGE
				);
				
				setVisible(false);
				
			}
			
			actionId = null;
			buttonStart.setEnabled(true);
			
			if (downloader != null)
				downloader.removeListenerLater(this);
			
			if (uploader != null)
				uploader.removeListenerLater(this);
			
			if (listener != null) {
				listener.updateFile(file);
			}
			
			Manager.external.removeListenerLater(this);
		}
		
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        labelPath = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        labelAction = new javax.swing.JLabel();
        labelInfo = new javax.swing.JLabel();
        progressState = new javax.swing.JProgressBar();
        labelState = new javax.swing.JLabel();
        buttonStart = new javax.swing.JButton();

        setTitle("Update file publicity");
        setMinimumSize(getPreferredSize());

        jLabel1.setText("Converted file:");

        labelPath.setText("/storage/path/file.ext");

        jLabel3.setText("Required action:");

        labelAction.setText("Reupload");

        labelInfo.setText("<html>\nFile is stored on server in encrypted format. You'll need to reupload it to make it public.<br>\nPublic files are stored unencrypted.\n</html>");
        labelInfo.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        labelState.setText("Ready");

        buttonStart.setText("Start");
        buttonStart.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonStartActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel3))
                        .addGap(28, 28, 28)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(labelAction, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(labelPath, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addComponent(progressState, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(labelState)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(buttonStart))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(labelInfo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(labelPath))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(labelAction))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(labelInfo, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(progressState, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelState)
                    .addComponent(buttonStart))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void buttonStartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonStartActionPerformed

		
		file.setPublic(setPublic);
		
		if (!setPublic)
			file.setEncryption(Settings.getEncryption());
		
		buttonStart.setEnabled(false);
		labelState.setText("Reuploading...");
		
		downloader.addListener(this);
		uploader.addListener(this);
		Manager.external.addListener(this);
		
		downloader.start(null);
		uploader.start(null);
		
    }//GEN-LAST:event_buttonStartActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton buttonStart;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel labelAction;
    private javax.swing.JLabel labelInfo;
    private javax.swing.JLabel labelPath;
    private javax.swing.JLabel labelState;
    private javax.swing.JProgressBar progressState;
    // End of variables declaration//GEN-END:variables

}
