package App;

import io.netty.channel.Channel;

public interface Player {
	public void setID(int id);
	public int getID();
	public void setName(String name);
	public String getName();
	public void setChannel(Channel channel);
	public Channel getChannel();
	
	
}
