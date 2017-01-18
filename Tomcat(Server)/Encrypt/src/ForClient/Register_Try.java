package ForClient;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import Datebase.DBLogin;
import MapKey.LoginMap;

@WebServlet("/register_try")
public class Register_Try extends HttpServlet {
	private int id;
	private String password;
	private String name;

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		getDate(req);
		dorequest(req);
		try {
			updateDB();
		} catch (SQLException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		dorespose(resp);
	}

	private void dorespose(HttpServletResponse resp) {
		resp.setContentType("text/html;charset=UTF-8");
		resp.setCharacterEncoding("utf-8");
		Map<String, String> register = new HashMap<String, String>();
		register.put(LoginMap.REGISTER_ID_KEY, id + "");
		register.put(LoginMap.REGISTER_STATE_KEY, LoginMap.REGISTER_STATE_SUCCESS_VALUE);
		Gson gson = new Gson();
		String json = gson.toJson(register);
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

	private void updateDB() throws SQLException {
		DBLogin dbLogin = new DBLogin();
		id = dbLogin.registerDB(name, password);
	}

	@SuppressWarnings("deprecation")
	private void dorequest(HttpServletRequest req) throws IOException {
		req.setCharacterEncoding("utf-8");
		String json = IOUtils.toString(req.getInputStream());
		Gson gson = new Gson();
		Map<String, String> register = gson.fromJson(json, new TypeToken<Map<String, String>>() {
		}.getType());

		password = register.get(LoginMap.REGISTER_PASSWORD_KEY);
		name = register.get(LoginMap.REGISTER_NAME_KEY);

		System.out.println("id=" + id);
		System.out.println("password=" + password);
		System.out.println("name=" + name);
	}

	private void getDate(HttpServletRequest req) throws UnknownHostException {
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
