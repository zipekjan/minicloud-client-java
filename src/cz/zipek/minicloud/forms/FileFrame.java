/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.zipek.minicloud.forms;

import cz.zipek.minicloud.Forms;
import cz.zipek.minicloud.Icons;
import cz.zipek.minicloud.Session;
import cz.zipek.minicloud.Tools;
import cz.zipek.minicloud.api.Event;
import cz.zipek.minicloud.api.Listener;
import cz.zipek.minicloud.api.events.SuccessEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author Kamen
 */
public class FileFrame extends javax.swing.JFrame implements Listener<Event> {
	private cz.zipek.minicloud.api.File file;
	private String actionId;
	
	/**
	 * Creates new form File
	 * @param file
	 */
	public FileFrame(cz.zipek.minicloud.api.File file) {
		initComponents();
		
		this.updateFile(file);
	}
	
	public final FileFrame updateFile(cz.zipek.minicloud.api.File file) {
		this.file = file;
		
		setTitle(file.getName());
		setIconImage(Icons.getIcon(file.getExtension()).getImage());
		
		textName.setText(file.getName());
		labelSize.setText(Tools.humanFileSize(file.getSize(), 2));

		labelCreated.setText(
				new SimpleDateFormat("dd.MM.YYYY HH:mm")
						.format(file.getMdtime())
		);
		
		labelUpdated.setText(
				new SimpleDateFormat("dd.MM.YYYY HH:mm")
						.format(file.getMdtime())
		);
		
		labelEncryption.setText(
				file.isEncrypted() ?
						file.getEncryption() : "Disabled"
		);
		
		if (file.isPublic()) {
			textLink.setText(file.getPublicDownloadLink(Session.getServer().hasNiceUrl()));
		} else {
			textLink.setText("Private file");
		}
		
		buttonPrivate.setVisible(file.isPublic());
		buttonPublic.setVisible(!file.isPublic());
		
		return this;
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel6 = new javax.swing.JLabel();
        textName = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        labelSize = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        labelUpdated = new javax.swing.JLabel();
        buttonDownload = new javax.swing.JButton();
        buttonMove = new javax.swing.JButton();
        buttonCancel = new javax.swing.JButton();
        buttonSave = new javax.swing.JButton();
        textLink = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        labelCreated = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        labelEncryption = new javax.swing.JLabel();
        buttonPublic = new javax.swing.JButton();
        buttonPrivate = new javax.swing.JButton();
        buttonDelete = new javax.swing.JButton();

        setMinimumSize(getPreferredSize());

        jLabel6.setText("Name:");

        textName.setText("File.ext");

        jLabel7.setText("Size:");

        labelSize.setText("12.3 MiB");

        jLabel9.setText("Updated:");

        labelUpdated.setText("10.11.2014 13:22");

        buttonDownload.setText("Download");
        buttonDownload.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonDownloadActionPerformed(evt);
            }
        });

        buttonMove.setText("Move");
        buttonMove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonMoveActionPerformed(evt);
            }
        });

        buttonCancel.setText("Close");
        buttonCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonCancelActionPerformed(evt);
            }
        });

        buttonSave.setText("Save");
        buttonSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonSaveActionPerformed(evt);
            }
        });

        textLink.setEditable(false);
        textLink.setToolTipText("");

        jLabel14.setText("Download link:");

        jLabel10.setText("Created:");

        labelCreated.setText("10.11.2014 13:22");

        jLabel11.setText("Encryption:");

        labelEncryption.setText("AES");

        buttonPublic.setText("Make public");
        buttonPublic.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonPublicActionPerformed(evt);
            }
        });

        buttonPrivate.setText("Make private");
        buttonPrivate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonPrivateActionPerformed(evt);
            }
        });

        buttonDelete.setText("Delete");
        buttonDelete.setToolTipText("");
        buttonDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonDeleteActionPerformed(evt);
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
                        .addComponent(buttonDownload)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(buttonMove)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(buttonDelete)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 106, Short.MAX_VALUE)
                        .addComponent(buttonSave)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(buttonCancel))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
                            .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel14, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
                            .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(textName)
                            .addComponent(labelSize, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(labelUpdated, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(labelCreated, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(labelEncryption, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(buttonPublic)
                                .addGap(11, 11, 11)
                                .addComponent(buttonPrivate)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(textLink))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(textName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(labelSize))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(labelUpdated))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(labelCreated)
                    .addComponent(jLabel10))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(labelEncryption)
                    .addComponent(jLabel11))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(textLink, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel14))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(buttonPublic)
                    .addComponent(buttonPrivate))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 33, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(buttonDownload)
                    .addComponent(buttonMove)
                    .addComponent(buttonCancel)
                    .addComponent(buttonSave)
                    .addComponent(buttonDelete))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void buttonDownloadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonDownloadActionPerformed
		List<cz.zipek.minicloud.api.File> files = new ArrayList<>();
		files.add(this.file);
		Forms.showDownload(files);
    }//GEN-LAST:event_buttonDownloadActionPerformed

    private void buttonMoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonMoveActionPerformed
        List<cz.zipek.minicloud.api.File> files = new ArrayList<>();
		files.add(this.file);
		Forms.showMove(files);
    }//GEN-LAST:event_buttonMoveActionPerformed

    private void buttonSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonSaveActionPerformed
		if (textName.getText().length() == 0) {
			JOptionPane.showMessageDialog(
					this,
					"Unable to save changes. Filename must be specified.",
					"Wrong input",
					JOptionPane.WARNING_MESSAGE
			);
			
			return;
		}
		
		file.getSource().addListener(this);
		file.setName(textName.getText());
		actionId = file.save();
    }//GEN-LAST:event_buttonSaveActionPerformed

    private void buttonCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonCancelActionPerformed
		setVisible(false);
    }//GEN-LAST:event_buttonCancelActionPerformed

    private void buttonDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonDeleteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_buttonDeleteActionPerformed

    private void buttonPrivateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonPrivateActionPerformed
        Forms.showAvailability(this, file, false);
    }//GEN-LAST:event_buttonPrivateActionPerformed

    private void buttonPublicActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonPublicActionPerformed
		Forms.showAvailability(this, file, true);
    }//GEN-LAST:event_buttonPublicActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton buttonCancel;
    private javax.swing.JButton buttonDelete;
    private javax.swing.JButton buttonDownload;
    private javax.swing.JButton buttonMove;
    private javax.swing.JButton buttonPrivate;
    private javax.swing.JButton buttonPublic;
    private javax.swing.JButton buttonSave;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel labelCreated;
    private javax.swing.JLabel labelEncryption;
    private javax.swing.JLabel labelSize;
    private javax.swing.JLabel labelUpdated;
    private javax.swing.JTextField textLink;
    private javax.swing.JTextField textName;
    // End of variables declaration//GEN-END:variables

	@Override
	public void handleEvent(Event e, Object sender) {
		if (e.getActionId() != null && e.getActionId().equals(actionId)) {
			if (!(e instanceof SuccessEvent)) {
				//@TODO:
				/*if (e instanceof UpdateConflictEvent) {
					JOptionPane.showMessageDialog(this, "This file name already exists.", "Update failed", JOptionPane.ERROR_MESSAGE);
				} else {*/
					JOptionPane.showMessageDialog(this, "Unknown error occured during file update", "Update failed", JOptionPane.ERROR_MESSAGE);
				//}
			} else {
				file.setName(textName.getText());
				file.getSource().removeListenerLater(this);
				file.getSource().getPath(file.getPath());
				setVisible(false);
			}
		}
	}
}
