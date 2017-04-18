package Server.Impl;

import java.util.Map;
import java.util.Set;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class NettyTCPServer extends AbstractNettyServer{
	private ServerBootstrap serverBootStrap;
	
	public NettyTCPServer(NettyConfig nettyConfig,
			ChannelInitializer<? extends Channel> channelInitializer)
	{
		super(nettyConfig,channelInitializer);
	}
	@Override
	public void startServer() throws Exception
	{
		try 
		{
			serverBootStrap = new ServerBootstrap();
			Map<ChannelOption<?>, Object> channelOptions = nettyConfig.getChannelOptions();
			if(null != channelOptions){
				Set<ChannelOption<?>> keySet = channelOptions.keySet();
				for(@SuppressWarnings("rawtypes") ChannelOption option : keySet)
				{
					serverBootStrap.option(option, channelOptions.get(option));
				}
			}
			serverBootStrap.group(getBossGroup(),getWorkerGroup())
					.channel(NioServerSocketChannel.class)
					.childHandler(getChannelInitializer());
			Channel serverChannel = serverBootStrap.bind(nettyConfig.getSocketAddress()).sync()
					.channel();
			ALL_CHANNELS.add(serverChannel);
		} 
		catch (Exception e) {
			System.out.println("TCP Server start error {}, going to shut down");
			super.stopServer();
			throw e;
		}
	}
	
	@Override
	public void setChannelInitializer(ChannelInitializer<? extends Channel> initializer) {
		this.channelInitializer = initializer;
		serverBootStrap.childHandler(initializer);
		
	}
	@Override
	public TransmissionProtocol getTransmissionProtocol() {
		return TRANSMISSION_PROTOCOL.TCP;
	}
	@Override
	public String toString() {
		return "NettyTCPServer [socketAddress=" + nettyConfig.getSocketAddress()
				+ ", portNumber=" + nettyConfig.getPortNumber() + "]";
	}
	
}
