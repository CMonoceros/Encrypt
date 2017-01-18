package Datebase;

import java.sql.ResultSet;
import java.sql.SQLException;

import Model.User;

public class DBLogin {
	private DBCon dbCon;
	private String sql1, sql2;
	private ResultSet rs1 = null, rs2 = null;
	private int rt1;

	public DBLogin() {
		dbCon = new DBCon("encrypt");
		new DBInit();
	}

	/**
	 * 注册用户
	 * 
	 * @param username
	 *            用户名
	 * @param password
	 *            用户密码
	 * 
	 * @return 用户注册得到的id，注册失败返回-1
	 * 
	 * @throws SQLException
	 */
	public int registerDB(String username, String password) throws SQLException {
		sql1 = "INSERT INTO UserDate (USERNAME,PASSWORD,REGISTERTIME) VALUES('" + username + "','" + password
				+ "',NOW());";
		rt1 = dbCon.pst.executeUpdate(sql1);
		sql2 = "SELECT USERID FROM UserDate WHERE USERNAME='" + username + "';";
		rs2 = dbCon.pst.executeQuery(sql2);
		if (rs2.next()) {
			int id = rs2.getInt(1);
			DBInit dbInit = new DBInit();
			dbInit.createFileList(id);
			dbInit.createFileEncrypt(id);
			return id;
		} else {
			return -1;
		}
	}

	/**
	 * 登录检查
	 * 
	 * @param id
	 *            用户id
	 * @param password
	 *            用户密码
	 * 
	 * @return 成功返回1，密码错误返回3，用户不存在返回2
	 * 
	 * @throws SQLException
	 */
	public int loginDB(int id, String password) throws SQLException {
		sql1 = "SELECT * FROM UserDate WHERE USERID=" + id + " AND PASSWORD='" + password + "';";
		rs1 = dbCon.pst.executeQuery(sql1);

		if (rs1.next()) {
			return 1;
		} else {
			sql2 = "SELECT * FROM UserDate WHERE USERID=" + id + ";";
			rs2 = dbCon.pst.executeQuery(sql2);
			if (rs2.next()) {
				return 3;
			} else {
				return 2;
			}
		}
	}

	/**
	 * 查找用户信息
	 * 
	 * @param id
	 *            用户id
	 * 
	 * @return User对象
	 * 
	 * @throws SQLException
	 */
	public User loginUserDB(int id) throws SQLException {
		sql1 = "SELECT * FROM UserDate WHERE USERID=" + id + " ;";
		rs1 = dbCon.pst.executeQuery(sql1);
		if (rs1.next()) {
			User user = new User(rs1.getString(2), rs1.getString(3));
			user.setId(rs1.getInt(1));
			return user;
		} else {
			return null;
		}
	}

	/**
	 * 更改密码
	 * 
	 * @param id
	 *            用户id
	 * @param oldPassword
	 *            用户旧密码
	 * @param newPassword
	 *            用户新密码
	 * 
	 * @return 更改成功返回1，失败返回2
	 * 
	 * @throws SQLException
	 */
	public int changePasswordDB(int id, String oldPassword, String newPassword) throws SQLException {
		sql1 = "UPDATE UserDate SET PASSWORD='" + newPassword + "' WHERE USERID=" + id + " AND PASSWORD='" + oldPassword
				+ "';";
		rt1 = dbCon.pst.executeUpdate(sql1);
		if (rt1 != 0) {
			return 1;
		} else {
			return 2;
		}
	}

	/**
	 * 更改用户名
	 * 
	 * @param id
	 *            用户id
	 * @param name
	 *            新用户名
	 * 
	 * @throws SQLException
	 */
	public void changeUserNameDB(int id, String name) throws SQLException {
		sql1 = "UPDATE UserDate SET USERNAME='" + name + "' WHERE USERID=" + id + " ;";
		rt1 = dbCon.pst.executeUpdate(sql1);

	}

}
