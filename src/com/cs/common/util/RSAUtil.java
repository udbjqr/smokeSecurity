package com.cs.common.util;//package com.com.cs.com.cs.common.util;
//
//import org.apache.commons.codec.binary.Hex;
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
//
//import javax.crypto.BadPaddingException;
//import javax.crypto.Cipher;
//import javax.crypto.IllegalBlockSizeException;
//import java.nio.charset.StandardCharsets;
//import java.nio.file.Files;
//import java.nio.file.Paths;
//import java.security.KeyFactory;
//import java.security.PrivateKey;
//import java.security.PublicKey;
//import java.security.spec.PKCS8EncodedKeySpec;
//import java.security.spec.X509EncodedKeySpec;
//import java.util.HashMap;
//
///**
// * 加密、解密使用的单元
// * <p>
// * 增加公钥/私钥 文件方法:
// * ssh-keygen -t rsa -b 2048 -C "注释"
// * 注意生成文件位置,当需要修改长度时，同时需要修改keyLength的值。
// * openssl pkcs8 -topk8 -inform PEM -outform DER -in id_rsa -out private_key.der -nocrypt
// * openssl rsa -in id_rsa -pubout -outform DER -out public_key.der
// *
// * @author yimin
// */
//public class RSAUtil {
//	public static final Logger log = LogManager.getLogger(RSAUtil.class.getName());
//	private static final String RSA = "RSA";
//	private static PrivateKey PRIVATE_KEY;
//	private static final HashMap<String, PublicKey> PUBLIC_KEYS = new HashMap<>();
//	@SuppressWarnings("FieldCanBeLocal")
//	private static int keyLength = 2048;
//
//	private static PublicKey readPublicKey(String name) {
//		if (PUBLIC_KEYS.containsKey(name)) {
//			return PUBLIC_KEYS.get(name);
//		} else {
//			try {
//				byte[] keyBytes = Files.readAllBytes(Paths.get(name));
//				X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
//				KeyFactory kf = KeyFactory.getInstance(RSA);
//				PublicKey key = kf.generatePublic(spec);
//
//				PUBLIC_KEYS.put(name, key);
//
//				return key;
//			} catch (Exception e) {
//				log.error("出现未知异常", e);
//				return null;
//			}
//		}
//	}
//
//	private static PrivateKey readPrivateKey(String name) {
//		try {
//			byte[] keyBytes = Files.readAllBytes(Paths.get(name));
//			PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
//			KeyFactory kf = KeyFactory.getInstance(RSA);
//			return kf.generatePrivate(spec);
//		} catch (Exception e) {
//			log.error("出现未知异常", e);
//			return null;
//		}
//	}
//
//
//	private static byte[] blockCipher(Cipher cipher, byte[] bytes, int mode) throws IllegalBlockSizeException, BadPaddingException {
//		// string initialize 2 buffers.
//		// scrambled will hold intermediate results
//		byte[] scrambled;
//		// toReturn will hold the total result
//		byte[] toReturn = new byte[0];
//		// 使用密钥长度进行计算相关长度。加密的减少11位。
//		int length = (mode == Cipher.ENCRYPT_MODE) ? (keyLength / 8) - 11 : (keyLength / 8);
//		// another buffer. this one will hold the bytes that have to be modified in this step
//		byte[] buffer = new byte[length];
//
//		for (int i = 0; i < bytes.length; i++) {
//			// if we filled our buffer array we have our block ready for de- or encryption
//			if ((i > 0) && (i % length == 0)) {
//				//execute the operation
//				scrambled = cipher.doFinal(buffer);
//				// add the result to our total result.
//				toReturn = append(toReturn, scrambled);
//				// here we calculate the length of the next buffer required
//				int newlength = length;
//
//				// if newlength would be longer than remaining bytes in the bytes array we shorten it.
//				if (i + length > bytes.length) {
//					newlength = bytes.length - i;
//				}
//				// clean the buffer array
//				buffer = new byte[newlength];
//			}
//			// copy byte into our buffer.
//			buffer[i % length] = bytes[i];
//		}
//
//		// this step is needed if we had a trailing buffer. should only happen when encrypting.
//		// example: we encrypt 110 bytes. 100 bytes per run means we "forgot" the last 10 bytes. they are in the buffer array
//		scrambled = cipher.doFinal(buffer);
//
//		// final step before we can return the modified data.
//		toReturn = append(toReturn, scrambled);
//
//		return toReturn;
//	}
//
//	private static byte[] append(byte[] toReturn, byte[] scrambled) {
//		byte[] destination = new byte[toReturn.length + scrambled.length];
//		System.arraycopy(toReturn, 0, destination, 0, toReturn.length);
//		System.arraycopy(scrambled, 0, destination, toReturn.length, scrambled.length);
//		return destination;
//	}
//
//	/**
//	 * @param encrypted
//	 * @param name
//	 * @return
//	 * @throws Exception 公钥加密
//	 */
//	public static String decryptWithPublicKey(String encrypted, String name) throws Exception {
//		PublicKey key = readPublicKey(name);
//
//		Cipher cipher = Cipher.getInstance(RSA);
//		cipher.init(Cipher.DECRYPT_MODE, key);
//
//		byte[] bts = Hex.decodeHex(encrypted.toCharArray());
//
//		byte[] decrypted = blockCipher(cipher, bts, Cipher.DECRYPT_MODE);
//
//		return new String(decrypted, StandardCharsets.UTF_8);
//	}
//
//	/**
//	 * @param plaintext
//	 * @param name
//	 * @return
//	 * @throws Exception 公钥解密
//	 */
//	public static String encryptWithPublicKey(String plaintext, String name) throws Exception {
//		PublicKey key = readPublicKey(name);
//
//		Cipher cipher = Cipher.getInstance(RSA);
//		cipher.init(Cipher.ENCRYPT_MODE, key);
//		byte[] bytes = plaintext.getBytes("UTF-8");
//
//		byte[] encrypted = blockCipher(cipher, bytes, Cipher.ENCRYPT_MODE);
//
//		char[] encryptedTranspherable = Hex.encodeHex(encrypted);
//		return new String(encryptedTranspherable);
//	}
//
//	/**
//	 * @param name
//	 * @param encrypted
//	 * @return
//	 * @throws Exception 私钥解密
//	 */
//	public static String decryptWithPrivateKey(String name, String encrypted) throws Exception {
//		if (PRIVATE_KEY == null) {
//			PRIVATE_KEY = readPrivateKey(name);
//		}
//
//		Cipher cipher = Cipher.getInstance(RSA);
//		cipher.init(Cipher.DECRYPT_MODE, PRIVATE_KEY);
//		byte[] bts = Hex.decodeHex(encrypted.toCharArray());
//
//		byte[] decrypted = blockCipher(cipher, bts, Cipher.DECRYPT_MODE);
//
//		return new String(decrypted, "UTF-8");
//	}
//
//	/**
//	 * @param name
//	 * @param plaintext
//	 * @return
//	 * @throws Exception 私钥加密
//	 */
//	public static String encryptWithPrivateKey(String name, String plaintext) throws Exception {
//		if (PRIVATE_KEY == null) {
//			PRIVATE_KEY = readPrivateKey(name);
//		}
//
//		Cipher cipher = Cipher.getInstance(RSA);
//		cipher.init(Cipher.ENCRYPT_MODE, PRIVATE_KEY);
//		byte[] bytes = plaintext.getBytes("UTF-8");
//
//		byte[] encrypted = blockCipher(cipher, bytes, Cipher.ENCRYPT_MODE);
//
//		char[] encryptedTranspherable = Hex.encodeHex(encrypted);
//		return new String(encryptedTranspherable);
//	}
//}
