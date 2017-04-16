package Server;

import io.netty.channel.Channel;

/*
 * @author : hungtm
 * @date 16/04/2017
 */
public class PlayerData {
	private String name;
	private Channel channel ;
	private int roomNum;
	public PlayerData(Channel channel,String name,int roomNum)
	{
		this.setChannel(channel);
		this.setName(name);
		this.setRoomNum(roomNum);
	}
	public Channel getChannel() {
		return channel;
	}
	public void setChannel(Channel channel) {
		this.channel = channel;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getRoomNum() {
		return roomNum;
	}
	public void setRoomNum(int roomNum) {
		this.roomNum = roomNum;
	}
	
	
}
