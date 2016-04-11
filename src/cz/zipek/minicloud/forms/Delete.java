/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.zipek.minicloud.forms;

import cz.zipek.minicloud.Forms;
import cz.zipek.minicloud.Manager;
import cz.zipek.minicloud.MetaItem;
import cz.zipek.minicloud.Tools;
import cz.zipek.minicloud.api.Event;
import cz.zipek.minicloud.api.File;
import cz.zipek.minicloud.api.Listener;
import cz.zipek.minicloud.api.Path;
import cz.zipek.minicloud.api.events.SuccessEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Kamen
 */
public class Delete extends javax.swing.JFrame implements Listener<Event> {
	private final List<MetaItem> items = new ArrayList<>();
	
	private String filesActionId;
	private String pathsActionId;
	
	/**
	 * Creates new form Delete
	 * @param items
	 */
	public Delete(List<MetaItem> items) {
		initComponents();
		
		this.items.addAll(items);
		
		DefaultTableModel dm = ((DefaultTableModel)tableFiles.getModel());
		for(MetaItem file : this.items) {
			dm.addRow(new String[] {
				file.getName(),
				file.isFile() ? Tools.humanFileSize(file.getFile().getSize(), 2) : "",
				file.isFile() ? file.getFile().getPath() : file.getPath().getPath()
			});
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

        jScrollPane1 = new javax.swing.JScrollPane();
        tableFiles = new javax.swing.JTable();
        buttonDelete = new javax.swing.JButton();
        buttonCancel = new javax.swing.JButton();

        setTitle("Delete files");
        setMinimumSize(getPreferredSize());
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        tableFiles.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "File", "Size", "Folder"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tableFiles);

        buttonDelete.setText("Delete");
        buttonDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonDeleteActionPerformed(evt);
            }
        });

        buttonCancel.setText("Cancel");
        buttonCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonCancelActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 380, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(buttonDelete)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(buttonCancel)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 249, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(buttonDelete)
                    .addComponent(buttonCancel))
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void buttonDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonDeleteActionPerformed
		Manager.external.addListener(this);
		
		buttonDelete.setEnabled(false);
		
		List<File> files = new ArrayList<>();
		List<Path> paths = new ArrayList<>();
		for(MetaItem item : items) {
			if (item.isFile())
				files.add(item.getFile());
			else
				paths.add(item.getPath());
		}
		
		if (files.size() > 0)
			filesActionId = Manager.external.deleteFiles(files);
		if (paths.size() > 0)
			pathsActionId = Manager.external.deletePaths(paths);
    }//GEN-LAST:event_buttonDeleteActionPerformed

    private void buttonCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonCancelActionPerformed
		setVisible(false);
    }//GEN-LAST:event_buttonCancelActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
		Forms.remove(this);
    }//GEN-LAST:event_formWindowClosing

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton buttonCancel;
    private javax.swing.JButton buttonDelete;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tableFiles;
    // End of variables declaration//GEN-END:variables

	@Override
	public synchronized void handleEvent(Event e, Object sender) {
		if (e.getActionId() != null) {
			if (e.getActionId().equals(filesActionId) || e.getActionId().equals(pathsActionId)) {

				if (!(e instanceof SuccessEvent)) {
					JOptionPane.showMessageDialog(this, "Deletion operation failed.", "Error ocurred", JOptionPane.ERROR_MESSAGE);
				}
				
				if (e.getActionId().equals(filesActionId))
					filesActionId = null;
				if (e.getActionId().equals(pathsActionId))
					pathsActionId = null;

				if (filesActionId == null && pathsActionId == null) {
					Manager.external.removeListenerLater(this);

					this.setVisible(false);
					Forms.remove(this);
					Forms.getMain().refreshList();
				}
			}
		}
	}
}
