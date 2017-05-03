package Service;

import java.util.*;

import AccountList.Account;
import AccountList.AccountList;
import AccountList.DefaultAccount;
import AccountList.DefaultAccountList;
import Room.DefaultRoomFactory;
import Room.Room;
import Room.RoomFactory;
import Session.DefaultRoomSession;
import Session.PlayerSession;
import Session.RoomSession;
import io.netty.channel.Channel;
import io.netty.channel.DefaultChannelConfig;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

public class test {
	public static Hashtable<Integer, Room> roomMap = new Hashtable<Integer,Room>();
	public static RoomFactory roomFactory = new DefaultRoomFactory(roomMap);
	static Channel channel = null;
	static Channel channel2 = null;
	public static  Hashtable<Integer, Account> accMap = new Hashtable<Integer, Account>();
	public static AccountList All_ACCOUNT = new DefaultAccountList(accMap);
  public static void main(String args[]) { 
	  System.out.println(roomMap.size());
		
	  roomFactory.newRoom(5, new DefaultChannelGroup(GlobalEventExecutor.INSTANCE));

	  
	  System.out.println(roomFactory.getRoom(2).getChannel());
		System.out.println(roomMap.get(2).getChannel());
	  
	  
  }
} 