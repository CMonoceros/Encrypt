package Encrypt.Base;

import Datebase.DBFile;
import ForClient.Web_Service;
import Model.EncryptFile;
import Model.PathAndKey;
import Util.ZipUtil;

public class BaseEncrypt {

	private BaseModel baseModel;
	private int userId, fileId, typeId;
	private Web_Service web_service;
	private String exInfJson;
	private String mainPath;
	private String fileName;
	private PathAndKey pathAndKey = new PathAndKey();

	public BaseEncrypt(EncryptFile encryptFile) {
		userId = encryptFile.getUserId();
		fileId = encryptFile.getFileId();
		typeId = encryptFile.getTypeId();
		web_service = Web_Service.get_service(userId);
		setupPath(encryptFile.getUserId());
		try {
			selectDB();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void setupPath(int id) {
		mainPath = PathAndKey.getPath();
		mainPath = mainPath + id + "/";
		pathAndKey.setPath(mainPath);
	}

	private void selectDB() throws Exception {
		DBFile dbFile = new DBFile();
		exInfJson = dbFile.selectExInfDB(userId, fileId, typeId);
		jsonToObject();

		fileName = dbFile.selectFileNameDB(userId, fileId);
	}

	private void jsonToObject() {

		baseModel = new BaseModel(exInfJson);
	}

	public void encryptFile() throws Exception {
		String[] s = fileName.split("\\.");
		String fileRealName = s[0];

		BaseEncryptMethod encrypt = new BaseEncryptMethod(baseModel.getDesKey(), pathAndKey.getKeyPath(),
				pathAndKey.getFileSavePath(), fileName, pathAndKey.getEncryptPath(), fileRealName + ".encrypt");
		web_service.sendMessage(Base.PROGRESS_CODE_1_INF);
		encrypt.setHashSign();
		web_service.sendMessage(Base.PROGRESS_CODE_2_INF);
		String sign = encrypt.privateKeySign();
		web_service.sendMessage(Base.PROGRESS_CODE_3_INF);
		String desEncrypt = encrypt.privateKeyDesEncrypt();
		web_service.sendMessage(Base.PROGRESS_CODE_4_INF);
		encrypt.desEncrypt();
		web_service.sendMessage(Base.PROGRESS_CODE_5_INF);
		encrypt.saveKeystoreAndSign(sign, desEncrypt);
		web_service.sendMessage(Base.PROGRESS_CODE_6_INF);
		ZipUtil.ZipEncrypt(pathAndKey.getEncryptPath(), pathAndKey.getSendPath(), fileRealName + ".zip");
		web_service.sendMessage(Base.PROGRESS_CODE_7_INF);

	}
}
