package ForClient;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
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

import org.apache.commons.io.IOUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import Datebase.DBFile;
import Encrypt.Base.BaseEncrypt;
import MapKey.EncryptMap;
import MapKey.MenuMap;
import Model.EncryptFile;
import Model.ServerFile;

@WebServlet("/encrypt_file")
public class Encrypt_File extends HttpServlet {

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
			encryptFile();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			updateDB();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@SuppressWarnings("deprecation")
	private void doReq() throws IOException {
		req.setCharacterEncoding("utf-8");
		String json = req.getParameter(MenuMap.ENCRYPT_FILE_KEY);
		Gson gson = new Gson();
		encryptFile = gson.fromJson(json, new TypeToken<EncryptFile>() {
		}.getType());
	}

	private void encryptFile() throws Exception {
		switch (encryptFile.getTypeId()) {
		case EncryptMap.BASE:
			BaseEncrypt baseEncrypt = new BaseEncrypt(encryptFile);
			baseEncrypt.encryptFile();
			break;
		}
	}

	private void updateDB() throws SQLException {
		DBFile dbFile = new DBFile();
		dbFile.encryptFileDB(encryptFile.getUserId(), encryptFile.getFileId());
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
