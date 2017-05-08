package Server.Object;
import java.util.Enumeration;
import java.util.Hashtable;

import AccountList.Account;
import AccountList.AccountList;
import AccountList.DefaultAccount;
import AccountList.DefaultAccountList;
import Database.DefaultSQL;
import Database.MySQL;
import Event.EventGame;
import Event.EventType;
import Event.Status;
import Room.DefaultRoom;
import Room.DefaultRoomFactory;
import Room.Room;
import Room.RoomFactory;

import io.netty.channel.Channel;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;


public class TCPServerHandler extends SimpleChannelInboundHandler<Object> {
	public static MySQL myDatabase = new DefaultSQL();
	
	public static Hashtable<Channel, Integer> ALL_CHANNELS = new Hashtable<Channel,Integer>();
	public static Hashtable<Integer, Account> accMap = new Hashtable<Integer, Account>();
	public static  AccountList All_ACCOUNT = new DefaultAccountList(accMap);
	
	private static Hashtable<Integer, Room> roomMap = new Hashtable<Integer,Room>();
	private static RoomFactory roomFactory = new DefaultRoomFactory(roomMap);

	public int idEnemy;
	
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
		case EventType.LOG_IN:
			onLogin(channel,obj);
			break;
		case EventType.LOG_OUT:
			onLogOut(channel,obj);
			break;
		case EventType.REGISTER:
			onRegister(channel,obj);
			break;
		case EventType.GAME_ROOM_JOIN :
			onGameRoomJoin(channel,obj);
			break;
		case EventType.READY :
			onReady(channel,obj);
			break;
		case EventType.GAME_ROOM_LEAVE:
			onGameRoomLeave(channel,obj);
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
	private void onRegister(Channel channel,Object obj)
	{
		System.out.println("onRegister");
		EventGame eve = (EventGame)obj;
		String[] tokens = eve.getData().split("-");
		System.out.println(tokens[0] + tokens[1] + tokens[2]);
		if(myDatabase.newAcc(tokens[0], tokens[1], tokens[2]))
		{
			System.out.println("dang ky thanh cong");
			SendEventToClient(channel, newEvent(EventType.REGISTER_SUCCESS, ""));
		}
		else 
		{
			System.out.println("dang ky that bai");
			SendEventToClient(channel, newEvent(EventType.REGISTER_FAILURE, ""));

		}
	}
	private  void onLogin(Channel channel, Object obj) {								
		EventGame eve = (EventGame)obj;
		
		String data = eve.getData();
		String[] parts = data.split("-");
		String userName = parts[0];
		String pass = parts[1]; 
		int id = myDatabase.checkAcc(userName, pass);
		
		//neu id ton tai va id ko online
		if(id !=0 && !myDatabase.getOnline(id)) 
		{
			SendEventToClient(channel, newEvent(EventType.LOG_IN_SUCCESS, "loginsucces"));
			ALL_CHANNELS.put(channel,id);
			All_ACCOUNT.addAcc(new DefaultAccount(id,channel,myDatabase.getName(id),myDatabase.getPass(id),	myDatabase.getNickName(id)));
			myDatabase.setOnline(id, true);
			System.out.println(myDatabase.getName(id) + "login");
			
		}
		else
		{
			//if account not exited
			if(id == 0)
				SendEventToClient(channel, newEvent(EventType.LOG_IN_FAILURE, "notexit"));
			// if account is online
			if(myDatabase.getOnline(id))
				SendEventToClient(channel, newEvent(EventType.LOG_IN_FAILURE, "online"));
		}
	}
	private  void onLogOut(Channel channel, Object obj) 
	{
		
		int id = ALL_CHANNELS.get(channel);
		myDatabase.setOnline(id, false);
		System.out.println(myDatabase.getName(id) +" onlogout");
	}
	private void onGameRoomJoin(Channel channel,Object obj)
	{
		
		System.out.println("onGameRoomJoin");
		EventGame e = (EventGame)obj;
		int room = Integer.parseInt(e.getData());
		int id = ALL_CHANNELS.get(channel);
		if(roomFactory.contains(room) == false)
		{
			roomFactory.newRoom(room);
		}
		int slot = checkSlot(room);
		
		System.out.print("Slot " + slot);
		if(slot != 0 )
		{			
			if(slot == 1)
			{
				All_ACCOUNT.getAcc(id).setRoot(true);	
				
			}
			if(slot == 2)
			{
				All_ACCOUNT.getAcc(id).setRoot(false);
				

			}			
			roomFactory.getRoom(room).addPlayer(All_ACCOUNT.getAcc(id));
			All_ACCOUNT.getAcc(id).setRoom(room);
			if(roomFactory.getRoom(room).getSize() == 1)
			SendEventToClient(channel, newEvent(EventType.GAME_ROOM_JOIN_SUCCESS,
					All_ACCOUNT.getAcc(id).getName() + "-" + Integer.toString(slot)+  "- " + "null" ));
			if(roomFactory.getRoom(room).getSize() == 2) 
				SendEventToClient(channel, newEvent(EventType.GAME_ROOM_JOIN_SUCCESS,
						All_ACCOUNT.getAcc(id).getName() +  "- "+ Integer.toString(slot) + "-" +"exitedEnemy"));
			System.out.println("room " + room +" size "+ roomFactory.getRoom(room).getSize());
			
			if(roomFactory.getRoom(room).getSize() == 2)
			{
				int id1 = 0;
				int id2 = 0;
				for(Channel ch : roomFactory.getRoom(room).getChannelGroup())
				{					
					System.out.println(ALL_CHANNELS.get(ch) + "-" + All_ACCOUNT.getAcc(ALL_CHANNELS.get(ch)).getChannel());
					if(ch!= channel)
					{
						id1 = ALL_CHANNELS.get(ch);
					}
					else 
					{
						id2 = ALL_CHANNELS.get(channel);
					}
					
				}
				SendEventToClient(All_ACCOUNT.getAcc(id1).getChannel(), 
						newEvent(EventType.ENEMY_JOIN_ROOM, myDatabase.getName(id2) + "-" + 
											All_ACCOUNT.getAcc(id2).isReady()));
				SendEventToClient(All_ACCOUNT.getAcc(id2).getChannel(), 
						newEvent(EventType.ENEMY_JOIN_ROOM, myDatabase.getName(id1) + "-" +
											All_ACCOUNT.getAcc(id1).isReady()));

			}
		}
		else 
		{
			System.out.println("Room full");
			SendEventToClient(channel, newEvent(EventType.GAME_ROOM_JOIN_FAILURE,
					"game room join failure"));
		}
		
		
		
	}
	private int checkSlot(int room)
	{
		if(roomFactory.getRoom(room).getSize() == 2 ) return 0;
		else
		{
			if(roomFactory.getRoom(room).getSize() == 0 ) return 1;
			
			if(roomFactory.getRoom(room).getSize() == 1)
			{
				for(Channel ch : roomFactory.getRoom(room).getChannelGroup())
				{
					
					int id = ALL_CHANNELS.get(ch);
					if(All_ACCOUNT.getAcc(id).isRoot())
						return 2;
					else return 1;
					
				}
			}
		
		}
		return 0;
		
	}

