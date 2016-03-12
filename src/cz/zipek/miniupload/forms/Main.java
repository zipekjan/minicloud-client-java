/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.zipek.miniupload.forms;

import cz.zipek.miniupload.Forms;
import cz.zipek.miniupload.Icons;
import cz.zipek.miniupload.Manager;
import cz.zipek.miniupload.Session;
import cz.zipek.miniupload.Settings;
import cz.zipek.miniupload.SettingsEvent;
import cz.zipek.miniupload.Tools;
import cz.zipek.miniupload.api.Event;
import cz.zipek.miniupload.api.File;
import cz.zipek.miniupload.api.Folder;
import cz.zipek.miniupload.api.Listener;
import cz.zipek.miniupload.api.events.FilesEvent;
import cz.zipek.miniupload.events.SyncFolderAddedEvent;
import cz.zipek.miniupload.events.SyncFolderModifiedEvent;
import cz.zipek.miniupload.events.SyncFolderRemovedEvent;
import cz.zipek.miniupload.sync.SyncFolder;
import java.awt.Component;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author zipekjan
 */
public class Main extends javax.swing.JFrame implements Listener<Event> {
	private Folder currentFolder;
	private Folder rootFolder;

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
		tableRemoteFiles.getColumnModel().getColumn(0).setCellRenderer(new ImageRenderer());

