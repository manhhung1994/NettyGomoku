package Server;


import Event.EventGame;
import Jackson.JacksonDecoder;
import Jackson.JacksonEncoder;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;


public class TcpServerInitializer extends ChannelInitializer<SocketChannel>{

	@Override
	protected void initChannel(SocketChannel socketChannel) throws Exception {
		ChannelPipeline pipeline = socketChannel.pipeline();
	
		pipeline.addLast(new JacksonEncoder());
		pipeline.addLast(new JacksonDecoder<EventGame>(EventGame.class));
		pipeline.addLast(new TCPServerHandler());
		
		

        System.out.println("["+socketChannel.remoteAddress() +"] init Channel");
	}
}
