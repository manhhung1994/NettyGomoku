package Room;

import AccountList.Account;

import io.netty.channel.group.ChannelGroup;

public interface Room {
	
	public boolean isFull();
	public void addPlayer(Account account);
	public void removePlayer(Account account);
	public int getSize();
	public void setSize(int size);
	public Account getPlayer(int id);
	public ChannelGroup getChannelGroup();
	public void setFlag(boolean isRoot,int row	, int col);
	public boolean checkWin(boolean isRoot,int row,int col);
	public void resetMatrix();
}
