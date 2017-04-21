package Server.Object;

import Event.EventGame;
import io.netty.channel.Channel;

public interface Server {
	public void startServer(int port) throws Exception;
	public void stopServer() throws Exception;
}
