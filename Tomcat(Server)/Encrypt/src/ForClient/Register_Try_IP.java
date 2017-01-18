package ForClient;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import MapKey.LoginMap;

@WebServlet("/register_try_connect")
public class Register_Try_IP extends HttpServlet {

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
		dorespose(resp);
	}

	private void dorespose(HttpServletResponse resp) {
		resp.setContentType("text/html;charset=UTF-8");
		resp.setCharacterEncoding("utf-8");
		try {
			PrintWriter out = resp.getWriter();
			out.print(LoginMap.REGISTER_TRY_IP_SUCCESS_VALUE);
			out.flush();
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void dorequest(HttpServletRequest req) {
		try {
			req.setCharacterEncoding("utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
