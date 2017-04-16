package Sourcegame;

import java.net.*;
import java.io.*;
import java.util.*;


import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOutboundInvoker;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

/*
 * The Client that can be run both as a console or a GUI
 */
public class Client  {

	private Channel channel;

	private String server, username;
	private int port;


	  Client(String server, int port, String username) {
		
		  this.server = server;
			this.port = port;
			this.username = username;
	}


	public void start() throws Exception{
		EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap  = new Bootstrap()
                    .group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ClientInitializer());
             channel = bootstrap.connect(server, port).sync().channel();
           
            //System.out.println(channel.isActive());
            
        } catch (Exception e) {
            e.printStackTrace();
        }
	}


	public void sendMessage(ChatMessage msg) {
		

		channel.writeAndFlush(msg + "\r\n");
	}
	public void sendData(String data)
	{
		channel.writeAndFlush(data+"\r\n");
	}

	
	public static void main(String[] args) {
			
		
	}
}
