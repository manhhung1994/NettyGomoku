package Server.origin;

import java.io.*;
import java.net.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import DataPackage.DataPackage;
import DataPackage.TypeData;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.concurrent.GlobalEventExecutor;

import org.codehaus.jackson.map.ObjectMapper;
/*
 * The server that can be run both as a console application or a GUI
 */
public class Server {
	// the port number to listen for connection
	private int port;
	public static ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

	private EventLoopGroup bossGroup = new NioEventLoopGroup(); 
    private EventLoopGroup workerGroup = new NioEventLoopGroup();
    
    private RoomManager roomManager ;
    private int tempRoom;
    private ObjectMapper jackson;
	/*
	 *  server constructor that receive the port to listen to for connection as parameter
	 *  in console
	 */
	public Server(int port) {
		this(port, null);
	}
	
	public Server(int port, ServerGUI sg) {
		// the port
		this.port = port;
		new SimpleDateFormat("HH:mm:ss");
		// ArrayList for the Client list
		//al = new ArrayList<ClientThread>();
		roomManager = new RoomManager(6);
	}
	
	public void start() throws Exception {
		//System.out.println("Server start");
		
        try {
            ServerBootstrap b = new ServerBootstrap(); 
            b.group(bossGroup, workerGroup)
             .channel(NioServerSocketChannel.class) 
             .childHandler(new ChannelInitializer<SocketChannel>() {             
				@Override
				protected void initChannel(SocketChannel channel) throws Exception {
					ChannelPipeline pipeline = channel.pipeline();

			        pipeline.addLast("framer", new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));
			        pipeline.addLast("decoder", new StringDecoder());
			        pipeline.addLast("encoder", new StringEncoder());
			        pipeline.addLast("handler", new SimpleChannelInboundHandler(){

			        	@Override
			            public void handlerAdded(ChannelHandlerContext ctx) throws Exception {  // (2)
			                Channel incoming = ctx.channel();
			                PlayerData data = new PlayerData(ctx.channel(), ctx.channel().localAddress() +"", 1);
			                // Broadcast a message to multiple Channels
			                //channels.writeAndFlush("[SERVER] - " + incoming.remoteAddress() + " handlerAdded\n");
			                
			               /* if(!roomManager.CheckFull(1))
			                	
			                {
			                	System.out.println("size room 1 : " +roomManager.getRoom(1).size());
			                	roomManager.getRoom(1).add(ctx.channel());
			                }
			                */
			            }

			            @Override
			            public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {  // (3)
			                Channel incoming = ctx.channel();
			                
			                // Broadcast a message to multiple Channels
			                //channels.writeAndFlush("[SERVER] - " + incoming.remoteAddress() + " handlerRemoved\n");
			                
			                // A closed Channel is automatically removed from ChannelGroup,
			                // so there is no need to do "channels.remove(ctx.channel());"
			            }
			            
			          
			        	@Override
			        	public void channelActive(ChannelHandlerContext ctx) throws Exception { // (5)
			                Channel incoming = ctx.channel();
			        		System.out.println("Server:"+incoming.remoteAddress()+"channelActive");
			        	}
			        	
			        	@Override
			        	public void channelInactive(ChannelHandlerContext ctx) throws Exception { // (6)
			                Channel incoming = ctx.channel();
			        		System.out.println("Server:"+incoming.remoteAddress()+"channelInactive");
			        	}
			            @Override
			            public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) { 
			            	Channel incoming = ctx.channel();
			        		System.out.println("Server:"+incoming.remoteAddress()+"exceptionCaught");
			                cause.printStackTrace();
			                ctx.close();
			            }

						@Override
						protected void channelRead0(ChannelHandlerContext ctx, Object s) throws Exception {
							//boolean roomFull =roomManager.CheckFull(tempRoom); 
							if(DataPackage.CheckTypeData(s.toString()).equals(TypeData.CHECK_ROOM))
							{
								tempRoom =  DataPackage.RoomChoose(s.toString());
								System.out.println("temp room" + roomManager.CheckFull(tempRoom));
								if(roomManager.CheckFull(tempRoom) == false)
								{
									roomManager.getRoom(tempRoom).add(ctx.channel());
									if(roomManager.getRoom(tempRoom).size()==1)
									{
										ctx.channel().writeAndFlush(TypeData.POSITION+ "-1" +"\n");
										System.out.println("Set vi tri 1 ");
									}
									else 
									{
										ctx.channel().writeAndFlush(TypeData.POSITION+ "-2" +"\n");	
										System.out.println("Set vi tri 2 ");
									}
								}
								else 
								{
									System.out.println("Room Full");
								}
							}							
							else {
								System.out.println("Khong check room nua");
							}
							if(DataPackage.CheckTypeData(s.toString()).equals(TypeData.CHECK_ROOM))
							{
								
							}
							Channel incoming = ctx.channel();
							
			        		for (Channel channel : roomManager.getRoom(tempRoom)) {
			                    if (channel != incoming){
			                        channel.writeAndFlush( s + "\n");
			                    } else {
			                    	channel.writeAndFlush("[you]" + s + "\n");
			                    }
			                }
			        		System.out.print("[" +incoming.remoteAddress() +"] "+ s.toString()+"\n");
							
						}
			        });
			 
					System.out.println("SimpleChatClient:"+channel.remoteAddress() +"initChannel");
					
				}
             })

             .option(ChannelOption.SO_BACKLOG, 128)         
             .childOption(ChannelOption.SO_KEEPALIVE, true);

            ChannelFuture f = b.bind(port).sync();
            System.out.println("Server started " + port);
            f.channel().closeFuture().sync();

        } finally {
            stop();
                		
        }
		
	}		
    
    
	protected void stop() {
		bossGroup.shutdownGracefully();
		workerGroup.shutdownGracefully();
	}
}
