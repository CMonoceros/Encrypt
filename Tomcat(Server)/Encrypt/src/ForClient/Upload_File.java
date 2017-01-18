package ForClient;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.google.gson.Gson;

import Datebase.DBFile;
import MapKey.MenuMap;
import Model.PathAndKey;
import Util.Get_File_Inf;

@SuppressWarnings("serial")
@WebServlet("/upload_file")
public class Upload_File extends HttpServlet {

	private String fileName;
	private int id;
	private String fileRealName;
	private HttpServletRequest req;
	private HttpServletResponse resp;
	private String mainPath;
	private PathAndKey pathAndKey = new PathAndKey();

	// PathAndKey.setPath("/root/Encrypt/Key/", "/root/Encrypt/");

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
		saveFile();
		try {
			updateDB();
		} catch (SQLException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}

	private void saveFile() throws UnsupportedEncodingException {
		req.setCharacterEncoding("utf-8");

		resp.setContentType("text/html;charset=UTF-8");
		resp.setCharacterEncoding("utf-8");

		id = Integer.parseInt(req.getHeader(MenuMap.UPLOAD_FILE_ID_KEY));

		confirmFileFolder();
		saveToServer();

	}

	private void updateDB() throws SQLException {
		DBFile dbFile = new DBFile();
		String filepath = pathAndKey.getFileSavePath() + fileName;

		dbFile.uploadFileDB(id, fileName, Get_File_Inf.getAutoFileOrFilesSize(filepath), pathAndKey.getFileSavePath());
	}

	private void confirmFileFolder() {
		mainPath = PathAndKey.getPath();
		mainPath = mainPath + id + "/";
		pathAndKey.setPath(mainPath);
	}

	private void saveToServer() {
		DiskFileItemFactory factory = new DiskFileItemFactory();
		// 得到绝对文件夹路径，比如"D:\\Tomcat6\\webapps\\test\\upload"
		String path = pathAndKey.getFileSavePath();
		// 临时文件夹路径
		String repositoryPath = pathAndKey.getFileTempPath();
		// 设定临时文件夹为repositoryPath
		factory.setRepository(new File(repositoryPath));
		// 设定上传文件的阈值，如果上传文件大于1M，就可能在repository
		// 所代 表的文件夹中产生临时文件，否则直接在内存中进行处理
		factory.setSizeThreshold(1024 * 1024);
		ServletFileUpload uploader = new ServletFileUpload(factory);
		try {
			List<?> fileItems = uploader.parseRequest(req);
			for (Iterator<?> iter = fileItems.iterator(); iter.hasNext();) {
				FileItem item = (FileItem) iter.next();
				if (item.isFormField()) {
					String name = item.getFieldName();
					String value = item.getString();
					System.out.println(name + " = " + value);
				}
				if (!item.isFormField()) {
					String fileName = item.getName();
					long size = item.getSize();
					// 判断是否选择了文件
					if ((fileName == null || fileName.equals("")) && size == 0) {
						continue;
					}
					fileName = fileName.substring(fileName.lastIndexOf("\\") + 1, fileName.length());
					fileName = fileName.substring(0, fileName.length() - 1);
					this.fileName = fileName;
					String[] s = fileName.split("\\.");
					fileRealName = s[0];
					InputStream in = item.getInputStream();
					long fileSize = item.getSize();
					int len = 0;
					byte[] buffer = new byte[4096];
					OutputStream out = new FileOutputStream(new File(path, fileName));
					long fileSizeUploaded = 0;
					while ((len = in.read(buffer)) > 0) {
						out.write(buffer, 0, len);
						fileSizeUploaded += len;
						System.out.println(
								"Upload Success " + fileRealName + " upload: " + fileSizeUploaded + " of " + fileSize);
					}
					in.close();
				}
			}
		} catch (Exception e) {
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
