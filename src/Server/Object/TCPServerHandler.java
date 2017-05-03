package Server.Object;
import java.util.Enumeration;
import java.util.Hashtable;

import AccountList.Account;
import AccountList.AccountList;
import AccountList.DefaultAccount;
import AccountList.DefaultAccountList;
import Event.EventGame;
import Event.EventType;
import Room.DefaultRoom;
import Room.DefaultRoomFactory;
import Room.Room;
import Room.RoomFactory;
import Server.origin.PlayerData;
import io.netty.channel.Channel;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

public class TCPServerHandler extends SimpleChannelInboundHandler<Object> {

	
	private Hashtable<Channel, Integer> ALL_CHANNELS = new Hashtable<Channel,Integer>();
	public Hashtable<Integer, Account> accMap = new Hashtable<Integer, Account>();
	private AccountList All_ACCOUNT = new DefaultAccountList(accMap);
	
	private static Hashtable<Integer, Room> roomMap = new Hashtable<Integer,Room>();
	private static RoomFactory roomFactory = new DefaultRoomFactory(roomMap);

	
	
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Object obj) throws Exception {
		
		System.out.println((EventGame)obj);
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
		case EventType.LOG_IN:
			onLogin(channel,obj);
			break;
		case EventType.GAME_ROOM_JOIN :
			onGameRoomJoin(channel,obj);
			break;
		case EventType.POSSITION :
			onPossition(channel, obj);
			break;	
		
		default:
			break;
		}
	}
	
	private void onConnect(Channel channel,Object obj) {	
		System.out.println("onConnect");
		
				
	}
	
	private void onLogin(Channel channel, Object obj) {								
		EventGame eve = (EventGame)obj;
		Account acc;
		
		Enumeration accElements = accMap.elements();
		boolean isExits = false;
		String data = eve.getData();
		String[] parts = data.split("-");
		String part1 = parts[0];
		String part2 = parts[1]; 
		//System.out.println(part1 + part2); 
		while(accElements.hasMoreElements()) {
	         acc = (Account) accElements.nextElement();
	         if(accMap.get(acc.getID()).getName().equals(part1) 
	        && accMap.get(acc.getID()).getPass().equals(part2))
	         {
	        	 System.out.println(accMap.get(acc.getID()).getName() + 
	        			 accMap.get(acc.getID()).getPass() );
	        	 ALL_CHANNELS.put(channel,acc.getID());
	        	 All_ACCOUNT.addAcc(new DefaultAccount(acc.getID(),channel,acc.getName(),acc.getPass()));
	        	 isExits =true;	        	 
	         }
	      }
		System.out.println(isExits);
		if(isExits)
			{
				SendEventToClient(channel, newEvent(EventType.LOG_IN_SUCCESS, "loginsucces"));
				
			}
		else 
			SendEventToClient(channel, newEvent(EventType.LOG_IN_FAILURE, "loginfail"));
	}
	private void onGameRoomJoin(Channel channel,Object obj)
	{
		System.out.println("onGameRoomJoin");
		EventGame e = (EventGame)obj;
		int room = Integer.parseInt(e.getData());
		int id = ALL_CHANNELS.get(channel);
		
		if(roomFactory.contains(room) == false)
		{
			System.out.println("Room ko ton tai");
			roomFactory.newRoom(room);
			roomFactory.getRoom(room).addPlayer(All_ACCOUNT.getAcc(id));
			All_ACCOUNT.getAcc(id).setRoom(room);
			SendEventToClient(channel, newEvent(EventType.GAME_ROOM_JOIN_SUCCESS,
					room + "-" + getPlayerRoot(room)));
			System.out.println("room size" + roomFactory.getRoom(room).getSize());
			
		}
		else
		{
			System.out.println("Room da ton tai");
			if(!roomFactory.getRoom(room).isFull())				
			{
				roomFactory.getRoom(room).addPlayer(All_ACCOUNT.getAcc(id));
				All_ACCOUNT.getAcc(id).setRoom(room);
				SendEventToClient(channel, newEvent(EventType.GAME_ROOM_JOIN_SUCCESS,
						room + "-" + getPlayerRoot(room)));
			}
			else 
			{
				System.out.println("Room da full");
				SendEventToClient(channel, newEvent(EventType.GAME_ROOM_JOIN_FAILURE,
						"game room join failure"));
			}
			System.out.println("room size" + roomFactory.getRoom(room).getSize());
		}
		
		
		
	}
	
	private String getPlayerRoot(int room)
	{
		if(roomFactory.getRoom(room).getSize() == 1 ) return "1";
		else return "2";
	}
	
	private void onPossition(Channel channel, Object obj)
	{
		
		EventGame eve = (EventGame)obj;
		int id = ALL_CHANNELS.get(channel);
		int room = All_ACCOUNT.getAcc(id).getRoom();
		System.out.println(roomFactory.getRoom(room).getChannelGroup());
		
		for(Channel ch: roomFactory.getRoom(room).getChannelGroup())
		{
			if(ch!= channel)
			{
				SendEventToClient(ch, newEvent(EventType.POSSITION, eve.getData()));
			}
			
		}
		
	}
	private int checkEventType(Object obj)
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
		

		SendEventToClient(ctx.channel(), newEvent(EventType.CONNECT_SUCCESS, "connect success"));
		

		
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
