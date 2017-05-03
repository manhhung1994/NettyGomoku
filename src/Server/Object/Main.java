package Server.Object;

import AccountList.Account;
import AccountList.DefaultAccount;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

public class Main {

	
	public static void main(String[] args) throws Exception {
		NettyTCPServer tcpServer  = new  NettyTCPServer(); 
		tcpServer.startServer(9696);
		
				
;	}

}
