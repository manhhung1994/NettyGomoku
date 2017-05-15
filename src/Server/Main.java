package Server;

import Database.DefaultSQL;
import Database.MySQL;

public class Main {

	static MySQL myDatabase = new DefaultSQL();
	public static void main(String[] args) throws Exception {
		myDatabase.resetAll();
		NettyTCPServer tcpServer  = new  NettyTCPServer(); 
		tcpServer.startServer(9696);
		
				
;	}

}
