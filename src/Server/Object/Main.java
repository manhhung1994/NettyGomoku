package Server.Object;

import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

public class Main {
	public static ChannelGroup ALL_CHANNELS = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

	public static void main(String[] args) throws Exception {
		NettyTCPServer tcpServer  = new  NettyTCPServer(); 
		tcpServer.startServer(9696);
		
				
;	}

}
