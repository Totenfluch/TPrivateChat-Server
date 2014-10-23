package me.Christian.networking;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;

public class Crypter {
	private static byte[] key1 = {

	};
	private static byte[] key2 = {

	};
	private static byte[] key3 = {

	};
	private static byte[] key4 = {

	};
	private static byte[] key5 = {

	};
	private static byte[] key6 = {

	};

	public static String hashit(String string){
		MessageDigest messageDigest;
		try {
			messageDigest = MessageDigest.getInstance("MD5");
			messageDigest.reset();
			messageDigest.update(string.getBytes(Charset.forName("UTF8")));
			final byte[] resultByte = messageDigest.digest();
			final String result = new String(Hex.encodeHex(resultByte));
			return result;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return "ERROR";
		}
	}

	public static String encrypt(String strToEncrypt, int ikey){
		byte[] key = null;
		if(ikey == 1){
			key = key1;
		}else if(ikey == 2){
			key = key2;
		}else if(ikey == 3){
			key = key3;
		}else if(ikey == 4){
			key = key4;
		}else if(ikey == 5){
			key = key5;
		}else if(ikey == 6){
			key = key6;
		}else{
			return "Invalid Key";
		}
		try{
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");  SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
			cipher.init(Cipher.ENCRYPT_MODE, secretKey);
			String encryptedString = Base64.encodeBase64String(cipher.doFinal(strToEncrypt.getBytes()));
			return encryptedString;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String decrypt(String strToDecrypt, int ikey){
		byte[] key = null;
		if(ikey == 1){
			key = key1;
		}else if(ikey == 2){
			key = key2;
		}else if(ikey == 3){
			key = key3;
		}else if(ikey == 4){
			key = key4;
		}else if(ikey == 5){
			key = key5;
		}else if(ikey == 6){
			key = key6;
		}else{
			return "Invalid Key";
		}
		try{
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
			SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
			cipher.init(Cipher.DECRYPT_MODE, secretKey);
			String decryptedString = new String(cipher.doFinal(Base64.decodeBase64(strToDecrypt)));
			return decryptedString;
		} catch (Exception e) {
			return "Your String is not Encrypted!";
		}
	}
}
