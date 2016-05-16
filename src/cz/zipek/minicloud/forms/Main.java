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
import cz.zipek.minicloud.Icons;
import cz.zipek.minicloud.Manager;
import cz.zipek.minicloud.MetaItem;
import cz.zipek.minicloud.Session;
import cz.zipek.minicloud.Settings;
import cz.zipek.minicloud.SettingsEvent;
import cz.zipek.minicloud.api.Event;
import cz.zipek.minicloud.api.File;
import cz.zipek.minicloud.api.FileVersion;
import cz.zipek.minicloud.api.Listener;
import cz.zipek.minicloud.api.Path;
import cz.zipek.minicloud.api.Tools;
import cz.zipek.minicloud.api.events.ConnectionErrorEvent;
import cz.zipek.minicloud.api.events.ErrorEvent;
import cz.zipek.minicloud.api.events.PathEvent;
import cz.zipek.minicloud.events.SyncFolderAddedEvent;
import cz.zipek.minicloud.events.SyncFolderModifiedEvent;
import cz.zipek.minicloud.events.SyncFolderRemovedEvent;
import cz.zipek.minicloud.sync.SyncFolder;
import java.awt.Component;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author zipekjan
 */
public class Main extends javax.swing.JFrame implements Listener<Event> {
	private Path currentPath;
	private Path rootPath;

	class ImageRenderer extends DefaultTableCellRenderer {

