/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.zipek.minicloud.forms;

import cz.zipek.minicloud.Manager;
import cz.zipek.minicloud.Session;
import cz.zipek.minicloud.Settings;
import cz.zipek.minicloud.api.Event;
import cz.zipek.minicloud.api.Listener;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import org.json.JSONException;

/**
 *
 * @author Kamen
 */
public class SettingsFrame extends javax.swing.JFrame implements Listener<Event> {

	/**
	 * Creates new form Settings
	 */
	public SettingsFrame() {
		initComponents();
		
		setIconImage(new ImageIcon(this.getClass().getResource("/cz/zipek/minicloud/res/settings.png")).getImage());
		
		reloadData();
				
		Manager.external.addListenerLater(this);
	}	
	
	public final void reloadData() {
		textEmail.setText(Session.getUser().getEmail());
		textUsername.setText(Settings.getUsername());
		textSyncKey.setText(Manager.external.getAuth());
		
		comboEncryption.removeAllItems();
		
		for(String item : Settings.getEncryptionOptions()) {
			comboEncryption.addItem( item == null ? "Disabled" : item );
		}
		
		comboEncryption.setSelectedItem(Settings.getEncryption());
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        syncRadioGroup = new javax.swing.ButtonGroup();
        tabbedMain = new javax.swing.JTabbedPane();
        panelGeneral = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        textUsername = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        comboEncryption = new javax.swing.JComboBox();
        panelProfile = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        textEmail = new javax.swing.JTextField();
        textPassword = new javax.swing.JPasswordField();
        textPassword2 = new javax.swing.JPasswordField();
        jLabel6 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        textSyncKey = new javax.swing.JTextArea();
        buttonSave = new javax.swing.JButton();
        buttonCancel = new javax.swing.JButton();

        setTitle("Settings");
        setMinimumSize(getPreferredSize());

        jLabel1.setText("Default login:");

        jLabel2.setText("Encryption:");

        comboEncryption.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Disabled", "AES/CBC/PKCS5Padding" }));

        javax.swing.GroupLayout panelGeneralLayout = new javax.swing.GroupLayout(panelGeneral);
        panelGeneral.setLayout(panelGeneralLayout);
        panelGeneralLayout.setHorizontalGroup(
            panelGeneralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelGeneralLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelGeneralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelGeneralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(comboEncryption, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(textUsername, javax.swing.GroupLayout.DEFAULT_SIZE, 313, Short.MAX_VALUE))
                .addContainerGap())
        );
        panelGeneralLayout.setVerticalGroup(
            panelGeneralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelGeneralLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelGeneralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(textUsername, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(panelGeneralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(comboEncryption, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(141, Short.MAX_VALUE))
        );

        tabbedMain.addTab("General", panelGeneral);

        jLabel3.setText("E-mail:");

        jLabel4.setText("Password:");

        jLabel5.setText("Password again:");

        jLabel6.setText("Sync key:");

        textSyncKey.setEditable(false);
        textSyncKey.setColumns(20);
        textSyncKey.setRows(3);
        jScrollPane1.setViewportView(textSyncKey);

        javax.swing.GroupLayout panelProfileLayout = new javax.swing.GroupLayout(panelProfile);
        panelProfile.setLayout(panelProfileLayout);
        panelProfileLayout.setHorizontalGroup(
            panelProfileLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelProfileLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelProfileLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panelProfileLayout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(textEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 284, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panelProfileLayout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 47, Short.MAX_VALUE)
                        .addComponent(textPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 284, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelProfileLayout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(textPassword2, javax.swing.GroupLayout.PREFERRED_SIZE, 284, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelProfileLayout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        panelProfileLayout.setVerticalGroup(
            panelProfileLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelProfileLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelProfileLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(textEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelProfileLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(textPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelProfileLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(textPassword2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 32, Short.MAX_VALUE)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        tabbedMain.addTab("Profile", panelProfile);

        buttonSave.setText("Save");
        buttonSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonSaveActionPerformed(evt);
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
                    .addComponent(tabbedMain)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(buttonSave)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(buttonCancel)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tabbedMain)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(buttonSave)
                    .addComponent(buttonCancel))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void buttonCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonCancelActionPerformed
		setVisible(false);
    }//GEN-LAST:event_buttonCancelActionPerformed

    private void buttonSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonSaveActionPerformed
		String enc = comboEncryption.getSelectedItem().toString();
		if (enc.equals("Disabled"))
			enc = null;
		
		Settings.setEncryption(enc);
		
		if (textPassword.getPassword().length > 0) {
			if (!Arrays.equals(textPassword.getPassword(), textPassword2.getPassword())) {
				JOptionPane.showMessageDialog(
						this,
						"Passwords don't match.",
						"Problem with your input",
						JOptionPane.WARNING_MESSAGE
				);

				return;
			}

			try {
				Session.getUser().setPassword(textPassword.getPassword(), false);
			} catch (NoSuchProviderException | UnsupportedEncodingException | InvalidAlgorithmParameterException | NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException ex) {
				Logger.getLogger(SettingsFrame.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
		
		try {
			Settings.save();
		} catch (JSONException | FileNotFoundException | UnsupportedEncodingException ex) {
			Logger.getLogger(SettingsFrame.class.getName()).log(Level.SEVERE, null, ex);
			JOptionPane.showMessageDialog(this, "Failed to save settiongs. Make sure settings.json is writable.", "Failed to save settings", JOptionPane.ERROR_MESSAGE);
		}
		setVisible(false);
    }//GEN-LAST:event_buttonSaveActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton buttonCancel;
    private javax.swing.JButton buttonSave;
    private javax.swing.JComboBox comboEncryption;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel panelGeneral;
    private javax.swing.JPanel panelProfile;
    private javax.swing.ButtonGroup syncRadioGroup;
    private javax.swing.JTabbedPane tabbedMain;
    private javax.swing.JTextField textEmail;
    private javax.swing.JPasswordField textPassword;
    private javax.swing.JPasswordField textPassword2;
    private javax.swing.JTextArea textSyncKey;
    private javax.swing.JTextField textUsername;
    // End of variables declaration//GEN-END:variables

	@Override
	public void handleEvent(Event event, Object sender) {
		//@TODO: Add something
	}
}
