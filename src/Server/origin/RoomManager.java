package Server.origin;

import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

public class RoomManager{
		
	private ChannelGroup[] Rooms ;
	
	public RoomManager(int leng)
	{
		Rooms = new DefaultChannelGroup[leng];
		for(int i=0;i<leng ; i++)
		{
			Rooms[i] = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
		}
	}

	public ChannelGroup getRoom(int index) {
		return Rooms[index-1];
	}

	public void setRoom(ChannelGroup channelGroup,int index) {
		this.Rooms[index-1] = channelGroup;
	}
	
	public void AddPlayer(int room, Channel playerChannel)
	{
		
	}
	public boolean CheckFull(int index)
	{
		
		return (Rooms[index-1].size()> 1 ? true : false );
	}
	
	
}
