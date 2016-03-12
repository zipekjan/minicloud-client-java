/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.zipek.miniupload;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Helper class with possibly usefull and widely used methods.
 * 
 * @author Kamen
 */
public class Tools {
	
	/**
	 * Returns human readable file size.
	 * 
	 * @param size size to stringify
	 * @param decimal_points round precision
	 * @return human readable file size
	 */
    public static String humanFileSize(long size, int decimal_points)
    {
        String[] levels = new String[]{ "", "k", "M", "G" };
        int level = 0;
        float s = size;
        while(s > 1024)
        {
            s /= 1024;
            level++;
        }
        return Math.round(s * Math.pow(10, decimal_points))/Math.pow(10, decimal_points) + " " + levels[level] + "iB";
    }
	
	/**
	 * Calculates md5 checksum of file.
	 * 
	 * @param file
	 * @return md5 checksum
	 * @throws NoSuchAlgorithmException
	 * @throws FileNotFoundException
	 * @throws IOException 
	 */
	public static String md5Checksum(File file) throws NoSuchAlgorithmException, FileNotFoundException, IOException {
		MessageDigest md = MessageDigest.getInstance("MD5");
		return getDigest(new FileInputStream(file.getAbsolutePath()), md, 2048);
	}

	/**
	 * Generates hashed string.
	 * 
	 * @param is input string
	 * @param md hash method
	 * @param byteArraySize
	 * @return hashed string
	 * @throws NoSuchAlgorithmException
	 * @throws IOException 
	 */
	private static String getDigest(InputStream is, MessageDigest md, int byteArraySize)
			throws NoSuchAlgorithmException, IOException {
		md.reset();
		byte[] bytes = new byte[byteArraySize];
		int numBytes;
		while ((numBytes = is.read(bytes)) != -1) {
			md.update(bytes, 0, numBytes);
		}
		byte[] hash = md.digest();
		StringBuilder result = new StringBuilder();
		for (int i = 0; i < hash.length; i++) {
			if ((0xff & hash[i]) < 0x10) {
				result.append("0").append(Integer.toHexString((0xFF & hash[i])));
			} else {
				result.append(Integer.toHexString(0xFF & hash[i]));
			}
		}
		return result.toString();
	}
}

