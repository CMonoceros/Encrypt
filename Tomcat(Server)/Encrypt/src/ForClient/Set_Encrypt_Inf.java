package ForClient;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import Datebase.DBFile;
import MapKey.MenuMap;
import Model.EncryptFile;
import Model.ServerFile;

@WebServlet("/set_encrypt_inf")
public class Set_Encrypt_Inf extends HttpServlet {

	private HttpServletRequest req;
	private HttpServletResponse resp;
	private EncryptFile encryptFile;

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
		doReq();
		try {
			updateDB();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		doResp();
	}

	private void doReq() throws IOException {
		req.setCharacterEncoding("utf-8");
		String json = req.getParameter(MenuMap.SET_ENCRYPT_FILE_KEY);
		Gson gson = new Gson();
		encryptFile = gson.fromJson(json, new TypeToken<EncryptFile>() {
		}.getType());
		
		
	}

	private void doResp() {
		resp.setContentType("text/html;charset=UTF-8");
		resp.setCharacterEncoding("utf-8");
		try {
			PrintWriter out = resp.getWriter();
			out.print(MenuMap.SET_ENCRYPT_FILE_SUCCESS_KEY);
			out.flush();
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void updateDB() throws SQLException {
		DBFile dbFile = new DBFile();
		dbFile.updateEncryptFileInfDB(encryptFile.getUserId(), encryptFile.getFileId(), encryptFile.getTypeId(),
				encryptFile.getExInf());
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
