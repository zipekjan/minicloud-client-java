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
import cz.zipek.minicloud.Tools;
import cz.zipek.minicloud.api.FileVersion;
import cz.zipek.minicloud.api.Listener;
import cz.zipek.minicloud.api.download.Downloader;
import cz.zipek.minicloud.api.download.events.DownloadFailedEvent;
import cz.zipek.minicloud.api.download.events.DownloadStoppedEvent;
import cz.zipek.minicloud.api.download.events.DownloadFileDoneEvent;
import cz.zipek.minicloud.api.download.events.DownloadProgressEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

/**
 *
 * @author Jan Zípek
 */
public class Download extends javax.swing.JFrame implements Listener {
	private List<FileVersion> files = new ArrayList<>();
	private Map<Integer, Integer> rows = new HashMap<>();
	private Downloader downloader;
	
	/**
	 * Creates new form Download
	 * @param files
	 */
	public Download(List<FileVersion> files) {
		initComponents();
		
		this.files = files;
		
		textTarget.setText(System.getProperty("user.home"));
		progressTotal.setMaximum(this.files.size());
		
		DefaultTableModel dm = ((DefaultTableModel)tableFiles.getModel());
		int row = 0;
		for(FileVersion version : files) {
			dm.addRow(new String[] {
				version.getFile().getName(),
				Tools.humanFileSize(version.getFile().getSize(), 2),
				"Queued"
			});
			rows.put(version.getFile().getId(), row++);
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
        jLabel1 = new javax.swing.JLabel();
        textTarget = new javax.swing.JTextField();
        buttonTarget = new javax.swing.JButton();
        progressTotal = new javax.swing.JProgressBar();
        progressFile = new javax.swing.JProgressBar();
        buttonStart = new javax.swing.JButton();
        buttonStop = new javax.swing.JButton();
        buttonCancel = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Download files");
        setMinimumSize(getPreferredSize());

        tableFiles.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "File", "Size", "State"
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

        jLabel1.setText("Target:");

        buttonTarget.setText("...");
        buttonTarget.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonTargetActionPerformed(evt);
            }
        });

        buttonStart.setText("Start");
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
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 497, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(textTarget)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(buttonTarget))
                    .addComponent(progressTotal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(progressFile, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(buttonStart)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(buttonStop)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(buttonCancel)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(textTarget, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(buttonTarget))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 143, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(progressTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(progressFile, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(buttonStart)
                    .addComponent(buttonStop)
                    .addComponent(buttonCancel))
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void buttonTargetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonTargetActionPerformed
		JFileChooser chooser = new JFileChooser(); 
		chooser.setCurrentDirectory(new java.io.File("."));
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		chooser.setMultiSelectionEnabled(false);
		chooser.setAcceptAllFileFilterUsed(false);
		if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) { 
			this.textTarget.setText(chooser.getSelectedFile().getAbsolutePath());
		}
    }//GEN-LAST:event_buttonTargetActionPerformed

    private void buttonStartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonStartActionPerformed
		if (downloader == null) {
			java.io.File path = new java.io.File(textTarget.getText());
			if (path.exists() && path.isDirectory()) {
				downloader = new Downloader(Manager.external, Session.getUser());
				downloader.addListener(this);
				for(FileVersion f : files) {
					downloader.add(f);
				}
				downloader.start(path.getAbsolutePath());
				
				buttonStart.setEnabled(false);
				buttonStop.setEnabled(true);
				textTarget.setEnabled(false);
			} else {
				JOptionPane.showMessageDialog(this, "Target folder doesn't exists.", "Wrong Target folder", JOptionPane.ERROR_MESSAGE);
			}
		}
    }//GEN-LAST:event_buttonStartActionPerformed

    private void buttonCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonCancelActionPerformed
		if (downloader != null) {
			JOptionPane.showMessageDialog(this, "Download in progress, please stop download first.", "Can't close", JOptionPane.INFORMATION_MESSAGE);
		} else {
			setVisible(false);
		}
    }//GEN-LAST:event_buttonCancelActionPerformed

    private void buttonStopActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonStopActionPerformed
        if (downloader != null)
			downloader.stop();
    }//GEN-LAST:event_buttonStopActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton buttonCancel;
    private javax.swing.JButton buttonStart;
    private javax.swing.JButton buttonStop;
    private javax.swing.JButton buttonTarget;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JProgressBar progressFile;
    private javax.swing.JProgressBar progressTotal;
    private javax.swing.JTable tableFiles;
    private javax.swing.JTextField textTarget;
    // End of variables declaration//GEN-END:variables
	
	@Override
	public synchronized void handleEvent(Object event, Object sender) {
		if (event instanceof DownloadFailedEvent) {
			JOptionPane.showMessageDialog(this, "Unknown error occured during download", "Download failed", JOptionPane.ERROR_MESSAGE);
		} else if (event instanceof DownloadProgressEvent) {
			DownloadProgressEvent e = (DownloadProgressEvent)event;
			
			int row = rows.get(e.getFile().getId());
			TableModel dm = tableFiles.getModel();
			int done = (int)(e.getDownloaded() * 100.0 / e.getTotal() + 0.5);

			dm.setValueAt(Integer.toString(done) + " %", row, 2);
			progressFile.setValue(done);

		} else if (event instanceof DownloadFileDoneEvent) {
			DownloadFileDoneEvent e = (DownloadFileDoneEvent)event;
			
			int row = rows.get(e.getFile().getId());
			TableModel dm = tableFiles.getModel();
			progressTotal.setValue(row + 1);
			
			if (row + 1 == files.size()) {
				downloader = null;
				buttonStop.setEnabled(false);
				buttonCancel.setText("Close");
				JOptionPane.showMessageDialog(this, "All files downloaded", "Done", JOptionPane.INFORMATION_MESSAGE);
				setVisible(false);
			}
		} else if (event instanceof DownloadStoppedEvent) {
			downloader = null;
			buttonStop.setEnabled(false);
			buttonStart.setEnabled(true);
		}
	}
}
