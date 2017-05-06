package Database;

public interface MySQL {
	public boolean newAcc(String userName,String passWord,String nickName);
	public int checkAcc(String userName,String pass);
	public String getName(int id);
	public String getPass(int id);
	public String getNickName(int id);
	public boolean setOnline(int id,boolean isOnline);
	public boolean getOnline(int id);
	static String myUrl = "jdbc:mysql://localhost:3306/users";
	static String user = "root";
	static String pass = "hungcuong";
	
}
