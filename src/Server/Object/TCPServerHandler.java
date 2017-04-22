package Server.Object;
import java.beans.EventSetDescriptor;

import Event.EventGame;
import Event.EventType;
import Jackson.*;
import Server.origin.PlayerData;

import io.netty.channel.Channel;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

public class TCPServerHandler extends SimpleChannelInboundHandler<Object> {

	public static ChannelGroup ALL_CHANNELS = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Object obj) throws Exception {
	
		ChannelReadController(ctx.channel(), obj);
		
        
	
	}
	public void ChannelReadController(Channel channel, Object obj)
	{
		int eventType = checkEventType(obj);
		EventGame eve = (EventGame)obj;
		switch (eventType) {
		case EventType.CONNECT:
			onConnect(channel,obj);
			break;
		case EventType.GAME_ROOM_JOIN :
			onGameRoomJoin(channel,obj);
			break;
		case EventType.ANY :
			for (Channel ch : ALL_CHANNELS) {
	            if (ch != channel){
	                SendEventToClient(ch, newEvent(EventType.ANY, "[" +channel.remoteAddress()+ "] -> : " + eve.getData() ));
	            } else {
	            	SendEventToClient(channel, newEvent(EventType.ANY, "[you] " + eve.getData()) );
	            }
	        }
			break;	
		default:
			break;
		}
	}
	private void onConnect(Channel channel,Object obj) {	
		System.out.println("onConnect");
		SendEventToClient(channel, newEvent(EventType.LOG_IN_SUCCESS, "game rom join"));

		
	}
	private void onGameRoomJoin(Channel channel,Object obj)
	{
		EventGame e = (EventGame)obj;
		int room = Integer.parseInt(e.getData());
		if(checkRoomFull(room))
		{
			SendEventToClient(channel, newEvent(EventType.GAME_ROOM_JOIN_SUCCESS,""));
		}
		System.out.println("onGameRoomJoin");
		
	}
	public boolean checkRoomFull(int room)
	{
		return true;
	}
	public int checkEventType(Object obj)
	{
		EventGame event = (EventGame)obj;
		return event.getEventType();
	}
	@Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {  // (2)
        Channel incoming = ctx.channel();
        PlayerData data = new PlayerData(ctx.channel(), ctx.channel().localAddress() +"", 1);
        
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {  // (3)
        Channel incoming = ctx.channel();
        

    }
    
  
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception { // (5)
        Channel incoming = ctx.channel();
		System.out.println("[" + incoming.remoteAddress() + "] Active");
		
		SendEventToClient(ctx.channel(), newEvent(EventType.LOG_IN_SUCCESS, "game rom join"));
		
		ALL_CHANNELS.add(incoming);
		System.out.println("size " + ALL_CHANNELS.size());
	}
	public EventGame newEvent(int eventType,String data)
	{
		EventGame event = new EventGame();
		event.setData(data);
		event.setEventType(eventType);
		
		return event;
	}
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception { // (6)
        Channel incoming = ctx.channel();
		System.out.println("[" + incoming.remoteAddress() + "] Inactive" );
	}
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) { 
    	Channel incoming = ctx.channel();
		System.out.println("Server:"+incoming.remoteAddress()+"exceptionCaught");
        cause.printStackTrace();
        ctx.close();
    }
	public void SendEventToClient(Channel channel, EventGame event)
	{
		channel.write(event);
		channel.flush();
	}

}
