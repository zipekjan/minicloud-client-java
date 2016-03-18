package cz.zipek.minicloud.api;

import cz.zipek.minicloud.api.encryption.Encryptor;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
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
	
	protected byte[] key;
	
	protected boolean admin;
	
	public User(External api, JSONObject data) throws JSONException {
		
		source = api;
		
		id = data.getInt("id");
		
		name = data.getString("name");
		email = data.getString("email");
		
		String keySource = data.optString("key", null);
		
		if (keySource != null && keySource.length() > 0) {
			key = Base64.getDecoder().decode(keySource);
		}
		
		admin = data.optBoolean("admin", false);
		
	}

	/**
	 * @return the source
	 */
	public External getSource() {
		return source;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @return the key
	 */
	public byte[] getKey() {
		return key;
	}

	/**
	 * @return the admin
	 */
	public boolean isAdmin() {
		return admin;
	}
	
	public Encryptor getEncryptor(String options) throws NoSuchAlgorithmException, NoSuchPaddingException {
		if (options == null || options.length() == 0)
			return null;
		return new Encryptor(key, options);
	}
	
}
