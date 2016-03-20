/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.zipek.minicloud.forms;

import cz.zipek.minicloud.Manager;
import cz.zipek.minicloud.Session;
import cz.zipek.minicloud.Settings;
import cz.zipek.minicloud.Tools;
import cz.zipek.minicloud.api.Listener;
import cz.zipek.minicloud.api.upload.Uploader;
import cz.zipek.minicloud.api.upload.events.UploadAllDoneEvent;
import cz.zipek.minicloud.api.upload.events.UploadFailedEvent;
import cz.zipek.minicloud.api.upload.events.UploadFileDoneEvent;
import cz.zipek.minicloud.api.upload.events.UploadProgressEvent;
import java.io.File;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.NoSuchPaddingException;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Kamen
 */
public class Upload extends javax.swing.JFrame implements Listener {
	private final List<File> files = new ArrayList<>();
	
	private Uploader uploader;
	
	
	/**
	 * Creates new form Upload
	 */
	public Upload() {
		initComponents();

		//@TODO: How to do this
		/*
		for(String folder : Forms.getMain().getFoldersPaths()) {
			comboTarget.addItem(folder);
		}
		*/
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        comboTarget = new javax.swing.JComboBox();
        textTagret = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableFiles = new javax.swing.JTable();
        buttonStart = new javax.swing.JButton();
        buttonStop = new javax.swing.JButton();
        buttonCancel = new javax.swing.JButton();
        buttonAdd = new javax.swing.JButton();
        buttonRemove = new javax.swing.JButton();
        progressFile = new javax.swing.JProgressBar();
        progressTotal = new javax.swing.JProgressBar();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Upload files");
        setMinimumSize(getPreferredSize());

        comboTarget.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "/" }));

        tableFiles.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "File", "Path", "Size", "State"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tableFiles);

        buttonStart.setText("Start upload");
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

        buttonCancel.setText("Close");
        buttonCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonCancelActionPerformed(evt);
            }
        });

        buttonAdd.setText("Add file");
        buttonAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonAddActionPerformed(evt);
            }
        });

        buttonRemove.setText("Remove selected");
        buttonRemove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonRemoveActionPerformed(evt);
            }
        });

        jLabel1.setText("Target folder:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(comboTarget, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(textTagret))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 539, Short.MAX_VALUE)
                    .addComponent(progressFile, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(buttonAdd)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(buttonRemove))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(buttonStart)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(buttonStop)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(buttonCancel))
                    .addComponent(progressTotal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(comboTarget, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(textTagret, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 358, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(buttonAdd)
                    .addComponent(buttonRemove))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(progressFile, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(progressTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(buttonStart)
                    .addComponent(buttonStop)
                    .addComponent(buttonCancel))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

	private void addFile(File file) {
		if (file.isFile()) {
			files.add(file);

			DefaultTableModel dm = ((DefaultTableModel)tableFiles.getModel());
			dm.addRow(new String[] 
			{
				file.getName(),
				file.getParentFile().getAbsolutePath(),
				Tools.humanFileSize(file.length(), 2),
				""
			});
		} else {
			for(File sub : file.listFiles())
				addFile(sub);
		}
	}
	
    private void buttonAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonAddActionPerformed
		JFileChooser chooser = new JFileChooser(); 
		chooser.setCurrentDirectory(new java.io.File("."));
		chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		chooser.setMultiSelectionEnabled(true);
		if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
			for(File file : chooser.getSelectedFiles()) {
				addFile(file);
			}
		}
    }//GEN-LAST:event_buttonAddActionPerformed

    private void buttonStartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonStartActionPerformed
		if (uploader == null) {
			try {
				setStartEnabled(false);
				
				uploader = new Uploader(Manager.external, Session.getUser().getEncryptor(Settings.getEncryption()));
				uploader.addListener(this);
				for(File f : files) {
					uploader.add(f);
				}
				uploader.start(comboTarget.getSelectedItem().toString() + textTagret.getText());
				
			} catch (NoSuchProviderException | NoSuchAlgorithmException | NoSuchPaddingException ex) {
				JOptionPane.showMessageDialog(
						this,
						"Failed to upload file. There is problem with encryption.",
						"Upload failed",
						JOptionPane.ERROR_MESSAGE
				);
				
				Logger.getLogger(Upload.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
    }//GEN-LAST:event_buttonStartActionPerformed

    private void buttonStopActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonStopActionPerformed
		if (uploader != null) {
			uploader.stop();
			uploader = null;
		}
    }//GEN-LAST:event_buttonStopActionPerformed

	private List<File> getSelectedFiles() {
		List<File> selected = new ArrayList<>();

		int[] rows = tableFiles.getSelectedRows();
		for (int row : rows) {
			selected.add(files.get(row));
		}

		return selected;
	}
	
    private void buttonRemoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonRemoveActionPerformed
		List<File> selected = getSelectedFiles();
		List<File> updated = new ArrayList<>(files);
		DefaultTableModel dm = ((DefaultTableModel)tableFiles.getModel());
		
		//Clear table
		while(dm.getRowCount() > 0) {
			dm.removeRow(0);
		}
		
		//Remove files
		for(File sel : selected) {
			updated.remove(sel);
		}
		
		files.clear();
		for(File f : updated) {
			addFile(f);
		}
    }//GEN-LAST:event_buttonRemoveActionPerformed

    private void buttonCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonCancelActionPerformed
		if (uploader != null) {
			JOptionPane.showMessageDialog(this, "Upload in progress, please stop upload first.", "Can't close", JOptionPane.INFORMATION_MESSAGE);
		} else {
			setVisible(false);
			dispose();
		}
    }//GEN-LAST:event_buttonCancelActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton buttonAdd;
    private javax.swing.JButton buttonCancel;
    private javax.swing.JButton buttonRemove;
    private javax.swing.JButton buttonStart;
    private javax.swing.JButton buttonStop;
    private javax.swing.JComboBox comboTarget;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JProgressBar progressFile;
    private javax.swing.JProgressBar progressTotal;
    private javax.swing.JTable tableFiles;
    private javax.swing.JTextField textTagret;
    // End of variables declaration//GEN-END:variables

	private void setStartEnabled(boolean enabled) {
		buttonStart.setEnabled(enabled);
		buttonStop.setEnabled(!enabled);
		buttonAdd.setEnabled(enabled);
		buttonRemove.setEnabled(enabled);
	}
	
	@Override
	public void handleEvent(Object event, Object sender) {
		if (event instanceof UploadFailedEvent) {
			JOptionPane.showMessageDialog(this, "Upload failed from unexpected reason", "Upload error", JOptionPane.ERROR_MESSAGE);
			uploader = null;
			setStartEnabled(true);
		} else if (event instanceof UploadProgressEvent) {
			UploadProgressEvent e = (UploadProgressEvent) event;
			int done = (int)(e.getSent()* 100.0 / e.getTotal() + 0.5);
			
			DefaultTableModel dm = ((DefaultTableModel)tableFiles.getModel());
			dm.setValueAt(Integer.toString(done) + " %",0,3);
		} else if (event instanceof UploadFileDoneEvent) {
			DefaultTableModel dm = ((DefaultTableModel)tableFiles.getModel());
			dm.removeRow(0);
			files.remove(0);
		} else if (event instanceof UploadAllDoneEvent) {
			setStartEnabled(true);
			uploader = null;
		}
	}
}
