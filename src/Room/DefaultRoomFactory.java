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

	public DefaultRoomFactory(Hashtable<Integer, Room> map) {
		roomFactory = map;
		
	}

	@Override
	public void newRoom(int id) {	
		
		switch (id) {
		case 1:
			roomFactory.put(id, new DefaultRoom(ROOM_1));
			break;
		case 2 : 
			roomFactory.put(id, new DefaultRoom(ROOM_2));
			break;
		case 3:
			roomFactory.put(id, new DefaultRoom(ROOM_3));
			break;
		case 4 : 
			roomFactory.put(id, new DefaultRoom(ROOM_4));
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
	public Room getRoom(int id) {
		return roomFactory.get(id);
	}
	@Override
	public boolean contains(int id)
	{
		return roomFactory.containsKey(id);
	}
	
	
	
	
}
