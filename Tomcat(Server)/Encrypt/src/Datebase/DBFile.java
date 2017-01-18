package Datebase;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import Model.EncryptType;
import Model.ServerFile;

public class DBFile {
	private DBCon dbCon;
	private String sql1, sql2, sql3;
	private ResultSet rs1 = null, rs2 = null;
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private int rt1, rt2, rt3;
	private String uploadTime = null, lastEncryptTime = null, lastDownloadTime = null;

	public DBFile() {
		dbCon = new DBCon("encrypt");
		new DBInit();
	}

	/**
	 * 上传文件
	 * 
	 * @param userId
	 *            用户id
	 * @param filename
	 *            文件名称
	 * @param size
	 *            文件大小
	 * @param path
	 *            文件路径
	 * 
	 * @return 上传后的ServerFile对象
	 * 
	 * @throws SQLException
	 */
	public ServerFile uploadFileDB(int userId, String filename, String size, String path) throws SQLException {
		sql1 = "INSERT INTO FileList" + userId + " (FILENAME,SIZE,PATH,UPLOADTIME) VALUES('" + filename + "','" + size
				+ "','" + path + "',NOW());";
		rt1 = dbCon.pst.executeUpdate(sql1);
		sql2 = "SELECT * FROM FileList" + userId + " WHERE PATH='" + path + "';";
		rs2 = dbCon.pst.executeQuery(sql2);
		if (rs2.next()) {
			uploadTime = sdf.format(rs2.getTimestamp(4));
			ServerFile serverFile = new ServerFile(rs2.getInt(1), rs2.getString(2), userId, uploadTime,
					rs2.getString(3), rs2.getString(7));
			return serverFile;
		} else {
			return null;
		}
	}

	/**
	 * 更新文件加密时间
	 * 
	 * @param userId
	 *            用户id
	 * @param fileId
	 *            文件id
	 * 
	 * @return 更新后的ServerFile对象
	 * 
	 * @throws SQLException
	 */
	public ServerFile encryptFileDB(int userId, int fileId) throws SQLException {
		sql1 = "UPDATE FileList" + userId + " SET LASTENCRYPTTIME=NOW() WHERE FILEID=" + fileId + ";";
		rt1 = dbCon.pst.executeUpdate(sql1);
		sql2 = "SELECT * FROM FileList" + userId + " WHERE FILEID=" + fileId + ";";
		rs2 = dbCon.pst.executeQuery(sql2);
		if (rs2.next()) {
			uploadTime = sdf.format(rs2.getTimestamp(4));
			lastEncryptTime = sdf.format(rs2.getTimestamp(6));
			ServerFile serverFile = new ServerFile(rs2.getInt(1), rs2.getString(2), userId, uploadTime,
					rs2.getString(3), rs2.getString(7));
			serverFile.setLastEncryptTime(lastEncryptTime);
			if (null != rs2.getTimestamp(5)) {
				lastDownloadTime = sdf.format(rs2.getTimestamp(5));
				serverFile.setLastDownloadTime(lastDownloadTime);
			}
			return serverFile;
		} else {
			return null;
		}
	}

	/**
	 * 更新下载时间
	 * 
	 * @param userId
	 *            用户id
	 * @param fileId
	 *            文件id
	 * 
	 * @return 更新后的ServerFile对象
	 * 
	 * @throws SQLException
	 */
	public ServerFile downloadFileDB(int userId, int fileId) throws SQLException {
		sql1 = "UPDATE FileList" + userId + " SET LASTDOWNLOADTIME=NOW() WHERE FILEID=" + fileId + ";";
		rt1 = dbCon.pst.executeUpdate(sql1);
		sql2 = "SELECT * FROM FileList" + userId + " WHERE FILEID=" + fileId + ";";
		rs2 = dbCon.pst.executeQuery(sql2);
		if (rs2.next()) {
			uploadTime = sdf.format(rs2.getTimestamp(4));
			lastDownloadTime = sdf.format(rs2.getTimestamp(5));
			ServerFile serverFile = new ServerFile(rs2.getInt(1), rs2.getString(2), userId, uploadTime,
					rs2.getString(3), rs2.getString(7));
			serverFile.setLastDownloadTime(lastDownloadTime);
			if (null != rs2.getTimestamp(6)) {
				lastEncryptTime = sdf.format(rs2.getTimestamp(6));
				serverFile.setLastEncryptTime(lastEncryptTime);
			}
			return serverFile;
		} else {
			return null;
		}
	}

