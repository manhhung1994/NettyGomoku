package Server;

import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.ChannelGroupException;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

public class RoomData {
	private ChannelGroup 	channelGroup ;
	public ChannelGroup getChannelGroup() {
		return channelGroup;
	}
	public void setChannelGroup(ChannelGroup channelGroup) {
		this.channelGroup = channelGroup;
	}
	private boolean 		isFull;
	private PlayerData 		player1,player2;
	public RoomData(){
		channelGroup  	= new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
		isFull 			= false;
	}
	public void AddPlayer(PlayerData data)
	{
		if(!isFull)
		{
			if(player1 == null) 
				player1 = data;
			else if (player2 ==null)
				player2 = data;
			channelGroup.add(data.getChannel());
			
		}
		else 
		{
			System.out.println("Room full");
		}
	}
	public void ClosePlayer(PlayerData data)
	{
		
	}
}
