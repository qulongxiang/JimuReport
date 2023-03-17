package com.jeecg.modules.jmreport.config;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/** 
* @ClassName: AESUtil
* @Description: 加密解密算法
*/
public final class AESUtil {
	
	private static final int KEY_SIZE = 128;
	
	private static final int OXFF = 0xFF;
	
	private static final int RADIX_HEX = 16;
	
	/**
	 * 私有构造体.
	* <p>Title: </p>
	* <p>Description: </p>
	 */
	private AESUtil() {
	}
	
	/**
	 * 
	* @Title: encrypt
	* @Description: 加密
	* @param content 加密内容
	* @param password 密钥
	* @param @return
	* @return String
	* @throws
	 */
	public static String encrypt(String content, String password) {
		try {
			KeyGenerator kgen = KeyGenerator.getInstance("AES");
			kgen.init(KEY_SIZE, new SecureRandom(password.getBytes()));
			SecretKey secretKey = kgen.generateKey();
			byte[] enCodeFormat = secretKey.getEncoded();
			SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
			Cipher cipher = Cipher.getInstance("AES"); // 创建密码器
			byte[] byteContent = content.getBytes("utf-8");
			cipher.init(Cipher.ENCRYPT_MODE, key); // 初始化
			byte[] result = cipher.doFinal(byteContent);
			String encryptResultStr = parseByte2HexStr(result);
			return encryptResultStr; // 加密
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 
	* @Title: decrypt
	* @Description: 解密
	* @param content 密文
	* @param password 密钥
	* @param @return
	* @return String
	* @throws
	 */
	public static String decrypt(String content, String password) {
		try {
			KeyGenerator kgen = KeyGenerator.getInstance("AES");
			kgen.init(KEY_SIZE, new SecureRandom(password.getBytes()));
			SecretKey secretKey = kgen.generateKey();
			byte[] enCodeFormat = secretKey.getEncoded();
			SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
			Cipher cipher = Cipher.getInstance("AES"); // 创建密码器
			cipher.init(Cipher.DECRYPT_MODE, key); // 初始化
			byte[] decryptFrom = parseHexStr2Byte(content);
			byte[] result = cipher.doFinal(decryptFrom);
			return new String(result); // 加密
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 
	* @Title: parseByte2HexStr
	* @Description: 将二进制转换成16进制
	* @param buf - 二进制数组
	* @param @return
	* @return String
	* @throws
	 */
	public static String parseByte2HexStr(byte[] buf) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < buf.length; i++) {
			String hex = Integer.toHexString(buf[i] & OXFF);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			sb.append(hex.toUpperCase());
		}
		return sb.toString();
	}

	/**
	 * 
	* @Title: parseHexStr2Byte
	* @Description: 将16进制转换为二进制
	* @param hexStr - 16进制数
	* @param @return
	* @return byte[]
	* @throws
	 */
	public static byte[] parseHexStr2Byte(String hexStr) {
		if (hexStr.length() < 1) {
			return null;
		}
		byte[] result = new byte[hexStr.length() / 2];
		for (int i = 0; i < hexStr.length() / 2; i++) {
			int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), RADIX_HEX);
			int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), RADIX_HEX);
			result[i] = (byte) (high * RADIX_HEX + low);
		}
		return result;
	}

	/**
	 * 
	* @Title: main
	* @Description: 测试方法
	* @param args - 测试参数
	 */
	public static void main(String[] args) {
		String content = "admin_123456_PC";
		// 加密
		System.out.println("加密前：" + content);
		String encryptResultStr = encrypt(content, "elink");
		System.out.println("加密后：" + encryptResultStr);
		// 解密
		System.out.println("解密后：" + new String(decrypt(encryptResultStr, "elink")));
	}
}
