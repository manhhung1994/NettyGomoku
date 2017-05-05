package Event;

public class EventType {

	/**
	 * Events should <b>NEVER</b> have this type. But event handlers can choose
	 * to have this type to signify that they will handle any type of incoming
	 * event. For e.g. {@link DefaultSessionEventHandler}
	 */

	// Lifecycle events.
	public final static byte CONNECT = 0x01;
	public final static byte CONNECT_SUCCESS = 0x02;
	/**
	 * Similar to LOG_IN but parameters are different. This event is sent from
	 * client to server.
	 */
	public static final byte RECONNECT = 0x3;
	public final static byte CONNECT_FAILED = 0x06;
	/**
	 * Event used to log in to a server from a remote client. Example payload
	 * will be <b>login opcode 0x08-protocl version 0x01- username as string
	 * bytes- password as string bytes - connection key as string bytes -
	 * optional udp client address as bytes</b>
	 */

	public static final byte LOG_IN = 0x08;
	public static final byte LOG_OUT = 0x0a;
	public static final byte LOG_IN_SUCCESS = 0x0b;
	public static final byte LOG_IN_FAILURE = 0x0c;
	public static final byte LOG_OUT_SUCCESS = 0x0e;
	public static final byte LOG_OUT_FAILURE = 0x0f;

	// Metadata events
	public static final byte GAME_LIST = 0x10;
	public static final byte ROOM_LIST = 0x12;
	public static final byte GAME_ROOM_JOIN = 0x14;
	public static final byte GAME_ROOM_LEAVE = 0x15;
	public static final byte ROOM_LEAVE_SUCCESS = 0x16;
	public static final byte ENEMY_LEAVE = 0x17;
	public static final byte GAME_ROOM_JOIN_SUCCESS = 0x18;
	public static final byte GAME_ROOM_JOIN_FAILURE = 0x19;

	/**
	 * Event sent from server to client to start message sending from client to
	 * server.
	 */
	public static final byte START = 0x1a;

	/**
	 * Event sent from server to client to stop messages from being sent to
	 * server.
	 */
	public static final byte STOP = 0x1b;
	/**
	 * Incoming data from another machine/JVM to this JVM (server or client)
	 */
	public static final byte READY = 0x1c;
	public static final byte READY_SUCCESS = 0x24;
	/**
	 * This event is used to send data from the current machine to remote
	 * machines using TCP or UDP transports. It is an out-going event.
	 */
	public static final byte POSSITION = 0x1d;
	public static final byte CHECKWIN = 0x28;
	

	public static final byte ENEMYDATA = 0x30;
	public static final byte PLAYER2_READY = 0x20;
	public static final byte PLAYER2_JOINROOM = 0x26;
	/**
	 * If a remote connection is disconnected or closed then raise this event.
	 */
	public static final byte DISCONNECT = 0x22;

	/**
	 * A network exception will in turn cause this even to be raised.
	 */

}