		JLabel lbl = new JLabel();

		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
			lbl.setIcon((ImageIcon) value);
			return lbl;
		}
	}

	/**
	 * Creates new form Main
	 */
	public Main() {
		initComponents();
		initCustom();
	}
	
	private void initCustom() {
		buttonAdmin.setVisible(Session.getUser().isAdmin());

		setIconImages(Icons.getLogo());
		
		tableRemoteFiles.getColumnModel().getColumn(0).setCellRenderer(new ImageRenderer());

		Manager.external.addListenerLater(this);
		Manager.external.getPath();
		
		Settings.addListener(new Listener<SettingsEvent>() {
			@Override
			public void handleEvent(SettingsEvent event, Object sender) {
				handleSettingsEvent(event);
			}
		});
		
		refreshSyncFolders();
	}

	public synchronized void handleSettingsEvent(SettingsEvent event) {
		if (event instanceof SyncFolderAddedEvent ||
			event instanceof SyncFolderModifiedEvent ||
			event instanceof SyncFolderRemovedEvent) {
			refreshSyncFolders();
		}
	}
	
	private synchronized void refreshSyncFolders() {
		//Get table model
		DefaultTableModel dm = ((DefaultTableModel) tableSyncFolders.getModel());

		//Clear rows
		while (dm.getRowCount() > 0) {
			dm.removeRow(0);
		}

		//Insert folders
		for (SyncFolder folder : Settings.getSyncFolders()) {
			String date = "";
			if (folder.getLastSync() != null) {
				date = new SimpleDateFormat("dd.MM.YYYY HH:mm")
						.format(folder.getLastSync());
			}
			dm.addRow(new String[]{
				folder.getRemote(),
				folder.getLocal().getAbsolutePath(),
				date
			});
		}
	}
	
	@Override
	public void handleEvent(Event event, Object sender) {
		if (event instanceof PathEvent) {
			handleSubEvent((PathEvent) event);
		}
		
		if (event instanceof ConnectionErrorEvent) {
			handleSubEvent((ConnectionErrorEvent)event);
		} else if (event instanceof ErrorEvent) {
			handleSubEvent((ErrorEvent)event);
		}
	}
	
	private void handleSubEvent(PathEvent event) {		
		setPath(event.getPath());
	}
	
	private void handleSubEvent(ErrorEvent event) {
		JOptionPane.showMessageDialog(
			this,
			"There was error in communication. Error: " + event.getMessage(),
			"Unexpected error.",
			JOptionPane.ERROR_MESSAGE
		);
	}
	
	private void handleSubEvent(ConnectionErrorEvent event) {
		JOptionPane.showMessageDialog(
			this,
			"There was connection error. Error: " + event.getException().getMessage(),
			"Unexpected error.",
			JOptionPane.ERROR_MESSAGE
		);
	}


	private void setPath(Path folder) {
		currentPath = folder;

		labelCurrentPath.setText(folder.getPath().length() > 0 ? "/" + folder.getPath() : "");
		
		//Get table model
		DefaultTableModel dm = ((DefaultTableModel) tableRemoteFiles.getModel());

		//Clear rows
		while (dm.getRowCount() > 0) {
			dm.removeRow(0);
		}

		//Add up folder
		if (currentPath.getParent() != -1) {
			dm.addRow(new Object[]{
				null,
				"..",
				"",
				"",
				""
			});
		}

		//Insert folders
		for (Path f : currentPath.getPaths()) {
			dm.addRow(new Object[]{
				Icons.getIcon(null),
				"[" + f.getName() + "]",
				"",
				(new SimpleDateFormat("dd.MM.yyyy HH:mm")).format(f.getMktime().getTime()),
				(new SimpleDateFormat("dd.MM.yyyy HH:mm")).format(f.getMdtime().getTime())
			});
		}

		//Insert files
		for (File f : currentPath.getFiles()) {
			dm.addRow(new Object[]{
				Icons.getIcon(f.getExtension()),
				f.getName(),
				Tools.humanFileSize(f.getSize(), 2),
				(new SimpleDateFormat("dd.MM.yyyy HH:mm")).format(f.getMktime().getTime()),
				(new SimpleDateFormat("dd.MM.yyyy HH:mm")).format(f.getMdtime().getTime())
			});
		}
	}

	public void loadPath(String path) {
		Manager.external.getPath(path);
	}
	
	public void loadPath(Path path) {
		loadPath(path.getId());
	}
	
	public void loadPath(int path_id) {
		Manager.external.getPath(path_id);
	}
	
	private List<MetaItem> getSelectedFiles() {
		List<MetaItem> selected = new ArrayList<>();

		int folders = currentPath.getPaths().size();
		if (currentPath.getParent() != -1) {
			folders += 1;
		}

		int[] rows = tableRemoteFiles.getSelectedRows();
		for (int row : rows) {
			if (row >= folders) {
				selected.add(new MetaItem(currentPath.getFiles().get(row - folders)));
			} else {
				if (currentPath.getParent() != -1)
					row -= 1;
				
				selected.add(new MetaItem(currentPath.getPaths().get(row)));
			}
		}

		return selected;
	}
	
	private List<SyncFolder> getSelectedSyncFolders() {
		List<SyncFolder> items = new ArrayList<>();

		int[] rows = tableSyncFolders.getSelectedRows();
		for (int row : rows) {
			items.add(Settings.getSyncFolders().get(row));
		}

		return items;
	}

	public void refreshList() {
		if (currentPath != null)
			Manager.external.getPath(currentPath.getId());
		else
			Manager.external.getPath();
	}
	
	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        tabsMain = new javax.swing.JTabbedPane();
        tabRemote = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableRemoteFiles = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        buttonDownload = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        buttonMove = new javax.swing.JButton();
        buttonDelete = new javax.swing.JButton();
        labelCurrentPath = new javax.swing.JLabel();
        buttonRefresh = new javax.swing.JButton();
        tabSync = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tableSyncFolders = new javax.swing.JTable();
        buttonAdd = new javax.swing.JButton();
        buttonRemove = new javax.swing.JButton();
        buttonSyncSelected = new javax.swing.JButton();
        buttonSync = new javax.swing.JButton();
        buttonSettings = new javax.swing.JButton();
        buttonUpload = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        buttonAdmin = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Minicloud Manager");
        setMinimumSize(getPreferredSize());

        tableRemoteFiles.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "", "Name", "Size", "Created", "Updated"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tableRemoteFiles.setFillsViewportHeight(true);
        tableRemoteFiles.setIntercellSpacing(new java.awt.Dimension(5, 5));
        tableRemoteFiles.setRowHeight(20);
        tableRemoteFiles.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableRemoteFilesMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tableRemoteFiles);
        if (tableRemoteFiles.getColumnModel().getColumnCount() > 0) {
            tableRemoteFiles.getColumnModel().getColumn(0).setMinWidth(20);
            tableRemoteFiles.getColumnModel().getColumn(0).setPreferredWidth(20);
            tableRemoteFiles.getColumnModel().getColumn(0).setMaxWidth(20);
        }
        tableRemoteFiles.getAccessibleContext().setAccessibleDescription("");

        buttonDownload.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cz/zipek/minicloud/res/download-small.png"))); // NOI18N
        buttonDownload.setText("Download");
        buttonDownload.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonDownloadActionPerformed(evt);
            }
        });

        jLabel1.setText("Selected files:");

        buttonMove.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cz/zipek/minicloud/res/move-small.png"))); // NOI18N
        buttonMove.setText("Move");
        buttonMove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonMoveActionPerformed(evt);
            }
        });

        buttonDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cz/zipek/minicloud/res/delete-small.png"))); // NOI18N
        buttonDelete.setText("Delete");
        buttonDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonDeleteActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 334, Short.MAX_VALUE)
                .addComponent(buttonDelete)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(buttonMove)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(buttonDownload)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(buttonDownload)
                    .addComponent(jLabel1)
                    .addComponent(buttonMove)
                    .addComponent(buttonDelete))
                .addContainerGap())
        );

        labelCurrentPath.setText("/current/path");

        buttonRefresh.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cz/zipek/minicloud/res/refresh-small.png"))); // NOI18N
        buttonRefresh.setText("Refresh");
        buttonRefresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonRefreshActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout tabRemoteLayout = new javax.swing.GroupLayout(tabRemote);
        tabRemote.setLayout(tabRemoteLayout);
        tabRemoteLayout.setHorizontalGroup(
            tabRemoteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(tabRemoteLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(tabRemoteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(tabRemoteLayout.createSequentialGroup()
                        .addComponent(labelCurrentPath, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(buttonRefresh)))
                .addContainerGap())
        );
        tabRemoteLayout.setVerticalGroup(
            tabRemoteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tabRemoteLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(tabRemoteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(buttonRefresh)
                    .addComponent(labelCurrentPath))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 247, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        tabsMain.addTab("Remote files", new javax.swing.ImageIcon(getClass().getResource("/cz/zipek/minicloud/res/remote.png")), tabRemote); // NOI18N

        tableSyncFolders.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Remote", "Local", "Last sync"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tableSyncFolders.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableSyncFoldersMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tableSyncFolders);

        buttonAdd.setText("Add new");
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

        buttonSyncSelected.setText("Synchronize selected");
        buttonSyncSelected.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonSyncSelectedActionPerformed(evt);
            }
        });

        buttonSync.setText("Synchronize all");
        buttonSync.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonSyncActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout tabSyncLayout = new javax.swing.GroupLayout(tabSync);
        tabSync.setLayout(tabSyncLayout);
        tabSyncLayout.setHorizontalGroup(
            tabSyncLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tabSyncLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(tabSyncLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 674, Short.MAX_VALUE)
                    .addGroup(tabSyncLayout.createSequentialGroup()
                        .addComponent(buttonAdd)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(buttonSync)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(buttonSyncSelected)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(buttonRemove)))
                .addContainerGap())
        );
        tabSyncLayout.setVerticalGroup(
            tabSyncLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tabSyncLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 281, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(tabSyncLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(buttonAdd)
                    .addComponent(buttonRemove)
                    .addComponent(buttonSyncSelected)
                    .addComponent(buttonSync))
                .addContainerGap())
        );

        tabsMain.addTab("Synchronized folders", new javax.swing.ImageIcon(getClass().getResource("/cz/zipek/minicloud/res/sync.png")), tabSync); // NOI18N

        buttonSettings.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cz/zipek/minicloud/res/settings.png"))); // NOI18N
        buttonSettings.setText("Settings");
        buttonSettings.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonSettingsActionPerformed(evt);
            }
        });

        buttonUpload.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cz/zipek/minicloud/res/upload.png"))); // NOI18N
        buttonUpload.setText("Upload files");
        buttonUpload.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonUploadActionPerformed(evt);
            }
        });

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cz/zipek/minicloud/res/wide-small.png"))); // NOI18N

        buttonAdmin.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cz/zipek/minicloud/res/admin.png"))); // NOI18N
        buttonAdmin.setText("Administration");
        buttonAdmin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonAdminActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tabsMain)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(buttonAdmin)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(buttonUpload)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(buttonSettings)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(buttonUpload)
                    .addComponent(buttonSettings)
                    .addComponent(jLabel2)
                    .addComponent(buttonAdmin, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tabsMain))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void tableRemoteFilesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableRemoteFilesMouseClicked
		if (evt.getClickCount() == 2) {
			int row = tableRemoteFiles.getSelectedRow();
			int tfolders = currentPath.getPaths().size();

			if (row == -1)
				return;
			
			if (currentPath.getParent() != -1 && row == 0) {
				this.loadPath(currentPath.getParent());
				return;
			} else if (currentPath.getParent() != -1) {
				row -= 1;
			}

			if (row < tfolders) {
				loadPath(currentPath.getPaths().get(row));
			} else {
				row -= tfolders;
				Forms.showFile(currentPath.getFiles().get(row));
			}
		}
    }//GEN-LAST:event_tableRemoteFilesMouseClicked

    private void buttonDownloadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonDownloadActionPerformed
		List<MetaItem> files = getSelectedFiles();
		List<FileVersion> versions = new ArrayList<>();
		if (!files.isEmpty()) {
			for(MetaItem item : files) {
				if (item.isFile())
					versions.add(item.getFile().getVersion());
			}
			
			Forms.showDownload(versions);
		}
    }//GEN-LAST:event_buttonDownloadActionPerformed

    private void buttonDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonDeleteActionPerformed
        List<MetaItem> files = getSelectedFiles();
		if (files.size() > 0) {
			Forms.showDelete(files);
		}
    }//GEN-LAST:event_buttonDeleteActionPerformed
	
    private void buttonUploadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonUploadActionPerformed
		Forms.showUpload();
    }//GEN-LAST:event_buttonUploadActionPerformed

    private void buttonRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonRefreshActionPerformed
		refreshList();
    }//GEN-LAST:event_buttonRefreshActionPerformed

    private void buttonMoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonMoveActionPerformed
        List<MetaItem> files = getSelectedFiles();
		if (!files.isEmpty()) {
			Forms.showMove(files);
		}
    }//GEN-LAST:event_buttonMoveActionPerformed

    private void buttonSettingsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonSettingsActionPerformed
		Forms.showSettings();
    }//GEN-LAST:event_buttonSettingsActionPerformed

    private void buttonAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonAddActionPerformed
		Forms.showSyncFolder(null);
    }//GEN-LAST:event_buttonAddActionPerformed

    private void tableSyncFoldersMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableSyncFoldersMouseClicked
        if (evt.getClickCount() == 2) {
			int row = tableSyncFolders.getSelectedRow();
			Forms.showSyncFolder(Settings.getSyncFolders().get(row));
		}
    }//GEN-LAST:event_tableSyncFoldersMouseClicked

    private void buttonSyncSelectedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonSyncSelectedActionPerformed
        List<SyncFolder> selected = getSelectedSyncFolders();
		if (!selected.isEmpty()) {
			Forms.showSync(selected);
		}
    }//GEN-LAST:event_buttonSyncSelectedActionPerformed

    private void buttonSyncActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonSyncActionPerformed
        if (!Settings.getSyncFolders().isEmpty()) {
			Forms.showSync(Settings.getSyncFolders());
		}
    }//GEN-LAST:event_buttonSyncActionPerformed

    private void buttonRemoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonRemoveActionPerformed
		List<SyncFolder> selected = getSelectedSyncFolders();
		if (!selected.isEmpty()) {
			for(SyncFolder folder : selected) {
				Settings.remove(folder);
			}
		}
    }//GEN-LAST:event_buttonRemoveActionPerformed

    private void buttonAdminActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonAdminActionPerformed
		Forms.showAdmin();
    }//GEN-LAST:event_buttonAdminActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton buttonAdd;
    private javax.swing.JButton buttonAdmin;
    private javax.swing.JButton buttonDelete;
    private javax.swing.JButton buttonDownload;
    private javax.swing.JButton buttonMove;
    private javax.swing.JButton buttonRefresh;
    private javax.swing.JButton buttonRemove;
    private javax.swing.JButton buttonSettings;
    private javax.swing.JButton buttonSync;
    private javax.swing.JButton buttonSyncSelected;
    private javax.swing.JButton buttonUpload;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel labelCurrentPath;
    private javax.swing.JPanel tabRemote;
    private javax.swing.JPanel tabSync;
    private javax.swing.JTable tableRemoteFiles;
    private javax.swing.JTable tableSyncFolders;
    private javax.swing.JTabbedPane tabsMain;
    // End of variables declaration//GEN-END:variables
}
