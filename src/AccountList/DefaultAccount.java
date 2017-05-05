package AccountList;

import Event.Status;
import io.netty.channel.Channel;

public class DefaultAccount  implements Account{
	private int id;
	private String name;
	private String pass;
	private Channel channel;
	private int room;
	private boolean isRoot;
	private boolean isReady;
	private Status stt;
	public DefaultAccount(int id , Channel channel, String name , String pass) {
		this.id = id;
		this.channel = channel;
		this.name = name;
		this.pass = pass;
	}
	
	@Override
	public int getID() {
		return id;
	}
	@Override
	public void setID(int id) {
		this.id = id;
		
	}
	@Override
	public String getName() {
		return name;
	}
	@Override
	public void setName(String name) {
		this.name = name;
		
	}
	@Override
	public String getPass() {
		return pass;
	}
	@Override
	public void setPass(String pass) {
		this.pass = pass;
		
	}

	@Override
	public void setChannel(Channel channel) {
		this.channel = channel;		
	}

	@Override
	public Channel getChannel() {
		return this.channel;
	}

	@Override
	public void setRoom(int room) {
		this.room = room;
		
	}

	@Override
	public int getRoom() {
		return this.room;
	}

	@Override
	public boolean isRoot() {
		return this.isRoot;
	}

	@Override
	public void setRoot(boolean isRoot) {
		this.isRoot = isRoot;
		
	}

	@Override
	public void setStatus(Status stt) {
		this.stt = stt;
		
	}

	@Override
	public Status getStatus() {
		return this.stt;
	}

	@Override
	public boolean isReady() {
		return this.isReady;
	}

	@Override
	public void setReady(boolean isReady) {
		this.isReady = isReady;
		
	}
	
	
	
	
}
