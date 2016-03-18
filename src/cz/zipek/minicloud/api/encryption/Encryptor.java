package cz.zipek.minicloud.api.encryption;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author Kamen
 */
public class Encryptor {
	
	private String config;
	
	private Cipher cipher;
	
	private SecretKeySpec key;

	public Encryptor(byte[] key) throws NoSuchAlgorithmException, NoSuchPaddingException {
		this(key, "AES/CBC/PKCS5Padding");
	}
	
	public Encryptor(byte[] rawKey, String aConfig) throws NoSuchAlgorithmException, NoSuchPaddingException {
		
		config = aConfig;
		cipher = Cipher.getInstance(config);
		key = new SecretKeySpec(rawKey, "AES");
		
	}
	
	public byte[] encrypt(byte[] input) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
		
		cipher.init(Cipher.ENCRYPT_MODE, key);
		return cipher.doFinal(input);

	}
	
	public byte[] decrypt(byte[] input) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
		
		cipher.init(Cipher.DECRYPT_MODE, key);
		return cipher.doFinal(input);
		
	}
	
}
