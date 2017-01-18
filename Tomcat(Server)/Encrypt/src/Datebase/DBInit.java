package Datebase;

import java.sql.SQLException;

public class DBInit {

	private DBCon dbCon;
	private String sql;
	private int rt;

	public DBInit() {
		dbCon = new DBCon("encrypt");

		try {
			initCreateTable();
		} catch (SQLException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}

	private void initCreateTable() throws SQLException {
		createUserDate();
		createFileType();
	}

	/**
	 * 新建用户数据表
	 * 
	 * @throws SQLException
	 */
	private void createUserDate() throws SQLException {
		sql = "CREATE TABLE If Not Exists UserDate(USERID INTEGER NOT NULL auto_increment,"
				+ "USERNAME VARCHAR(16) NOT NULL," + "PASSWORD VARCHAR(16) NOT NULL,"
				+ "REGISTERTIME DATETIME NOT NULL ," + "PRIMARY KEY(USERID));";
		rt = dbCon.pst.executeUpdate(sql);
	}

	/**
	 * 新建加密类型表
	 * 
	 * @throws SQLException
	 */
	private void createFileType() throws SQLException {
		sql = "CREATE TABLE If Not Exists FileType(TYPEID INTEGER NOT NULL auto_increment,"
				+ "NAME VARCHAR(50) NOT NULL," + "INF VARCHAR(100) ," + "PRIMARY KEY(TYPEID));";
		rt = dbCon.pst.executeUpdate(sql);
	}

	/**
	 * 新建用户文件列表
	 * 
	 * @param id
	 *            用户id
	 * 
	 * @throws SQLException
	 */
	public void createFileList(int id) throws SQLException {
		sql = "CREATE TABLE If Not Exists FileList" + id + "(FILEID INTEGER NOT NULL auto_increment,"
				+ "FILENAME VARCHAR(100) NOT NULL," + "SIZE VARCHAR(10) NOT NULL," + "UPLOADTIME DATETIME NOT NULL ,"
				+ "LASTDOWNLOADTIME DATETIME," + "LASTENCRYPTTIME DATETIME ," + "PATH VARCHAR(100) NOT NULL ,"
				+ "PRIMARY KEY(FILEID));";
		rt = dbCon.pst.executeUpdate(sql);
	}

	/**
	 * 新建用户文件加密表
	 * 
	 * @param userId
	 *            用户id
	 * 
	 * @throws SQLException
	 */
	public void createFileEncrypt(int userId) throws SQLException {
		sql = "CREATE TABLE If Not Exists FileEncrypt" + userId + "(ID INTEGER NOT NULL auto_increment,"
				+ "FILEID INTEGER NOT NULL ," + "TYPEID INTEGER NOT NULL ," + "EXINF TEXT," + "PRIMARY KEY(ID),"
				+ "FOREIGN KEY(FILEID) REFERENCES FileList(FILEID),"
				+ "FOREIGN KEY(TYPEID) REFERENCES FileType(TYPEID));";
		rt = dbCon.pst.executeUpdate(sql);
	}

}
