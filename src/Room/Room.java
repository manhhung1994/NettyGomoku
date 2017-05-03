package Room;

import AccountList.Account;
import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;

public interface Room {
	
	public boolean isFull();
	public void addPlayer(Account account);
	public boolean removePlayer(Channel channel);
	public int getSize();
	public void setSize(int size);
	public Account getPlayer(int id);
	public ChannelGroup getChannelGroup();
	
}