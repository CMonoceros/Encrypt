package ForClient;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import com.google.gson.Gson;

import MapKey.MenuMap;

@ServerEndpoint(value = "/web_service/{id}")
public class Web_Service {

	// 静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
	private static int onlineCount = 0;

	private static Map<Integer, Web_Service> webSocketMap = new HashMap<Integer, Web_Service>();

	// 与某个客户端的连接会话，需要通过它来给客户端发送数据
	private Session session;

	private int id;
	private String uploadFileProgress = "";

	/**
	 * 连接建立成功调用的方法
	 * 
	 * @param session
	 *            可选的参数。session为与某个客户端的连接会话，需要通过它来给客户端发送数据
	 */
	@OnOpen
	public void onOpen(Session session, @PathParam(value = "id") String id) {
		this.session = session;
		this.id = Integer.parseInt(id.trim());
		webSocketMap.put(this.id, this); // 加入set中
		System.out.println("有新连接加入! " + session.getRequestURI());
	}

	/**
	 * 连接关闭调用的方法
	 */
	@OnClose
	public void onClose() {
		webSocketMap.remove(id); // 从set中删除
		System.out.println("有一连接关闭! " + session.getRequestURI());
	}

	/**
	 * 收到客户端消息后调用的方法
	 * 
	 * @param message
	 *            客户端发送过来的消息
	 * @param session
	 *            可选的参数
	 * @throws IOException
	 */
	@OnMessage
	public void onMessage(String message, Session session) throws IOException {
		System.out.println("来自客户端的消息:" + message);
	}

	/**
	 * 发生错误时调用
	 * 
	 * @param session
	 * @param error
	 */
	@OnError
	public void onError(Session session, Throwable error) {
		System.out.println("发生错误! " + session.getRequestURI());
		error.printStackTrace();
	}

	/**
	 * 这个方法与上面几个方法不一样。没有用注解，是根据自己需要添加的方法。
	 * 
	 * @param message
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public void sendMessage(String message) throws IOException, InterruptedException {
		synchronized (this.session) {
			this.session.getBasicRemote().sendText(message);
		}
	}

	public static Web_Service get_service(int id) {
		Web_Service get_service = (Web_Service) webSocketMap.get(id);
		return get_service;
	}

}
