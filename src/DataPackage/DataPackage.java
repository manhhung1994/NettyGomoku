package DataPackage;

import java.util.Vector;

public class DataPackage {

	public static String CheckTypeData(String data)
	{
		String[] temp = data.split("-");
		return temp[0];
	}
	public static int RoomChoose(String data)
	{
		String[] temp = data.split("-");
		return Integer.parseInt(temp[1]);
	}
	
}
