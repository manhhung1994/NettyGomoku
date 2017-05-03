package Session;

public interface RoomSession {
	enum Status
	{
		FULL,  EMPTY
	}
	public void addPlayerSession(PlayerSession playerSession);
	public void removePlayerSession(PlayerSession playerSession);
	public void setStatus(Status status);
	public Status getStatus();
}
