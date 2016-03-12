/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.zipek.miniupload.forms;

import cz.zipek.miniupload.Forms;
import cz.zipek.miniupload.Icons;
import cz.zipek.miniupload.Session;
import cz.zipek.miniupload.Tools;
import cz.zipek.miniupload.api.Event;
import cz.zipek.miniupload.api.Listener;
import cz.zipek.miniupload.api.events.UpdateConflictEvent;
import cz.zipek.miniupload.api.events.UpdateEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author Kamen
 */
public class FileFrame extends javax.swing.JFrame implements Listener<Event> {
	private cz.zipek.miniupload.api.File file;
	private String actionId;
	
	/**
	 * Creates new form File
	 * @param file
	 */
	public FileFrame(cz.zipek.miniupload.api.File file) {
		initComponents();
		
		this.updateFile(file);
	}
	
	public final FileFrame updateFile(cz.zipek.miniupload.api.File file) {
		this.file = file;
		
		setTitle(this.file.getName());
		setIconImage(Icons.getIcon(this.file.getExtension()).getImage());
		
		textName.setText(this.file.getName());
		textNote.setText(this.file.getNote());
		textLink.setText(this.file.getDownloadLink());
		labelSize.setText(Tools.humanFileSize(this.file.getSize(), 2));
		labelDownloaded.setText(Integer.toString(this.file.getDownloads()));
		labelUploaded.setText(
				new SimpleDateFormat("dd.MM.YYYY HH:mm")
						.format(this.file.getDate())
		);
		if (this.file.getDownloaded() != null) {
			labelLastDownloaded.setText(
					new SimpleDateFormat("dd.MM.YYYY HH:mm")
							.format(this.file.getDownloaded())
			);
		} else {
			labelLastDownloaded.setText("Never");
		}
		
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
        labelUploaded = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        labelDownloaded = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        labelLastDownloaded = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        textNote = new javax.swing.JTextArea();
        buttonDownload = new javax.swing.JButton();
        buttonMove = new javax.swing.JButton();
        buttonCancel = new javax.swing.JButton();
        buttonSave = new javax.swing.JButton();
        textLink = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();

        jLabel6.setText("Name:");

        textName.setText("File.ext");

        jLabel7.setText("Size:");

        labelSize.setText("12.3 MiB");

        jLabel9.setText("Uploaded:");

        labelUploaded.setText("10.11.2014 13:22");

        jLabel11.setText("Downloaded:");

        labelDownloaded.setText("122x");

        jLabel13.setText("Last download:");

        jScrollPane1.setToolTipText("File note");

        textNote.setColumns(20);
        textNote.setRows(5);
        jScrollPane1.setViewportView(textNote);

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

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(buttonDownload)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(buttonMove)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 53, Short.MAX_VALUE)
                        .addComponent(buttonSave)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(buttonCancel))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
                                .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(labelDownloaded, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(textName)
                            .addComponent(labelSize, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(labelUploaded, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(labelLastDownloaded, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
                    .addComponent(labelUploaded))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(labelDownloaded))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(labelLastDownloaded, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(textLink, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel14))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 126, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(buttonDownload)
                    .addComponent(buttonMove)
                    .addComponent(buttonCancel)
                    .addComponent(buttonSave))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void buttonDownloadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonDownloadActionPerformed
		List<cz.zipek.miniupload.api.File> files = new ArrayList<>();
		files.add(this.file);
		Forms.showDownload(files);
    }//GEN-LAST:event_buttonDownloadActionPerformed

    private void buttonMoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonMoveActionPerformed
        List<cz.zipek.miniupload.api.File> files = new ArrayList<>();
		files.add(this.file);
		Forms.showMove(files);
    }//GEN-LAST:event_buttonMoveActionPerformed

    private void buttonSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonSaveActionPerformed
		file.getSource().addListener(this);
		actionId = file.getSource().update(Session.getId(), file, textName.getText(), textNote.getText());
    }//GEN-LAST:event_buttonSaveActionPerformed

    private void buttonCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonCancelActionPerformed
		setVisible(false);
    }//GEN-LAST:event_buttonCancelActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton buttonCancel;
    private javax.swing.JButton buttonDownload;
    private javax.swing.JButton buttonMove;
    private javax.swing.JButton buttonSave;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel labelDownloaded;
    private javax.swing.JLabel labelLastDownloaded;
    private javax.swing.JLabel labelSize;
    private javax.swing.JLabel labelUploaded;
    private javax.swing.JTextField textLink;
    private javax.swing.JTextField textName;
    private javax.swing.JTextArea textNote;
    // End of variables declaration//GEN-END:variables

	@Override
	public void handleEvent(Event e, Object sender) {
		if (e.getActionId() != null && e.getActionId().equals(actionId)) {
			if (!(e instanceof UpdateEvent)) {
				if (e instanceof UpdateConflictEvent) {
					JOptionPane.showMessageDialog(this, "This file name already exists.", "Update failed", JOptionPane.ERROR_MESSAGE);
				} else {
					JOptionPane.showMessageDialog(this, "Unknown error occured during file update", "Update failed", JOptionPane.ERROR_MESSAGE);
				}
			} else {
				file.setName(textName.getText());
				file.setNote(textNote.getText());
				file.getSource().removeListenerLater(this);
				file.getSource().files(Session.getId());
				setVisible(false);
			}
		}
	}
}
