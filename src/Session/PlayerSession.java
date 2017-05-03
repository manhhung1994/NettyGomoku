package Session;

import io.netty.channel.Channel;

public interface PlayerSession {
	enum State
	{
		MY_TURN, ENEMY_TURN, READY , WIN, GAMEOVER
	}
	public void setID(int id);
	public int getID();
	public void setChannel(Channel channel);
	public void setGameRoom(GameRoom gameRoom);
	public GameRoom getGameRoom();
	public void setState(State state);
	public State getState();
	
}
