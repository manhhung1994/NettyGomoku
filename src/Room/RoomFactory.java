package Room;

import AccountList.Account;
import io.netty.channel.group.ChannelGroup;

public interface RoomFactory {
	
	public void newRoom(int id);
	public void removeRoom(int id);
	public Room getRoom(int id);
	public boolean contains(int id);
}
