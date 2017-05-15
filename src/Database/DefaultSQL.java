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
			
		}
		return 0;
	}
	@Override
	public String getName(int id) {
		try {
			myStatement = myConnection.createStatement();
			String sql = "select *from user where id ='" + id +"'";
			myRs = myStatement.executeQuery(sql);
			while(myRs.next())
			{
				return myRs.getString("username");
			}
			
		} catch (Exception e) {
			
		}
		return "";
	}
	@Override
	public String getPass(int id) {
		try {
			myStatement = myConnection.createStatement();
			String sql = "select *from user where id ='" + id +"'";
			myRs = myStatement.executeQuery(sql);
			while(myRs.next())
			{
				return myRs.getString("password");
			}
			
		} catch (Exception e) {
			
		}
		return "";
	}
	@Override
	public String getNickName(int id) {
		try {
			myStatement = myConnection.createStatement();
			String sql = "select *from user where id ='" + id +"'";
			myRs = myStatement.executeQuery(sql);
			while(myRs.next())
			{
				return myRs.getString("nickname");
			}
			
		} catch (Exception e) {
			
		}
		return "";
	}
	@Override
	public boolean setOnline(int id,boolean isOline) {

		try {	
			int result =0;
			myStatement = myConnection.createStatement();
			if(isOline)
			{
				result = myStatement.executeUpdate("update user "+" set online ='online'"+" where id = " + id);
				
			}
			else
			{
				result = myStatement.executeUpdate("update user "+" set online ='offline'"+" where id = " + id);
			}
			if(result==1) return true;
			else return false;
			
		} catch (Exception e) {
			
		}
		return false;
	}
	@Override
	public boolean getOnline(int id) {

		try {
			myStatement = myConnection.createStatement();
			String sql = "select *from user where id ='" + id +"'";
			myRs = myStatement.executeQuery(sql);
			while(myRs.next())
			{
				if(myRs.getString("online").equals("online")) return true;
				else return false;
			}
			
		} catch (Exception e) {
			
		}
		return false;
	}
	@Override
	public void resetAll() {
		try {
			
			myStatement = myConnection.createStatement();
			myRs= myStatement.executeQuery("select * from user");		
			while(myRs.next())
			{				
				myStatement.executeUpdate("update user set online ='offline'");
			}
			
		} catch (Exception e) {
			
		}
		
	}
	@Override
	public int getScore(int id) {
		try {
			myStatement = myConnection.createStatement();
			String sql = "select *from user where id ='" + id +"'";
			myRs = myStatement.executeQuery(sql);
			while(myRs.next())
			{
				int score = myRs.getInt("score");
				return score;
			}
			
		} catch (Exception e) {
			
		}
		return 0;
	}
	@Override
	public void addScore(int id, int score) {
		try {
			myStatement = myConnection.createStatement();
			String sql = "select *from user where id ='" + id +"'";
			int currentScore = 0; 
			myRs = myStatement.executeQuery(sql);
			while(myRs.next())
			{
				 currentScore = myRs.getInt("score");
				
			}
			int newScore = currentScore + score;
			if(newScore < 0) newScore = 0;	
			myStatement.executeUpdate("update user "+" set score ='" + newScore +"'" +" where id = " + id);
			
		} catch (Exception e) {
			
		}
		
	}
	
	
}	
