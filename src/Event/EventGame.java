package Event;

public class EventGame {
	private int eventType;
	private String data;
	/*public EventGame(int eventType,String data)
	{
		this.eventType = eventType;
		this.data =data;
	}
	public EventGame(int eventType)
	{
		this.eventType = eventType;
		this.data =null;
	}*/
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public int getEventType() {
		return eventType;
	}
	public void setEventType(int eventType) {
		this.eventType = eventType;
	}
	
	
}
