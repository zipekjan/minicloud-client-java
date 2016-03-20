package cz.zipek.minicloud.api.encryption;

import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.Arrays;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author Kamen
 */
public class Encryptor {
	
	private String config;
	
	private Cipher cipher;
	
	private SecretKeySpec key;

	private IvParameterSpec iv;
	
	public Encryptor(byte[] key) throws NoSuchAlgorithmException, NoSuchPaddingException, NoSuchProviderException {
		this(key, "AES/CBC/PKCS5Padding");
	}
	
	public Encryptor(byte[] rawKey, String aConfig) throws NoSuchAlgorithmException, NoSuchPaddingException, NoSuchProviderException {
		
		config = aConfig;
		cipher = Cipher.getInstance(config);
		
		byte[] ivParam = new byte[16];
		Arrays.fill( ivParam, (byte) 0 );
		
		key = new SecretKeySpec(rawKey, "AES");
		iv = new IvParameterSpec(ivParam);
		
	}
	
	public CipherOutputStream getOutputStream(OutputStream stream, int optmode) throws InvalidKeyException, InvalidAlgorithmParameterException {
		
		cipher.init(optmode, key, iv);
		return new CipherOutputStream(stream, cipher);
		
	}
	
	public CipherInputStream getInputStream(InputStream stream, int optmode) throws InvalidKeyException, InvalidAlgorithmParameterException {
		
		cipher.init(optmode, key, iv);
		return new CipherInputStream(stream, cipher);
		
	}
	
	public byte[] encrypt(byte[] input) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {
		
		cipher.init(Cipher.ENCRYPT_MODE, key, iv);
		return cipher.doFinal(input);

	}
	
	public byte[] decrypt(byte[] input) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {
		
		cipher.init(Cipher.DECRYPT_MODE, key, iv);
		return cipher.doFinal(input);
		
	}
	
	public String getConfig() {
		return config;
	}
	
}
