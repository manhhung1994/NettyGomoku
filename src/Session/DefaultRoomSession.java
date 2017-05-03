package Session;

import java.util.Hashtable;

import AccountList.Account;
import AccountList.DefaultAccount;

public class DefaultRoomSession implements RoomSession {

	public  Hashtable<Integer, PlayerSession> PLAYER_SESSIONS = new Hashtable<Integer, PlayerSession>();
	
	private Status status;
	public DefaultRoomSession(Hashtable<Integer, PlayerSession> map)
	{
		PLAYER_SESSIONS = map;
		

	}
	@Override
	public void addPlayerSession(PlayerSession playerSession) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removePlayerSession(PlayerSession playerSession) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setStatus(Status status) {
		this.status = status;
		
	}

	@Override
	public Status getStatus() {
		return this.status;
	}
	
}
