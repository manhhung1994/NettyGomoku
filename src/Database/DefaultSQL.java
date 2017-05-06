package Database;
import java.sql.*;

public class DefaultSQL implements MySQL {	
	public static Connection myConnection;
	public static Statement myStatement;
	public static ResultSet myRs; 
	public DefaultSQL() {
		try {
			myConnection =  DriverManager.getConnection(myUrl,user,pass);
			myStatement = myConnection.createStatement();
			myRs= myStatement.executeQuery("select * from user");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	@Override
	public boolean newAcc(String userName, String passWord, String nickName) {
		int id = 0;
		try {	
			myConnection =  DriverManager.getConnection(myUrl,user,pass);
			myStatement = myConnection.createStatement();
			myRs= myStatement.executeQuery("select * from user");
			while(myRs.next())
			{
				if(myRs.getString("username").equals(userName))
					return false;
				id++;				
			}
			id ++;
			String values  = " values ('" +id+"'," + "'" + userName +"'," + 
								 "'" + passWord +"'," +  "'" + nickName +"','offline')" ;
			System.out.println(values);
			int result = myStatement.executeUpdate("insert into user " 
					+" (id, username, password,nickname,online)"
					+	values);
			if(result == 1) return true;
			else return false;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return false;
	}
	@Override
	public int checkAcc(String userName, String pass) {
		try {
			myRs= myStatement.executeQuery("select * from user");
			while(myRs.next())
			{
				if(myRs.getString("username").equals(userName)&& 
						myRs.getString("password").equals(pass))
					return myRs.getInt("id");
								
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return 0;
	}
	@Override
	public String getName(int id) {
		int count = 0;
		try {
			myRs= myStatement.executeQuery("select * from user");
			while(myRs.next())
			{
				count ++;
				if(myRs.getInt("id") == count)
					return myRs.getString("username");
								
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return "";
	}
	@Override
	public String getPass(int id) {
		int count = 0;
		try {
			myRs= myStatement.executeQuery("select * from user");
			while(myRs.next())
			{
				count ++;
				if(myRs.getInt("id") == count)
					return myRs.getString("password");
								
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return "";
	}
	@Override
	public String getNickName(int id) {
		int count = 0;
		try {
			myRs= myStatement.executeQuery("select * from user");
			while(myRs.next())
			{
				count ++;
				if(myRs.getInt("id") == count)
					return myRs.getString("nickname");
								
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return "";
	}
	@Override
	public boolean setOnline(int id,boolean isOline) {

		try {
			String edit;
			myConnection =  DriverManager.getConnection(myUrl,user,pass);
			myStatement = myConnection.createStatement();
			myRs= myStatement.executeQuery("select * from user");
			if(isOline)
			 edit= "update user "
					+" set online ='online'"
					+" where id = " + id;
			else edit= "update user "
					+" set online ='offline'"
					+" where id = " + id;
			int result = myStatement.executeUpdate(edit);
			
			if(result ==1 ) return true;
			else return false;
		} catch (Exception e) {
			// TODO: handle exception
		}
		return false;
	}
	@Override
	public boolean getOnline(int id) {
		int count = 0;
		try {
			myRs= myStatement.executeQuery("select * from user");
			while(myRs.next())
			{
				count ++;
				if(myRs.getInt("id") == count)
					System.out.println( myRs.getString("nickname"));
								
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return false;
	}
	
	
}	
