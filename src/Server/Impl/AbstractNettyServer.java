package Server.Impl;
import java.net.InetSocketAddress;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoop;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.ChannelGroupFuture;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
public abstract class AbstractNettyServer implements NettyServer{

	public static final ChannelGroup ALL_CHANNELS =new DefaultChannelGroup("ALL_CHANNELS",GlobalEventExecutor.INSTANCE);
	protected final NettyConfig nettyConfig;
	protected ChannelInitializer<? extends Channel> channelInitializer;
	
	public  AbstractNettyServer(NettyConfig nettyConfig,
			ChannelInitializer<? extends Channel> channelInitializer) {
		this.nettyConfig = nettyConfig;
		this.channelInitializer = channelInitializer;
	}
	
	@Override
	public void startServer(int port) throws Exception
	{
		nettyConfig.setPortNumber(port);
		nettyConfig.setSocketAddress(new InetSocketAddress(port));
		startServer();
		
	}
	@Override
	public void startServer(InetSocketAddress socketAddress) throws Exception
	{
		nettyConfig.setSocketAddress(socketAddress);
		startServer();
	}
	@Override
	public void stopServer() throws Exception 
	{
		ChannelGroupFuture future = ALL_CHANNELS.close();
		try
		{
			future.wait();
		}
		catch(InterruptedException e)
		{
			System.out.println("Execption occurred while waiting for channels to close: {}");
			
		}
		finally
		{
			if(null != nettyConfig.getBossGroup())
			{
				nettyConfig.getBossGroup().shutdownGracefully();				
			}
			if(null != nettyConfig.getWorkerGroup())
			{
				nettyConfig.getWorkerGroup().shutdownGracefully();
			}
		}		
	}
	@Override
	public ChannelInitializer<? extends Channel> getChannelInitializer()
	{
		return channelInitializer;
	}

	@Override
	public NettyConfig getNettyConfig()
	{
		return nettyConfig;
	}
	@Override
	public InetSocketAddress getSocketAddress()
	{
		return nettyConfig.getSocketAddress();
	}
	@Override
	public String toString()
	{
		return "NettyServer [socketAddress=" + nettyConfig.getSocketAddress()
		+ ", portNumber=" + nettyConfig.getPortNumber() + "]"; 
	}
	protected EventLoopGroup getBossGroup()
	{
		return nettyConfig.getBossGroup();
	}
	protected EventLoopGroup getWorkerGroup()
	{
		return nettyConfig.getWorkerGroup();	
	}
	
}
