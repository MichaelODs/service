
//服务器端的运行类，服务器的核心

package main;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Date;

import connect.AConnection;
import connect.AConnectionManager;
import message.Db;
import server.AddfriendListener;
import server.AddgroupListener;
import server.ChatP2PListener;
import server.ChatRoomListener;
import server.LogOutListener;
import server.LoginMsgListener;
import server.NewGroupListener;
import server.OnLineListener;
import server.SignListener;
import server.ZhuceListener;

public class Service {

	public static Connection c = null;
	public static Statement stmt = null;
	public static Connection d = null;
	public static Statement stmt2 = null;
	public static Connection e = null;
	public static Statement stmt3 = null;

	public static void main(String[] args) {
		try {
			//创建一个线程 等其他客户端的连接
			final ServerSocket server = new ServerSocket(8088);
			System.out.println("---服务器启动---" + new Date().toString());

			//初始化数据库
			Class.forName("org.sqlite.JDBC");
			//用户数据库
			c = DriverManager.getConnection("jdbc:sqlite:src/test.db");
			c.setAutoCommit(false);
			stmt = c.createStatement();
			//群组数据库
			d = DriverManager.getConnection("jdbc:sqlite:src/group.db");
			d.setAutoCommit(false);
			stmt2 = d.createStatement();
			//活动数据库
			e = DriverManager.getConnection("jdbc:sqlite:src/plan.db");
			e.setAutoCommit(false);
			stmt3 = e.createStatement();
			new Db();
			new AConnectionManager();

			//如果客户端连接成功分配置一个线程，加入监听器
			new Thread()
			{
				public void run()
				{
					while (true) {
						AConnection conn = null;
						try {
							Socket client = server.accept();
							System.out.println("---有客户端接入---" + client);
							conn = new AConnection(client);
							conn.addOnRecevieMsgListener(new LoginMsgListener(conn));
							conn.addOnRecevieMsgListener(new ChatP2PListener());
							conn.addOnRecevieMsgListener(new SignListener());
							conn.addOnRecevieMsgListener(new ChatRoomListener());
							conn.addOnRecevieMsgListener(new LogOutListener());
							conn.addOnRecevieMsgListener(new AddfriendListener());
							conn.addOnRecevieMsgListener(new AddgroupListener());
							conn.addOnRecevieMsgListener(new NewGroupListener());
							conn.addOnRecevieMsgListener(new ZhuceListener(conn));
							conn.addOnRecevieMsgListener(new OnLineListener(conn));
							//该线程内等待用户数据
							conn.connect();
						} catch (IOException e) {
							e.printStackTrace();
							conn.disconnect();
						}
					}
				};
			}.start();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
