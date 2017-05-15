package Server;


import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class NettyTCPServer implements Server {

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
