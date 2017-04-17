package Event;

public abstract class AbstractEventHandler implements EventHandler {
	public final int EVENT_TYPE;
	
	public AbstractEventHandler(int eventType)
	{
		this.EVENT_TYPE = eventType;
	}
	@Override
	public int getTypeEvent()
	{
		return this.EVENT_TYPE;
	}
}