	private void onGameRoomLeave(Channel channel , Object obj)
	{
		System.out.println("onGameRoomLeave");
		int id = ALL_CHANNELS.get(channel);
		int room = All_ACCOUNT.getAcc(id).getRoom();
		EventGame eve = (EventGame)obj;

		
		
		for(Channel ch : roomFactory.getRoom(room).getChannelGroup())
		{
			if(ch!= channel)
			{
				if(eve.getData().equals("lose"))				
				{
					System.out.println("Check win true" + ch);

					//SendEventToClient(ch, newEvent(EventType.CHECKWIN, "true"));
					SendEventToClient(ch, newEvent(EventType.ENEMY_LEAVE,"win" ));
				}
				else 
				SendEventToClient(ch, newEvent(EventType.ENEMY_LEAVE,"noWin" ));
			}
			else				
			{			
				
				SendEventToClient(channel, newEvent(EventType.ROOM_LEAVE_SUCCESS, ""));
				System.out.println("ROOM_LEAVE_SUCCESS");
				
				roomFactory.getRoom(room).removePlayer(All_ACCOUNT.getAcc(id));
				All_ACCOUNT.getAcc(id).setRoom(0);
				System.out.print("size room " + room + "la " + roomFactory.getRoom(room).getSize());
			}
		}
		
	}
	private void onReady(Channel channel, Object obj) {
		int id = ALL_CHANNELS.get(channel);
		int room = All_ACCOUNT.getAcc(id).getRoom();
		roomFactory.getRoom(room).getPlayer(id).setStatus(Status.READY);
		SendEventToClient(channel, newEvent(EventType.READY_SUCCESS, ""));
		All_ACCOUNT.getAcc(id).setReady(true);

		if(roomFactory.getRoom(room).getSize() == 2)
			roomFactory.getRoom(room).resetMatrix();
		for(Channel ch : roomFactory.getRoom(room).getChannelGroup())
		{
			if(ch!= channel)
				SendEventToClient(ch, newEvent(EventType.ENEMY_READY, ""));
		}
		
		
	}
	
	private void onPossition(Channel channel, Object obj)
	{
		
		EventGame eve = (EventGame)obj;
		int id = ALL_CHANNELS.get(channel);
		int room = All_ACCOUNT.getAcc(id).getRoom();
		
		String tokens[] = eve.getData().split("-");
		int row = Integer.parseInt(tokens[0]);
		int col = Integer.parseInt(tokens[1]);
		System.out.println(row +"-" + col);
		boolean isRoot = All_ACCOUNT.getAcc(id).isRoot();
		System.out.println("isRoot" + isRoot);
		roomFactory.getRoom(room).setFlag(isRoot, row, col);
		boolean checkWin = roomFactory.getRoom(room).checkWin(isRoot, row, col);
		System.out.println("win -" + checkWin);
		for(Channel ch: roomFactory.getRoom(room).getChannelGroup())
		{
			if(ch!= channel)
			{
				// gui cho enemy
				
				if(checkWin) 
				{
					SendEventToClient(ch, newEvent(EventType.GAME_OVER, ""));
					System.out.println("Gui gameover");
				}
				SendEventToClient(ch, newEvent(EventType.POSSITION, eve.getData()));
				
			}
			
			else 
			{
				//gui cho minh
				if(checkWin)
					SendEventToClient(ch, newEvent(EventType.WIN, ""));
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
        
        
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {  // (3)
    	Channel incoming = ctx.channel();
    	System.out.println("[" + incoming.remoteAddress() + "] handlerRemoved");
    	
    }
  
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception { // (5)
        Channel incoming = ctx.channel();
		System.out.println("[" + incoming + "] Active");
		

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
