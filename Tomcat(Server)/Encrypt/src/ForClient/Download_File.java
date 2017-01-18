package ForClient;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.SQLException;

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
import Model.PathAndKey;
import Util.Get_File_Inf;

@WebServlet("/download_file")
public class Download_File extends HttpServlet {

	private String fileName;
	private int userId, fileId;
	private String fileRealName;
	private HttpServletRequest req;
	private HttpServletResponse resp;
	private String mainPath;
	private EncryptFile encryptFile;
	private PathAndKey pathAndKey = new PathAndKey();

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

		getFile();
	}

	private void getFile() throws IOException {
		req.setCharacterEncoding("utf-8");

		resp.setContentType("text/html;charset=UTF-8");
		resp.setCharacterEncoding("utf-8");

		dorequest();

		getFromServer();

	}

	private void dorequest() throws IOException {
		req.setCharacterEncoding("utf-8");
		String json = req.getParameter(MenuMap.DOWNLOAD_FILE_KEY);
		Gson gson = new Gson();
		encryptFile = gson.fromJson(json, new TypeToken<EncryptFile>() {
		}.getType());

		userId = encryptFile.getUserId();
		fileId = encryptFile.getFileId();

		try {
			updateDB();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String[] s = fileName.split("\\.");
		fileRealName = s[0];
		
		setupPath();
	}
	
	private void setupPath() {
		mainPath = PathAndKey.getPath();
		mainPath = mainPath + userId + "/";
		pathAndKey.setPath(mainPath);
	}

	private void updateDB() throws SQLException {
		DBFile dbFile = new DBFile();
		fileName = dbFile.selectFileNameDB(userId, fileId);
	}

	private void getFromServer() {
		// 1.获取要下载的文件的绝对路径
		String realPath = pathAndKey.getSendPath() + fileRealName + ".zip";
		// 2.获取要下载的文件名
		String fileName = realPath.substring(realPath.lastIndexOf("\\") + 1);
		// 3.设置content-disposition响应头控制浏览器以下载的形式打开文件
		resp.setHeader("content-disposition", "attachment;filename=" + fileName);
		resp.setContentType("application/zip");
		// 4.获取要下载的文件输入流
		InputStream in;
		try {
			in = new FileInputStream(realPath);
			int fileSize = (int) Get_File_Inf.getFileOrFilesSize(realPath, 1);
			resp.setContentLength(fileSize);
			int len = 0;
			// 5.创建数据缓冲区
			byte[] buffer = new byte[4096];
			// 6.通过response对象获取OutputStream流
			OutputStream out = resp.getOutputStream();
			long fileSizeDownloaded = 0;
			// 7.将FileInputStream流写入到buffer缓冲区
			while ((len = in.read(buffer)) > 0) {
				// 8.使用OutputStream将缓冲区的数据输出到客户端浏览器
				out.write(buffer, 0, len);
				fileSizeDownloaded += len;
				System.out.println(
						"Download Success " + fileRealName + " download: " + fileSizeDownloaded + " of " + fileSize);
			}

			in.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
