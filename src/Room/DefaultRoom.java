package Room;

import AccountList.Account;
import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;

public class DefaultRoom  implements Room{
	public ChannelGroup channelGroup ;
	public DefaultRoom(ChannelGroup channelGroup) {
		this.channelGroup = channelGroup;
	}
	@Override
	public boolean isFull() {
		if(channelGroup.size() == 2 ) return true;
		else return false;
	}

	@Override
	public void addPlayer(Account account) {
		this.channelGroup.add(account.getChannel());
		
	}

	@Override
	public boolean removePlayer(Channel channel) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getSize() {
		return this.channelGroup.size();
	}

	@Override
	public void setSize(int size) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Account getPlayer(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ChannelGroup getChannelGroup() {
		return this.channelGroup;
	}

	



}
