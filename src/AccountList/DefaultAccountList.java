package AccountList;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Hashtable;

import Database.MySQL;

public class DefaultAccountList implements AccountList{
	

	public  Hashtable<Integer, Account> ALL_ACCOUNT = new Hashtable<Integer, Account>();
	private Connection myConn;
	private Statement myStatement;
	private ResultSet myRs;
	public DefaultAccountList(Hashtable<Integer, Account> map)
	{
		try {
			myConn = DriverManager.getConnection(MySQL.myUrl,MySQL.user,MySQL.pass);
			myStatement = myConn.createStatement();
			myRs = myStatement.executeQuery("select * from user");
			
			while(myRs.next())
			{
				int id = myRs.getInt("id");
				String name = myRs.getString("username");
				String pass = myRs.getString("password");
				String nickname = myRs.getString("nickname");
				ALL_ACCOUNT.put(id, new DefaultAccount(id,null, name, pass,nickname));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ALL_ACCOUNT = map;
		/*
		ALL_ACCOUNT.put(1, new DefaultAccount(1,null, "admin1", "admin",""));
		ALL_ACCOUNT.put(2, new DefaultAccount(2,null, "admin2", "admin",""));
		ALL_ACCOUNT.put(3, new DefaultAccount(3,null, "admin3", "admin",""));
		ALL_ACCOUNT.put(4, new DefaultAccount(4,null, "admin4", "admin",""));
		*/
		
	}
	@Override
	public void addAcc(Account acc) {
		ALL_ACCOUNT.put(acc.getID(), acc);
		
	}
	
	@Override
	public void removeAcc(Account acc) {
		ALL_ACCOUNT.remove(acc.getID());		
	}
	@Override
	public Account getAcc(int key) {
		return ALL_ACCOUNT.get(key);
		
	}
	
}
