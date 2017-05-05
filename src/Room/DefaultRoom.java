package Room;

import AccountList.Account;
import Server.Object.TCPServerHandler;
import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;

public class DefaultRoom  implements Room{
	public ChannelGroup channelGroup ;
	public int[][] matrix ;
	public DefaultRoom(ChannelGroup channelGroup, int[][] matrix) {
		this.channelGroup = channelGroup;
		this.matrix = matrix;
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
	public void removePlayer(Account account) {
		this.channelGroup.remove(account.getChannel());
		account.setRoom(0);
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
		return TCPServerHandler.All_ACCOUNT.getAcc(id);
	}
	
	@Override
	public ChannelGroup getChannelGroup() {
		return this.channelGroup;
	}
	@Override
	public void setFlag(boolean isRoot,int row, int col) {
		if(isRoot)
			matrix[row][col] = 1;
		else 
			matrix[row][col] =	2;
		
	}
	@Override
	public boolean checkWin(boolean isRoot,int row, int col) {
		if(isRoot)
		{
			if(matrix[0][0] == 1 && matrix[0][1] == 1) return true;
			else return false;
		}
		else {
			if(matrix[14][0] == 2 && matrix[14][1] == 2) return true;
			else return false;
		}
	}

	



}
