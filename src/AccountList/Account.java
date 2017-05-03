package AccountList;

import io.netty.channel.Channel;

public interface Account {

	public void 	setID(int id);
	public void 	setPass(String pass);
	public void 	setChannel(Channel channel);
	public void 	setName(String name);
	public void 	setRoom(int room);
	public int 		getRoom();
	public int 		getID();
	public String 	getName();
	public String 	getPass();
	public Channel 	getChannel();
	public boolean 	isRoot();
	public void		setRoot(boolean isRoot);
}

