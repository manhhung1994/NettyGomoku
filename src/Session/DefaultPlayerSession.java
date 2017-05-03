package Session;

import io.netty.channel.Channel;

public class DefaultPlayerSession implements PlayerSession {
	
	private int id;
	private GameRoom gameRoom;
	private Channel channel;
	private State state;
	
	public DefaultPlayerSession(int id,GameRoom gameRoom, Channel channel, State state) {
		this.id = id;
		this.gameRoom = gameRoom;
		this.channel = channel;
		this.state = state;
	}
	
	@Override
	public void setID(int id) {
		this.id = id;		
	}

	@Override
	public int getID() {
		return this.id;
	}

	@Override
	public void setGameRoom(GameRoom gameRoom) {
		this.gameRoom = gameRoom;
		
	}

	@Override
	public GameRoom getGameRoom() {
		return this.gameRoom;
	}

	@Override
	public void setState(State state) {
		this.state = state;
		
	}

	@Override
	public State getState() {
		return this.state;
	}

	@Override
	public void setChannel(Channel channel) {
		this.channel = channel;
		
	}

	@Override
	public Channel getChannel() {
		return this.channel;
	}

}
