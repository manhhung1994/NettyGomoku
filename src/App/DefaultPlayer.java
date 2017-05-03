package App;

import io.netty.channel.Channel;

public class DefaultPlayer implements Player{

	public int id;
	public String name;
	public Channel channel;
	
	public DefaultPlayer()
	{
		
	}
	@Override
	public void setID(int id) {
		this.id = id;
		
	}

	@Override
	public int getID() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setName(String name) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setChannel(Channel channel) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Channel getChannel() {
		// TODO Auto-generated method stub
		return null;
	}

}
