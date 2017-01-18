package Model;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import Encrypt.Base.RSA.RSAUtil;

public class PathAndKey {

	private static String Path = "C:/Code/JAVA/test/";

	// RSA密钥保存路径
	private String keyPath;

	// 文件原目录及名称
	private String filePath;

	private String fileSavePath;

	// 文件加密后储存目录及名称
	private String encryptPath;

	// 文件解密后储存目录及名称
	private String decryptPath;

	// 文件压缩后储存目录及名称
	private  String sendPath;

	// 文件解压后储存目录
	private  String getPath;

	private  String fileTempPath;

	public void setPath(String filePath) {
		this.filePath = filePath;
		confirmFile(filePath);
		this.keyPath = filePath + "Key/";
		confirmFile(keyPath);
		this.fileTempPath = filePath + "Temp/";
		confirmFile(fileTempPath);
		this.fileSavePath = filePath + "Save/";
		confirmFile(fileSavePath);
		this.encryptPath = filePath + "Encrypt/";
		confirmFile(encryptPath);
		this.decryptPath = filePath + "Decrypt/";
		confirmFile(decryptPath);
		this.sendPath = filePath + "Send/";
		confirmFile(sendPath);
		this.getPath = filePath + "Get/";
		confirmFile(getPath);

		// 生成RSA密钥
		RSAUtil.genKeyPair(keyPath);
	}

	public static String getPath() {
		return Path;
	}

	private static void confirmFile(String path) {
		File file = new File(path);
		if (!file.exists()) {
			file.mkdirs();
		}
	}

	public String getKeyPath() {
		return keyPath;
	}

	public  String getEncryptPath() {
		return encryptPath;
	}

	public  String getSendPath() {
		return sendPath;
	}

	public  String getFilePath() {
		return filePath;
	}

	public  String getFileSavePath() {
		return fileSavePath;
	}

	public  String getFileTempPath() {
		return fileTempPath;
	}

}
