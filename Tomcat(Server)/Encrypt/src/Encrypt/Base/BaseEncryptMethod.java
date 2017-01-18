package Encrypt.Base;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apaches.commons.codec.binary.Base64;

import Encrypt.Base.DES.DesUtil;
import Encrypt.Base.Md5.Md5Util;
import Encrypt.Base.RSA.RSASignature;
import Encrypt.Base.RSA.RSAUtil;
import Util.Transform;

/**
 * 加密实现类
 * 
 * @author ZJM
 *
 */
public class BaseEncryptMethod {
	/**
	 * 密钥存储路径
	 */
	private String keyPath;
	/**
	 * 文件存储路径
	 */
	private String filePath;
	/**
	 * 文件名称
	 */
	private String fileName;
	/**
	 * 加密后存储路径
	 */
	private String encryptPath;
	/**
	 * 加密后名称
	 */
	private String encryptName;
	/**
	 * DES密钥
	 */
	private String desKey;
	/**
	 * MD5签名摘要
	 */
	private String hashSign;

	public BaseEncryptMethod(String desKey, String keyPath, String filePath, String fileName, String encryptPath,
			String encryptName) {
		this.desKey = desKey;
		this.keyPath = keyPath;
		this.filePath = filePath;
		this.fileName = fileName;
		this.encryptPath = encryptPath;
		this.encryptName = encryptName;
	}

	/**
	 * 获取MD5文件摘要
	 */
	public void setHashSign() {
		System.out.println("---------------Md5获取文件摘要------------------");
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SS");// 设置日期格式
		System.out.println(df.format(new Date()));
		hashSign = Md5Util.getMd5ByFile(new File(filePath + fileName));
		System.out.println(hashSign);
		System.out.println("---------------Md5获取摘要成功------------------");
		System.out.println(df.format(new Date()));
	}

	/**
	 * RSA私钥签名
	 * 
	 * @return 签名
	 * @throws Exception
	 */
	public String privateKeySign() throws Exception {
		System.out.println("---------------RSA私钥签名过程------------------");
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SS");// 设置日期格式
		System.out.println(df.format(new Date()));
		String signstr = RSASignature.sign(hashSign, RSAUtil.loadPrivateKeyByFile(keyPath));
		System.out.println("签名原串：" + hashSign);
		System.out.println("签名串：" + signstr);
		System.out.println(df.format(new Date()));
		System.out.println();

		return signstr;
	}

	/**
	 * DES加密
	 * 
	 * @throws Exception
	 */
	public void desEncrypt() throws Exception {
		System.out.println("---------------DES加密文件------------------");
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SS");// 设置日期格式
		System.out.println(df.format(new Date()));
		byte[] encrypt = DesUtil.encrypt(Transform.File2byte(filePath + fileName), desKey.getBytes());
		Transform.byte2File(encrypt, encryptPath, encryptName);
		System.out.println("---------------DES加密成功------------------");
		System.out.println(df.format(new Date()));
	}

	/**
	 * RSA私钥加密DES密钥
	 * 
	 * @return 加密后DES密钥
	 * @throws Exception
	 */
	public String privateKeyDesEncrypt() throws Exception {
		System.out.println("--------------RSA私钥加密过程-------------------");
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SS");// 设置日期格式
		System.out.println(df.format(new Date()));
		// 私钥加密过程
		byte[] cipherData = RSAUtil.encrypt(RSAUtil.loadPrivateKeyByStr(RSAUtil.loadPrivateKeyByFile(keyPath)),
				desKey.getBytes());
		String cipher = Base64.encodeBase64String(cipherData);

		System.out.println("原文：" + desKey);
		System.out.println("加密：" + cipher);
		System.out.println(df.format(new Date()));
		System.out.println();

		return cipher;
	}

	/**
	 * RSA公钥加密DES密钥
	 * 
	 * @return 加密后DES密钥
	 * @throws Exception
	 */
	public String publicKeyDesEncrypt() throws Exception {
		System.out.println("--------------RSA公钥加密过程-------------------");
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SS");// 设置日期格式
		System.out.println(df.format(new Date()));

		// 公钥加密过程
		byte[] cipherData = RSAUtil.encrypt(RSAUtil.loadPublicKeyByStr(RSAUtil.loadPublicKeyByFile(keyPath)),
				desKey.getBytes());
		String cipher = Base64.encodeBase64String(cipherData);

		System.out.println("原文：" + desKey);
		System.out.println("加密：" + cipher);
		System.out.println(df.format(new Date()));
		System.out.println();

		return cipher;
	}

	/**
	 * 存储加密后DES密钥 RSA公钥 签名
	 * 
	 * @param sign
	 *            签名
	 * @param desEncrypt
	 *            加密后DES密钥
	 * @throws Exception
	  */
	public void saveKeystoreAndSign(String sign ,String desEncrypt) throws Exception {
		FileWriter signFile = new FileWriter(encryptPath + "sign.sign");
		BufferedWriter signBW = new BufferedWriter(signFile);
		signBW.write(sign);
		signBW.flush();
		signBW.close();
		signFile.close();
		FileWriter desKeyFile = new FileWriter(encryptPath + "desKey.keystore");
		BufferedWriter desKeyBW = new BufferedWriter(desKeyFile);
		desKeyBW.write(desEncrypt);
		desKeyBW.flush();
		desKeyBW.close();
		desKeyFile.close();
		FileWriter publicKeyFile = new FileWriter(encryptPath + "publicKey.keystore");
		BufferedWriter publicKeyBW = new BufferedWriter(publicKeyFile);
		publicKeyBW.write(RSAUtil.loadPublicKeyByFile(keyPath));
		publicKeyBW.flush();
		publicKeyBW.close();
		publicKeyFile.close();
	}
}
