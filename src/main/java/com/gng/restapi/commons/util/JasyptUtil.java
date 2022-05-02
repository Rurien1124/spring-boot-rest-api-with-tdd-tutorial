package com.gng.restapi.commons.util;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.iv.RandomIvGenerator;

import lombok.extern.slf4j.Slf4j;

/**
 * Jasypt encryption/decryption util
 * @author gchyoo
 *
 */
@Slf4j
public class JasyptUtil {
	private static StandardPBEStringEncryptor pbeEnc = new StandardPBEStringEncryptor();
	private String key = "";
	
	public JasyptUtil(String key) {
		this.key = key;
	}
	
	/**
	 * Jasypt encryption
	 * @param pwd
	 * @return
	 */
	public String encrypt(String pwd) {;
		try {
			pbeEnc.setAlgorithm("PBEWithMD5AndDES");
			pbeEnc.setIvGenerator(new RandomIvGenerator());
			pbeEnc.setPassword(key);
		} catch(Exception ex) {
			
		}
		String password = pbeEnc.encrypt(pwd);
		
		return password;
	}
	
	/**
	 * Jasypt decryption
	 * @param pwd
	 * @return
	 */
	public String decrypt(String pwd) {
		try {
			pbeEnc.setAlgorithm("PBEWithMD5AndDES");
			pbeEnc.setPassword(key);
		} catch(Exception ex) {
			
		}
		String password = pbeEnc.decrypt(pwd);

		return password;
	}
	
	/**
	 * 
	 * @param args
	 * Click right mouse button > Run As > Run Configurations > Arguments > Program Arguments > insert key value
	 */
	public static void main(String[] args) {
		String key = args[0].replaceAll(" ", "");
		
		JasyptUtil jasyptUtil = new JasyptUtil(key);
		
		String pwd = jasyptUtil.encrypt("value to encrypt");
		
		log.info("encrypted : [{}]", pwd);

		pwd = jasyptUtil.decrypt(pwd);
		
		log.info("decrypted : [{}]", pwd);
	}
}
