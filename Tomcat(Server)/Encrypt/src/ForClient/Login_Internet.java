package ForClient;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
import Model.User;

@WebServlet("/login_internet")
public class Login_Internet extends HttpServlet {
	private String id;
	private String password;
	private int state;
	private User user;
	private String result;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO 自动生成的方法存根
		doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO 自动生成的方法存根
		getDate(req);

		dorequest(req);
		try {
			updateDB();
		} catch (NumberFormatException | SQLException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		dorespose(resp);
	}

	private void dorespose(HttpServletResponse resp) {
		resp.setContentType("text/html;charset=UTF-8");
		resp.setCharacterEncoding("utf-8");

		if (state == 1) {
			Gson gson = new Gson();
			result = gson.toJson(user);
		} else if (state == 2) {
			result = LoginMap.LOGIN_STATE_NON_EXISTENT;
		} else if (state == 3) {
			result = LoginMap.LOGIN_STATE_WRONG_PASSWORD;
		}
		try {
			PrintWriter out = resp.getWriter();
			out.print(result);
			out.flush();
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void updateDB() throws NumberFormatException, SQLException {
		DBLogin dbLogin = new DBLogin();
		state = dbLogin.loginDB(Integer.parseInt(id), password);
		if (state == 1) {
			user = dbLogin.loginUserDB(Integer.parseInt(id));
			user.setIsLogin(true);
		}
	}

	@SuppressWarnings("deprecation")
	private void dorequest(HttpServletRequest req) throws IOException {
		req.setCharacterEncoding("utf-8");
		String json = IOUtils.toString(req.getInputStream());
		Gson gson = new Gson();
		Map<String, String> login = gson.fromJson(json, new TypeToken<Map<String, String>>() {
		}.getType());

		id = login.get(LoginMap.LOGIN_ID_KEY);
		password = login.get(LoginMap.LOGIN_PASSWORD_KEY);

		System.out.println("id=" + id);
		System.out.println("password=" + password);
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
