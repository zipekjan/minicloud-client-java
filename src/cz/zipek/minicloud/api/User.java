package cz.zipek.minicloud.api;

import cz.zipek.minicloud.Tools;
import cz.zipek.minicloud.api.encryption.Encryptor;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.NoSuchPaddingException;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Contains informations about logged user.
 * 
 * @author Kamen
 */
public class User {
	
	protected External source;
	
	protected int id;
	
	protected String name;
	protected String email;
	protected char[] password;
	
	protected byte[] key;
	
	protected boolean admin;
	
	public User(External api, JSONObject data, char[] password) throws JSONException {	
		this(api, data);
		
		this.password = password;
	}
	
	public User(External api, JSONObject data) throws JSONException {
		
		source = api;
		
		id = data.getInt("id");
		
		name = data.getString("name");
		email = data.optString("email", null);
		
		String keySource = data.optString("key", null);
		
		if (keySource != null && keySource.length() > 0) {
			key = Base64.getDecoder().decode(keySource);
		}
		
		admin = data.optBoolean("admin", false);
		
	}

	/**
	 * API used to fetch this info.
	 * 
	 * @return the source
	 */
	public External getSource() {
		return source;
	}

	/**
	 * Unique ID representing this user.
	 * This id is used to identify user on server.
	 * 
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * Sets unhashed user password.
	 * This change is only local. You need to call save method to apply changes to server.
	 * Unhashed user password needs to be saved in order to decrypt user key.
	 * 
	 * @param password unhashed password 
	 */
	public void setPassword(char[] password) {
		this.password = password;
	}
	
	/**
	 * Returns unhashed user password.
	 * Unhashed user password is used to encrypt/decrypt user key.
	 * 
	 * @return unhashed password
	 */
	public char[] getPassword() {
		return password;
	}
	
	/**
	 * User name used for login.
	 * 
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets user email.
	 * This change is only local. You need to call save method to apply changes to server.
	 * 
	 * @param email 
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	
	/**
	 * Returns user email.
	 * Email is currently unused.
	 * 
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * Sets user encryption key.
	 * Key must be unencrypted.
	 * 
	 * @param key 
	 */
	public void setKey(byte[] key) {
		this.key = key;
	}
	
	/**
	 * Returns unencrypted user key.
	 * 
	 * @return the key
	 */
	public byte[] getKey() {
		return key;
	}

	/**
	 * Returns if user is admin.
	 * 
	 * @return is user admin
	 */
	public boolean isAdmin() {
		return admin;
	}
	
	/**
	 * Returns encryptor with user key applied.
	 * This method is shorcut for creating correct encryptor for user.
	 * 
	 * @param options Cipher options
	 * @return encryptor with user key applied
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchPaddingException 
	 */
	public Encryptor getEncryptor(String options) throws NoSuchAlgorithmException, NoSuchPaddingException {
		if (options == null || options.length() == 0)
			return null;
		return new Encryptor(key, options);
	}
	
	/**
	 * Builds list of parameters used to update file.
	 * This list contains all changeable params of file.
	 * It's used when calling updateFile method of API.
	 * 
	 * @return updatable file params with current values
	 */
	public Map<String, String> getUpdate() {
		Map<String, String> items = new HashMap<>();
		
		if (getPassword() != null) {
			try {
				items.put("password", Tools.sha256(getPassword()));
			} catch (NoSuchAlgorithmException | UnsupportedEncodingException ex) {
				Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
		
		if (getKey() != null) {
			items.put("key", Base64.getEncoder().encodeToString(key));
		}
		
		items.put("email", getEmail());
		
		return items;
	}
	
	/**
	 * Applies local changes to server.
	 * This is asychronous call.
	 * You will need to listen on API for success event with same action id to confirm successfull save.
	 * 
	 * @return action id
	 */
	public String save() {
		return getSource().setUser(this);
	}
	
}