		Manager.external.addListenerLater(this);
		Manager.external.files(Session.getId());
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
		if (event instanceof FilesEvent) {
			handleSubEvent((FilesEvent) event);
		}
	}
	
	private void handleSubEvent(FilesEvent event) {
		String path = "";
		if (currentFolder != null) {
			path = currentFolder.getPath();
		}
		rootFolder = event.getRoot();
		setPath(path);
	}

	private void setFolder(Folder folder) {
		currentFolder = folder;

		//Get table model
		DefaultTableModel dm = ((DefaultTableModel) tableRemoteFiles.getModel());

		//Clear rows
		while (dm.getRowCount() > 0) {
			dm.removeRow(0);
		}

		//Add up folder
		if (currentFolder.getParent() != null) {
			dm.addRow(new Object[]{
				null,
				"..",
				"",
				"",
				""
			});
		}

		//Insert folders
		for (Folder f : currentFolder.getFolders()) {
			dm.addRow(new Object[]{
				Icons.getIcon(null),
				"[" + f.getName() + "]",
				Tools.humanFileSize(f.getSize(), 2),
				"",
				""
			});
		}

		//Insert files
		for (File f : currentFolder.getFiles()) {
			dm.addRow(new Object[]{
				Icons.getIcon(f.getExtension()),
				f.getName(),
				Tools.humanFileSize(f.getSize(), 2),
				(new SimpleDateFormat("dd.MM.yyyy HH:mm")).format(f.getDate().getTime()),
				Integer.toString(f.getDownloads())
			});
		}
	}

	public void setPath(String path) {
		setFolder(rootFolder.findPath(path));
	}
	
	private List<File> getSelectedFiles() {
		List<File> files = new ArrayList<>();

		int folders = currentFolder.getFolders().size();
		if (currentFolder.getParent() != null) {
			folders += 1;
		}

		int[] rows = tableRemoteFiles.getSelectedRows();
		for (int row : rows) {
			if (row >= folders) {
				files.add(currentFolder.getFiles().get(row - folders));
			} else {
				if (currentFolder.getParent() != null)
					row -= 1;
				files.addAll(
					currentFolder
					.getFolders()
					.get(row)
					.getAllFiles()
				);
			}
		}

		return files;
	}
	
	private List<SyncFolder> getSelectedSyncFolders() {
		List<SyncFolder> items = new ArrayList<>();

		int[] rows = tableSyncFolders.getSelectedRows();
		for (int row : rows) {
			items.add(Settings.getSyncFolders().get(row));
		}

		return items;
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
        tabSync = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tableSyncFolders = new javax.swing.JTable();
        buttonAdd = new javax.swing.JButton();
        buttonRemove = new javax.swing.JButton();
        buttonSyncSelected = new javax.swing.JButton();
        buttonSync = new javax.swing.JButton();
        buttonSettings = new javax.swing.JButton();
        buttonUpload = new javax.swing.JButton();
        buttonRefresh = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Miniupload Manager");

        tableRemoteFiles.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "", "Name", "Size", "Uploaded", "Downloaded"
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

        buttonDownload.setText("Download");
        buttonDownload.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonDownloadActionPerformed(evt);
            }
        });

        jLabel1.setText("Selected files:");

        buttonMove.setText("Move");
        buttonMove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonMoveActionPerformed(evt);
            }
        });

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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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

        javax.swing.GroupLayout tabRemoteLayout = new javax.swing.GroupLayout(tabRemote);
        tabRemote.setLayout(tabRemoteLayout);
        tabRemoteLayout.setHorizontalGroup(
            tabRemoteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, tabRemoteLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 654, Short.MAX_VALUE)
                .addContainerGap())
        );
        tabRemoteLayout.setVerticalGroup(
            tabRemoteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tabRemoteLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 260, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        tabsMain.addTab("Remote files", new javax.swing.ImageIcon(getClass().getResource("/cz/zipek/miniupload/res/remote.png")), tabRemote); // NOI18N

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
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 654, Short.MAX_VALUE)
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
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 263, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(tabSyncLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(buttonAdd)
                    .addComponent(buttonRemove)
                    .addComponent(buttonSyncSelected)
                    .addComponent(buttonSync))
                .addContainerGap())
        );

        tabsMain.addTab("Synchronized folders", new javax.swing.ImageIcon(getClass().getResource("/cz/zipek/miniupload/res/sync.png")), tabSync); // NOI18N

        buttonSettings.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cz/zipek/miniupload/res/settings.png"))); // NOI18N
        buttonSettings.setText("Settings");
        buttonSettings.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonSettingsActionPerformed(evt);
            }
        });

        buttonUpload.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cz/zipek/miniupload/res/upload.png"))); // NOI18N
        buttonUpload.setText("Upload files");
        buttonUpload.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonUploadActionPerformed(evt);
            }
        });

        buttonRefresh.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cz/zipek/miniupload/res/refresh.png"))); // NOI18N
        buttonRefresh.setText("Refresh");
        buttonRefresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonRefreshActionPerformed(evt);
            }
        });

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cz/zipek/miniupload/res/logo.png"))); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tabsMain)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(buttonRefresh)
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
                    .addComponent(buttonRefresh)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(tabsMain))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tableRemoteFilesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableRemoteFilesMouseClicked
		if (evt.getClickCount() == 2) {
			int row = tableRemoteFiles.getSelectedRow();
			int tfolders = this.currentFolder.getFolders().size();

			if (this.currentFolder.getParent() != null && row == 0) {
				this.setFolder(this.currentFolder.getParent());
				return;
			} else if (this.currentFolder.getParent() != null) {
				row -= 1;
			}

			if (row < tfolders) {
				this.setFolder(this.currentFolder.getFolders().get(row));
			} else {
				row -= tfolders;
				Forms.showFile(this.currentFolder.getFiles().get(row));
			}
		}
    }//GEN-LAST:event_tableRemoteFilesMouseClicked

    private void buttonDownloadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonDownloadActionPerformed
		List<File> files = getSelectedFiles();
		if (files.size() > 0) {
			Forms.showDownload(files);
		}
    }//GEN-LAST:event_buttonDownloadActionPerformed

    private void buttonDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonDeleteActionPerformed
        List<File> files = getSelectedFiles();
		if (files.size() > 0) {
			Forms.showDelete(files);
		}
    }//GEN-LAST:event_buttonDeleteActionPerformed

	public List<String> getFoldersPaths() {
		List<String> paths = new ArrayList<>();
		if (rootFolder != null) {
			for(Folder folder : rootFolder.getAllFolders()) {
				paths.add(folder.getPath());
			}
		} else {
			paths.add("/");
		}
		return paths;
	}
	
    private void buttonUploadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonUploadActionPerformed
		Forms.showUpload();
    }//GEN-LAST:event_buttonUploadActionPerformed

    private void buttonRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonRefreshActionPerformed
		Manager.external.files(Session.getId());
    }//GEN-LAST:event_buttonRefreshActionPerformed

    private void buttonMoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonMoveActionPerformed
        List<File> files = getSelectedFiles();
		if (files.size() > 0) {
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

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton buttonAdd;
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
    private javax.swing.JPanel tabRemote;
    private javax.swing.JPanel tabSync;
    private javax.swing.JTable tableRemoteFiles;
    private javax.swing.JTable tableSyncFolders;
    private javax.swing.JTabbedPane tabsMain;
    // End of variables declaration//GEN-END:variables
}
