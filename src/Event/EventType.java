package Event;

public class EventType {

	public final static byte CONNECT = 0x01;
	public final static byte CONNECT_SUCCESS = 0x02;

	public static final byte RECONNECT = 0x3;
	public final static byte CONNECT_FAILED = 0x04;


	public static final byte LOG_IN = 0x05;
	public static final byte LOG_OUT = 0x06;
	public static final byte LOG_IN_SUCCESS = 0x07;
	public static final byte LOG_IN_FAILURE = 0x08;
	public static final byte LOG_OUT_SUCCESS = 0x09;
	public static final byte LOG_OUT_FAILURE = 0x0a;

	// Metadata events
	public static final byte GAME_LIST 			= 0x0b;
	public static final byte ROOM_LIST 			= 0x0c;
	public static final byte GAME_ROOM_JOIN 	= 0x0d;
	public static final byte GAME_ROOM_LEAVE 	= 0x10;
	public static final byte ROOM_LEAVE_SUCCESS = 0x11;
	public static final byte ENEMY_LEAVE 		= 0x12;
	public static final byte GAME_ROOM_JOIN_SUCCESS = 0x13;
	public static final byte GAME_ROOM_JOIN_FAILURE = 0x14;


	public static final byte READY 			= 0x15;
	public static final byte READY_SUCCESS 	= 0x16;

	public static final byte POSSITION 		= 0x17;
	public static final byte CHECKWIN 		= 0x18;
	

	public static final byte ENEMYDATA 			= 0x19;
	public static final byte ENEMY_JOIN_ROOM 	= 0x1a;
	public static final byte PLAYER2_READY 		= 0x1b;
	public static final byte PLAYER2_JOINROOM 	= 0x1c;

	public static final byte DISCONNECT 		= 0x1d;

	public static final byte REGISTER 			= 0x20;
	public static final byte REGISTER_SUCCESS 	= 0x21;
	public static final byte REGISTER_FAILURE 	= 0x22;


}
