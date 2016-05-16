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

import cz.zipek.minicloud.Forms;
import cz.zipek.minicloud.Manager;
import cz.zipek.minicloud.Session;
import cz.zipek.minicloud.Settings;
import cz.zipek.minicloud.Tools;
import cz.zipek.minicloud.api.File;
import cz.zipek.minicloud.api.Listener;
import cz.zipek.minicloud.api.download.DownloadEvent;
import cz.zipek.minicloud.api.download.events.DownloadAllDoneEvent;
import cz.zipek.minicloud.api.download.events.DownloadFailedEvent;
import cz.zipek.minicloud.api.download.events.DownloadFileDoneEvent;
import cz.zipek.minicloud.api.download.events.DownloadFileStartedEvent;
import cz.zipek.minicloud.api.download.events.DownloadProgressEvent;
import cz.zipek.minicloud.api.upload.UploadEvent;
import cz.zipek.minicloud.api.upload.events.UploadFailedEvent;
import cz.zipek.minicloud.api.upload.events.UploadFileDoneEvent;
import cz.zipek.minicloud.api.upload.events.UploadFileStartedEvent;
import cz.zipek.minicloud.api.upload.events.UploadProgressEvent;
import cz.zipek.minicloud.sync.SyncEvent;
import cz.zipek.minicloud.sync.SyncFolder;
import cz.zipek.minicloud.sync.events.SyncChecksumFailedEvent;
import cz.zipek.minicloud.sync.events.SyncDone;
import cz.zipek.minicloud.sync.events.SyncDownloadEvent;
import cz.zipek.minicloud.sync.events.SyncMkdirFailedEvent;
import cz.zipek.minicloud.sync.events.SyncUploadEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Jan Zípek
 */
public class Sync extends javax.swing.JFrame implements Listener<SyncEvent> {
	private final List<SyncFolder> folders = new ArrayList<>();
	private int currentFolder = -1;
	private Thread syncThread;
	
	private final List<File> download = new ArrayList<>();
	private final List<java.io.File> upload = new ArrayList<>();
	
	/**
	 * Creates new form Sync
	 * @param folders
	 */
	public Sync(List<SyncFolder> folders) {
		initComponents();
		
		this.folders.addAll(folders);
		
		DefaultTableModel dm = ((DefaultTableModel) tableFolders.getModel());
		for(SyncFolder folder : this.folders) {
			dm.addRow(new String[] {
				folder.getLocal().getAbsolutePath(),
				folder.getRemote(),
				""
			});
		}
		
		progressFolders.setMaximum(folders.size());
	}
	
	private void nextFolder() {
		currentFolder += 1;
		progressFolders.setValue(currentFolder);
		if (currentFolder < folders.size()) {
			syncFolder(folders.get(currentFolder));
		} else {			
			JOptionPane.showMessageDialog(this, "Synchronization done.", "Completed", JOptionPane.INFORMATION_MESSAGE);
			stop();
		}
	}
	
