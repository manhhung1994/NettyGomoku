package Server.Object;
import java.util.Enumeration;
import java.util.Hashtable;

import AccountList.Account;
import AccountList.AccountList;
import AccountList.DefaultAccount;
import AccountList.DefaultAccountList;
import Event.EventGame;
import Event.EventType;
import Event.Status;
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
	        	/* System.out.println(accMap.get(acc.getID()).getName() + 
	        			 accMap.get(acc.getID()).getPass() );
	        	 */

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
			SendEventToClient(channel, newEvent(EventType.GAME_ROOM_JOIN_SUCCESS,
					All_ACCOUNT.getAcc(id).getName() + "-" + Integer.toString(slot) ));
			System.out.println("room size" + roomFactory.getRoom(room).getSize());
			
			if(roomFactory.getRoom(room).getSize() == 2)
			{
				//gui data enemy;
				for(Channel ch : roomFactory.getRoom(room).getChannelGroup())
				{
					
					SendEventToClient(ch, newEvent(EventType.ENEMYDATA,
							"Enemy" +"-" 
							+ Boolean.toString(All_ACCOUNT.getAcc(id).isReady())));
					
					/*
					if(ch!= channel)
					{						
						SendEventToClient(ch, newEvent(EventType.ENEMYDATA,
										All_ACCOUNT.getAcc(id).getName() +"-" 
										+ Boolean.toString(All_ACCOUNT.getAcc(id).isReady())));
						idEnemy = ALL_CHANNELS.get(ch);
						System.out.println("id enemy " +idEnemy);
					}
					else
					{
						System.out.println("id enemy 2 : " +idEnemy);
						SendEventToClient(channel, newEvent(EventType.ENEMYDATA,
								All_ACCOUNT.getAcc(idEnemy).getName() +"-" 
								+ Boolean.toString(All_ACCOUNT.getAcc(idEnemy).isReady())));
					}*/
					
				}
			}
		}
		else 
		{
			System.out.println("Room full");
			SendEventToClient(channel, newEvent(EventType.GAME_ROOM_JOIN_FAILURE,
					"game room join failure"));
		}
		
		/*
		if(roomFactory.contains(room) == false)
		{
			System.out.println("Room ko ton tai");
			roomFactory.newRoom(room);
			roomFactory.getRoom(room).addPlayer(All_ACCOUNT.getAcc(id));
			All_ACCOUNT.getAcc(id).setRoom(room);
			All_ACCOUNT.getAcc(id).setRoot(true);
			SendEventToClient(channel, newEvent(EventType.GAME_ROOM_JOIN_SUCCESS,
					All_ACCOUNT.getAcc(id).getName() +"-" +"nullPlayer2"+ "-" + getPlayerRoot(room)));
			System.out.println("room size" + roomFactory.getRoom(room).getSize());
			
		}
		else
		{
			System.out.println("Room da ton tai");
			if(!roomFactory.getRoom(room).isFull())				
			{
				roomFactory.getRoom(room).addPlayer(All_ACCOUNT.getAcc(id));
				All_ACCOUNT.getAcc(id).setRoom(room);
				if(getPlayerRoot(room).equals("2")) // neu la player2
				{
					int idPlayer1;
					All_ACCOUNT.getAcc(id).setRoot(false);
					for(Channel ch: roomFactory.getRoom(room).getChannelGroup())
					{
						if(ch!= channel)
						{
							SendEventToClient(ch, newEvent(EventType.PLAYER2_JOINROOM,
												roomFactory.getRoom(room).getPlayer(id).getName()));
							idPlayer1 = ALL_CHANNELS.get(ch);
						}
						else
						{
							// gui cho minh ten cua minh va thang player1
							SendEventToClient(channel, newEvent(EventType.GAME_ROOM_JOIN_SUCCESS,
										All_ACCOUNT.getAcc(id).getName()+"-" + 
											All_ACCOUNT.getAcc(idPlayer1).getName() + "-" + 
												getPlayerRoot(room)));
						}
							
						
					}
				}
				
				
			}
			else 
			{
				System.out.println("Room da full");
				SendEventToClient(channel, newEvent(EventType.GAME_ROOM_JOIN_FAILURE,
						"game room join failure"));
			}
			System.out.println("room size" + roomFactory.getRoom(room).getSize());
		}
		
		
		*/
		
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
	private String getPlayerRoot(int room)
	{
		if(roomFactory.getRoom(room).getSize() == 1 ) return "1";
		else return "2";
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
					SendEventToClient(ch, newEvent(EventType.CHECKWIN, "true"));
				
				SendEventToClient(ch, newEvent(EventType.ENEMY_LEAVE,"" ));
			}
			else				
			{			
				
				SendEventToClient(channel, newEvent(EventType.ROOM_LEAVE_SUCCESS, ""));
				System.out.println("ROOM_LEAVE_SUCCESS");
				roomFactory.getRoom(room).removePlayer(All_ACCOUNT.getAcc(id));
				All_ACCOUNT.getAcc(id).setRoom(0);
			}
		}
		
	}
	private void onReady(Channel channel, Object obj) {
		int id = ALL_CHANNELS.get(channel);
		int room = All_ACCOUNT.getAcc(id).getRoom();
		roomFactory.getRoom(room).getPlayer(id).setStatus(Status.READY);
		SendEventToClient(channel, newEvent(EventType.READY_SUCCESS, ""));
		EventGame eve = (EventGame)obj;
		
		if(eve.getData().equals("player2"))
		{
			for(Channel ch : roomFactory.getRoom(room).getChannelGroup())
			{
				if(ch!= channel)
					SendEventToClient(ch, newEvent(EventType.PLAYER2_READY, ""));
			}
		}
		else
		{
			
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
				SendEventToClient(ch, newEvent(EventType.POSSITION, eve.getData()));
				if(checkWin) 
					SendEventToClient(ch, newEvent(EventType.CHECKWIN, "false"));
			}
			else 
			{
				// gui cho minh
				if(checkWin)
					SendEventToClient(ch, newEvent(EventType.CHECKWIN, "true"));
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
