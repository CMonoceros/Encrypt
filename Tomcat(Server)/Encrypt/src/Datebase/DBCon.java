package Datebase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBCon {
	// 数据库jdbc值
	public static final String url = "jdbc:mysql://115.159.73.148:3306/";
	public static final String name = "com.mysql.jdbc.Driver";
	// user为用户名称，password为用户密码
	public static final String password = "?user=root&password=root&useUnicode=true&characterEncoding=UTF8&useSSL=true";

	public Connection conn = null;
	public java.sql.Statement pst = null;

	public DBCon(String db) {
		try {
			Class.forName(name);// 指定连接类型
			conn = DriverManager.getConnection(url + db + password);// 获取连接
			pst = conn.createStatement();// 准备执行语句
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void close() {
		try {
			this.conn.close();
			this.pst.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
