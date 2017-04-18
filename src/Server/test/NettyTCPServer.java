package Server.test;


import Event.EventGame;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.GlobalEventExecutor;

public class NettyTCPServer implements Server {

	public static final ChannelGroup ALL_CHANNELS = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
	private EventLoopGroup bossGroup = new NioEventLoopGroup(); // (1)
    private EventLoopGroup workerGroup = new NioEventLoopGroup();
	public NettyTCPServer()
	{
		
	}
	@Override
	public void startServer(int port) throws Exception {
		
        try 
        {
            ServerBootstrap b = new ServerBootstrap(); // (2)
            b.group(bossGroup, workerGroup)
             .channel(NioServerSocketChannel.class) // (3)
             .childHandler(new TcpServerInitializer())  //(4)
             .option(ChannelOption.SO_BACKLOG, 128)          // (5)
             .childOption(ChannelOption.SO_KEEPALIVE, true); // (6)
           
           ChannelFuture f = b.bind(port).sync();
           System.out.println("Server started " + port);
           f.channel().closeFuture().sync();
           
           ALL_CHANNELS.add(f.channel());
        } 
     catch (Exception e) 
        {
    
        }
	}
        
	@Override
	public void stopServer() throws Exception {
		bossGroup.shutdownGracefully();
		workerGroup.shutdownGracefully();
		
	}
	
}