	/**
	 * 获取用户文件列表
	 * 
	 * @param userId
	 *            用户id
	 * 
	 * @return ServerFile对象的List列表
	 * 
	 * @throws SQLException
	 */
	public List<ServerFile> getFileListDB(int userId) throws SQLException {
		List<ServerFile> list = new ArrayList<>();
		sql1 = "SELECT * FROM FileList" + userId + ";";
		rs1 = dbCon.pst.executeQuery(sql1);
		while (rs1.next()) {
			uploadTime = sdf.format(rs1.getTimestamp(4));
			ServerFile serverFile = new ServerFile(rs1.getInt(1), rs1.getString(2), userId, uploadTime,
					rs1.getString(3), rs1.getString(7));
			if (null != rs1.getTimestamp(5)) {
				lastDownloadTime = sdf.format(rs1.getTimestamp(5));
				serverFile.setLastDownloadTime(lastDownloadTime);
			}
			if (null != rs1.getTimestamp(6)) {
				lastEncryptTime = sdf.format(rs1.getTimestamp(6));
				serverFile.setLastEncryptTime(lastEncryptTime);
			}
			list.add(serverFile);
		}
		return list;
	}

	/**
	 * 更新文件额外信息
	 * 
	 * @param userId
	 *            用户id
	 * @param fileId
	 *            文件id
	 * @param typeId
	 *            加密类型id
	 * @param exInf
	 *            额外信息
	 * 
	 * @throws SQLException
	 */
	public void updateEncryptFileInfDB(int userId, int fileId, int typeId, String exInf) throws SQLException {
		sql1 = "SELECT ID FROM FileEncrypt" + userId + " WHERE TYPEID=" + typeId + " AND FILEID=" + fileId + " ;";
		rs1 = dbCon.pst.executeQuery(sql1);
		if (rs1.next()) {
			sql2 = "UPDATE FileEncrypt" + userId + " SET EXINF='" + exInf + "' WHERE TYPEID=" + typeId + " AND FILEID="
					+ fileId + " ;";
			rt2 = dbCon.pst.executeUpdate(sql2);
		} else {
			sql3 = "INSERT INTO FileEncrypt" + userId + " (TYPEID,FILEID,EXINF) VALUES(" + typeId + "," + fileId + ",'"
					+ exInf + "');";
			rt3 = dbCon.pst.executeUpdate(sql3);
		}

	}

	/**
	 * 获取文件加密方式列表
	 * 
	 * @return EncryptType对象的List列表
	 * 
	 * @throws SQLException
	 */
	public List<EncryptType> getEncryptTypeDB() throws SQLException {
		List<EncryptType> list = new ArrayList<>();
		sql1 = "SELECT * FROM FileType ;";
		rs1 = dbCon.pst.executeQuery(sql1);
		while (rs1.next()) {
			EncryptType encryptType = new EncryptType(rs1.getInt(1), rs1.getString(2), rs1.getString(3));
			list.add(encryptType);
		}
		return list;
	}

	/**
	 * 查找加密的额外信息
	 * 
	 * @param userId
	 *            用户id
	 * @param fileId
	 *            文件id
	 * @param typeId
	 *            加密类型id
	 * 
	 * @return 额外信息
	 * 
	 * @throws SQLException
	 */
	public String selectExInfDB(int userId, int fileId, int typeId) throws SQLException {
		sql1 = "SELECT EXINF FROM FileEncrypt" + userId + " WHERE FILEID=" + fileId + " AND TYPEID=" + typeId + " ;";
		rs1 = dbCon.pst.executeQuery(sql1);
		if (rs1.next()) {
			return rs1.getString(1);
		} else {
			return null;
		}

	}

	/**
	 * 查找文件名称
	 * 
	 * @param userId
	 *            用户id
	 * @param fileId
	 *            文件id
	 * 
	 * @return 文件名称
	 * 
	 * @throws SQLException
	 */
	public String selectFileNameDB(int userId, int fileId) throws SQLException {
		sql1 = "SELECT FILENAME FROM FileList" + userId + " WHERE FILEID=" + fileId + ";";
		rs1 = dbCon.pst.executeQuery(sql1);
		if (rs1.next()) {
			return rs1.getString(1);
		} else {
			return null;
		}

	}

}
