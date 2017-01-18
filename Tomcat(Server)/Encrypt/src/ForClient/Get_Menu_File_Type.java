package ForClient;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import Datebase.DBFile;
import MapKey.MenuMap;
import Model.EncryptType;
import Model.ServerFile;

@WebServlet("/get_menu_file_type")
public class Get_Menu_File_Type extends HttpServlet {

	private HttpServletRequest req;
	private HttpServletResponse resp;
	private int id;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO 自动生成的方法存根
		doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO 自动生成的方法存根
		this.req = req;
		this.resp = resp;
		getDate();
		String json = null;
		try {
			json = updateDB();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		doResp(json);
	}

	private String updateDB() throws SQLException {
		DBFile dbFile = new DBFile();
		List<EncryptType> typeList = new ArrayList<EncryptType>();
		typeList = dbFile.getEncryptTypeDB();
		return typeToJson(typeList);
	}

	private String typeToJson(List<EncryptType> list) {
		Gson gson = new Gson();
		String json = gson.toJson(list);
		return json;
	}

	private void doResp(String json) {
		resp.setContentType("text/html;charset=UTF-8");
		resp.setCharacterEncoding("utf-8");
		try {
			PrintWriter out = resp.getWriter();
			out.print(json);
			out.flush();
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void getDate() throws UnknownHostException {
		System.out.println("Servlet=" + req.getServletPath());
		System.out.println("method=" + req.getMethod());

		InetAddress iA = null;
		iA = InetAddress.getLocalHost();

		String localName = iA.getHostName();
		String localIp = iA.getHostAddress();
		System.out.println("localName=" + localName);
		System.out.println("localIp=" + localIp);
	}
}
