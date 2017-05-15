package Room;



public interface RoomFactory {
	
	public void newRoom(int id);
	public void removeRoom(int id);
	public Room getRoom(int room);
	public boolean contains(int id);
}
