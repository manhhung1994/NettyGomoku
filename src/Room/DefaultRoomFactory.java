package Room;

import java.util.Hashtable;

import AccountList.Account;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

public class DefaultRoomFactory implements RoomFactory{
	public Hashtable<Integer, Room> roomFactory = new Hashtable<Integer,Room>();
	
	public static ChannelGroup ROOM_1 = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
	public static ChannelGroup ROOM_2 = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
	public static ChannelGroup ROOM_3 = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
	public static ChannelGroup ROOM_4 = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
	public static int[][] MATRIX1 	  = new int[15][15];
	public static int[][] MATRIX2 	  = new int[15][15];
	public static int[][] MATRIX3 	  = new int[15][15];
	public static int[][] MATRIX4 	  = new int[15][15];
	
	public DefaultRoomFactory(Hashtable<Integer, Room> map) {
		roomFactory = map;
		
	}

	@Override
	public void newRoom(int id) {	
		
		switch (id) {
		case 1:
			roomFactory.put(id, new DefaultRoom(ROOM_1,MATRIX1));
			break;
		case 2 : 
			roomFactory.put(id, new DefaultRoom(ROOM_2,MATRIX2));
			break;
		case 3:
			roomFactory.put(id, new DefaultRoom(ROOM_3,MATRIX3));
			break;
		case 4 : 
			roomFactory.put(id, new DefaultRoom(ROOM_4,MATRIX4));
			break;
		default:
			break;
		}
	}

	@Override
	public void removeRoom(int id) {
		roomFactory.remove(id);
		
	}

	@Override
	public Room getRoom(int room) {
		return roomFactory.get(room);
	}
	@Override
	public boolean contains(int id)
	{
		return roomFactory.containsKey(id);
	}
	
	
	
	
}
