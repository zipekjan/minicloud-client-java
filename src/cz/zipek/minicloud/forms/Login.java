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
import cz.zipek.minicloud.Session;
import cz.zipek.minicloud.Settings;
import cz.zipek.minicloud.api.Listener;
import cz.zipek.minicloud.api.events.ConnectionErrorEvent;
import cz.zipek.minicloud.api.events.ErrorEvent;
import cz.zipek.minicloud.api.events.ServerInfoEvent;
import cz.zipek.minicloud.api.events.UserEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.swing.JOptionPane;
import org.json.JSONException;

/**
 *
 * @author zipekjan
 */
public class Login extends javax.swing.JFrame implements Listener {
	/**
	 * Creates new form Login
	 */
	public Login() {
		initComponents();

		setIconImages(Icons.getLogo());
		
		textServer.setText(Settings.getServer());
		textServer.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				textLogin.requestFocus();
			}
		});
		
		textLogin.setText(Settings.getUsername());
		textLogin.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				textPassword.requestFocus();
			}
		});
		textPassword.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				loginButtonActionPerformed(null);
			}
		});
		
		if (textServer.getText().isEmpty()) {
			textServer.requestFocus();
		} else if (textLogin.getText().isEmpty()) {
			textLogin.requestFocus();
		} else {
			textPassword.requestFocus();
		}
		
		Manager.external.addListener(this);
		
		if (!Manager.checkKeyLength()) {
			JOptionPane.showMessageDialog(this,
				"You JRE doesn't support AES key length of 256 bits. This can be fixed by running install_key script.",
				"AES key length limited",
				JOptionPane.ERROR_MESSAGE
			);
			
			setVisible(false);
			dispose();
		}
		
	}

	public synchronized void setState(String state) {
		this.labelState.setText(state);
	}
	
	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        loginButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        textLogin = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        textPassword = new javax.swing.JPasswordField();
        jLabel3 = new javax.swing.JLabel();
        labelState = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        textServer = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Login - Minicloud Manager");
        setMinimumSize(getPreferredSize());
        setResizable(false);

        loginButton.setText("Login");
        loginButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loginButtonActionPerformed(evt);
            }
        });

        cancelButton.setText("Cancel");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        textLogin.setHorizontalAlignment(javax.swing.JTextField.LEFT);

        jLabel1.setText("Login:");

        jLabel2.setText("Password:");

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cz/zipek/minicloud/res/wide-small.png"))); // NOI18N
        jLabel3.setAlignmentX(0.5F);
        jLabel3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        labelState.setToolTipText("");

        jLabel4.setText("Server:");

        textServer.setHorizontalAlignment(javax.swing.JTextField.LEFT);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(labelState, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(loginButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cancelButton))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel1)
                            .addComponent(jLabel4))
                        .addGap(24, 24, 24)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(textServer)
                            .addComponent(textLogin)
                            .addComponent(textPassword, javax.swing.GroupLayout.DEFAULT_SIZE, 374, Short.MAX_VALUE))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(textServer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(textLogin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(textPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 24, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(labelState, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(loginButton, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
		this.setVisible(false);
		this.dispose();
    }//GEN-LAST:event_cancelButtonActionPerformed

    private void loginButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loginButtonActionPerformed
		setState("Logging in ...");
		
		loginButton.setEnabled(false);
		textLogin.setEnabled(false);
		textServer.setEnabled(false);
		
		Settings.setUsername(textLogin.getText());
		Settings.setServer(textServer.getText());
		
		try {
			Settings.save();
		} catch (JSONException | FileNotFoundException | UnsupportedEncodingException ex) {
			Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
		}
		
		Manager.external.setServer(textServer.getText());
		Manager.external.setAuth(textLogin.getText(), textPassword.getPassword());
		
		Manager.external.getUser();
    }//GEN-LAST:event_loginButtonActionPerformed

	@Override
	public void handleEvent(Object e, Object sender) {
		if (!isVisible()) {
			return;
		}

		if (e instanceof UserEvent) {
			Session.setUser(((UserEvent)e).getUser());
			try {
				Session.getUser().setPassword(textPassword.getPassword(), true);
			} catch (NoSuchProviderException | NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException | InvalidAlgorithmParameterException | UnsupportedEncodingException ex) {

				JOptionPane.showMessageDialog(
						this,
						"There was error when decrypting user key. Error: " + ex.getMessage(),
						"Unexpected error.",
						JOptionPane.ERROR_MESSAGE
				);

				Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
				
				loginButton.setEnabled(true);
				textLogin.setEnabled(true);
				textServer.setEnabled(true);
				
				setState("Invalid password");
				
				return;
			}

			Manager.external.getServerInfo();
		} else if (e instanceof ServerInfoEvent) {
			Session.setServer(((ServerInfoEvent)e).getServerInfo());

			setState("");
			setVisible(false);

			if (Session.getUser().getKey() == null) {
				Forms.showNewUser();
			} else {
				Forms.showMain();
			}

		} else if (e instanceof ConnectionErrorEvent) {
			setState("Server connection error");

			loginButton.setEnabled(true);
			textLogin.setEnabled(true);
			textServer.setEnabled(true);
			
		} else if (e instanceof ErrorEvent) {
			setState("Wrong login or password");

			loginButton.setEnabled(true);
			textLogin.setEnabled(true);
			textServer.setEnabled(true);
		}
	}
	
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancelButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel labelState;
    private javax.swing.JButton loginButton;
    private javax.swing.JTextField textLogin;
    private javax.swing.JPasswordField textPassword;
    private javax.swing.JTextField textServer;
    // End of variables declaration//GEN-END:variables
}
