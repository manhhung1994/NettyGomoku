package Server.test;
import Event.EventGame;
import Event.EventType;
import Jackson.*;
import Server.origin.PlayerData;

import io.netty.channel.Channel;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class TCPServerHandler extends SimpleChannelInboundHandler<Object> {


	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Object obj) throws Exception {
		EventGame event = (EventGame)obj;
		System.out.println(event.getEventType());
		System.out.println(event.getData());
		
		ChannelReadController(ctx.channel(), obj);
		ctx.channel().write(event);
		ctx.channel().flush();
	}
	public void ChannelReadController(Channel channel, Object obj)
	{
		int eventType = checkEventType(obj);
		switch (eventType) {
		case EventType.CONNECT:
			onConnect(channel);
			break;

		default:
			break;
		}
	}
	private void onConnect(Channel channel) {
		System.out.println("["+ channel.remoteAddress() + "]" + "connected");
		//SendEventToClient(channel, newEvent(EventType.LOG_IN_SUCCESS, ""));
		
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
		
		SendEventToClient(incoming,newEvent(EventType.LOG_IN_SUCCESS,""));
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