	private void syncFolder(SyncFolder folder) {
		int row = folders.indexOf(folder);
		if (row >= 0) {
			tableFolders.getModel().setValueAt("Syncing...", row, 2);
		}
		
		folder.setTimeOffset((int)Session.getServer().getOffset());
		folder.setUser(Session.getUser());
		folder.setExternal(Manager.external);
		folder.setEncryption(Settings.getEncryption());
		folder.addListener(this);
		syncThread = folder.syncAsync();
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        tableFolders = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        tableDownload = new javax.swing.JTable();
        progressFolders = new javax.swing.JProgressBar();
        progressDownload = new javax.swing.JProgressBar();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tableUpload = new javax.swing.JTable();
        progressUpload = new javax.swing.JProgressBar();
        jLabel2 = new javax.swing.JLabel();
        buttonCancel = new javax.swing.JButton();
        buttonStart = new javax.swing.JButton();
        buttonStop = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setMinimumSize(getPreferredSize());
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        tableFolders.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Local", "Remote", "State"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tableFolders);

        tableDownload.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "File", "From", "To", "Size", "State"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(tableDownload);

        jLabel1.setText("Receiving");

        tableUpload.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "File", "From", "To", "Size", "State"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane3.setViewportView(tableUpload);

        jLabel2.setText("Sending");

        buttonCancel.setText("Close");
        buttonCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonCancelActionPerformed(evt);
            }
        });

        buttonStart.setText("Start synchronization");
        buttonStart.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonStartActionPerformed(evt);
            }
        });

        buttonStop.setText("Stop");
        buttonStop.setEnabled(false);
        buttonStop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonStopActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 668, Short.MAX_VALUE)
                    .addComponent(progressFolders, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(progressDownload, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 668, Short.MAX_VALUE)
                    .addComponent(progressUpload, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(buttonStart)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(buttonStop)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(buttonCancel)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 77, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(progressFolders, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel1)
                .addGap(3, 3, 3)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 176, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(progressDownload, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 157, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(progressUpload, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(buttonCancel)
                    .addComponent(buttonStart)
                    .addComponent(buttonStop))
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

	private void stop() {
		if (syncThread != null) {
			folders.get(currentFolder).removeListenerLater(this);
			folders.get(currentFolder).stop();
			syncThread.interrupt();
		}
		
		currentFolder = -1;
		buttonStart.setEnabled(true);
		buttonStop.setEnabled(false);
	}
	
    private void buttonStartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonStartActionPerformed
		if (currentFolder == -1) {
			//Clear upload / download table
			DefaultTableModel dm = ((DefaultTableModel) tableDownload.getModel());
			while(dm.getRowCount() > 0)
				dm.removeRow(0);
			dm = ((DefaultTableModel) tableUpload.getModel());
			while(dm.getRowCount() > 0)
				dm.removeRow(0);
			
			nextFolder();
			
			buttonStart.setEnabled(false);
			buttonStop.setEnabled(true);
		}
    }//GEN-LAST:event_buttonStartActionPerformed

    private void buttonStopActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonStopActionPerformed
		stop();
    }//GEN-LAST:event_buttonStopActionPerformed

    private void buttonCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonCancelActionPerformed
		if (currentFolder != -1) {
			JOptionPane.showMessageDialog(this, "Synchronization in progress, please stop synchronization first.", "Can't close", JOptionPane.INFORMATION_MESSAGE);
		} else {
			setVisible(false);
		}
    }//GEN-LAST:event_buttonCancelActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
		Forms.remove(this);
    }//GEN-LAST:event_formWindowClosing

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton buttonCancel;
    private javax.swing.JButton buttonStart;
    private javax.swing.JButton buttonStop;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JProgressBar progressDownload;
    private javax.swing.JProgressBar progressFolders;
    private javax.swing.JProgressBar progressUpload;
    private javax.swing.JTable tableDownload;
    private javax.swing.JTable tableFolders;
    private javax.swing.JTable tableUpload;
    // End of variables declaration//GEN-END:variables

	@Override
	public synchronized void handleEvent(SyncEvent event, Object sender) {
		if (event instanceof SyncDownloadEvent) {
			handleEvent(((SyncDownloadEvent)event).getEvent());
		} else if (event instanceof SyncUploadEvent) {
			handleEvent(((SyncUploadEvent)event).getEvent());
		} else {
			if (event instanceof SyncMkdirFailedEvent) {
				tableFolders.getModel().setValueAt("Failed", currentFolder, 2);
				
				SyncMkdirFailedEvent e = (SyncMkdirFailedEvent) event;
				stop();
				JOptionPane.showMessageDialog(this, "Failed to create '" + e.getFolder() + "'", "Synchronization failed", JOptionPane.ERROR_MESSAGE);
			} else if (event instanceof SyncChecksumFailedEvent) {
				tableFolders.getModel().setValueAt("Failed", currentFolder, 2);
				
				SyncChecksumFailedEvent e = (SyncChecksumFailedEvent) event;
				stop();
				JOptionPane.showMessageDialog(this, "Failed to generate MD5 checksum of '" + e.getPath()+ "'", "Synchronization failed", JOptionPane.ERROR_MESSAGE);
			} else if (event instanceof SyncDone) {
				if (currentFolder >= 0) {
					tableFolders.getModel().setValueAt("Done", currentFolder, 2);
				}
				
				folders.get(currentFolder).removeListenerLater(this);
				syncThread = null;
				
				nextFolder();
			}
		}
	}
	
	public synchronized void handleEvent(DownloadEvent event) {
		DefaultTableModel dm = ((DefaultTableModel) tableDownload.getModel());
		if (event instanceof DownloadFileStartedEvent) {
			DownloadFileStartedEvent e = (DownloadFileStartedEvent) event;
			download.add(e.getFile());
			
			dm.addRow(new String[] {
				e.getFile().getName(),
				e.getFile().getFolderPath(),
				(new java.io.File(e.getTarget())).getParentFile().getAbsolutePath(),
				Tools.humanFileSize(e.getFile().getSize(), 2),
				""
			});
		} else if (event instanceof DownloadProgressEvent) {
			DownloadProgressEvent e = (DownloadProgressEvent) event;
			int row = download.indexOf(e.getFile());
			if (row >= 0) {
				int percentage = (int)(e.getDownloaded() * 100.0 / e.getTotal() + 0.5);
				dm.setValueAt(percentage + " %", row, 4);
				progressDownload.setValue(percentage);
			}
		} else if (event instanceof DownloadFileDoneEvent) {
			DownloadFileDoneEvent e = (DownloadFileDoneEvent) event;
			int row = download.indexOf(e.getFile());
			if (row >= 0) {
				dm.setValueAt("Done", row, 4);
			}
		} else if (event instanceof DownloadFailedEvent) {
			DownloadFailedEvent e = (DownloadFailedEvent) event;
			int row = download.indexOf(e.getFile());
			if (row >= 0) {
				dm.setValueAt("Failed", row, 4);
				JOptionPane.showMessageDialog(this, "Download of '" + e.getFile().getName() +  "' failed", "Unexpected error", JOptionPane.ERROR_MESSAGE);
				stop();
			}
		}
	}
	
	public synchronized void handleEvent(UploadEvent event) {
		DefaultTableModel dm = ((DefaultTableModel) tableUpload.getModel());
		if (event instanceof UploadFileStartedEvent) {
			UploadFileStartedEvent e = (UploadFileStartedEvent) event;
			upload.add(e.getFile());
			
			dm.addRow(new String[] {
				e.getFile().getName(),
				e.getFile().getAbsolutePath(),
				e.getRemote() != null ? e.getRemote().getFolderPath() : e.getTarget(),
				Tools.humanFileSize(e.getFile().length(), 2),
				""
			});
		} else if (event instanceof UploadProgressEvent) {
			UploadProgressEvent e = (UploadProgressEvent) event;
			int row = upload.indexOf(e.getFile());
			if (row >= 0) {
				int percentage = (int)(e.getSent()* 100.0 / e.getTotal() + 0.5);
				dm.setValueAt(percentage + " %", row, 4);
				progressUpload.setValue(percentage);
			}
		} else if (event instanceof UploadFileDoneEvent) {
			UploadFileDoneEvent e = (UploadFileDoneEvent) event;
			int row = upload.indexOf(e.getFile());
			if (row >= 0) {
				dm.setValueAt("Done", row, 4);
			}
		} else if (event instanceof UploadFailedEvent) {
			UploadFailedEvent e = (UploadFailedEvent) event;
			JOptionPane.showMessageDialog(this, "Upload failed from unexpected reason.", "Unexpected error", JOptionPane.ERROR_MESSAGE);
			stop();
		}
	}
}
