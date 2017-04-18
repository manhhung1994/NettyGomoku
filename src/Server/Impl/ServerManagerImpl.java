package Server.Impl;

import java.util.HashSet;
import java.util.Set;

import Context.AppContext;

public class ServerManagerImpl implements ServerManager {
	private Set<AbstractNettyServer> servers;
	
	public ServerManagerImpl()
	{
		servers = new HashSet<AbstractNettyServer>();
	}
	@Override
	public void startServer(int tcpPort) throws Exception {
		if(tcpPort >0 )
		{
			
		}
		
	}

	@Override
	public void stropServer() throws Exception {
		for(AbstractNettyServer nettyServer: servers){
			try
			{
				nettyServer.stopServer();
			}
			catch (Exception e)
			{
				System.out.println("Unable to stop server {} due to error {}");
				throw e;
			}
		}
		
	}

}
