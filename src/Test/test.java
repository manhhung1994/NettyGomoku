package Test;

import java.sql.*;
public class test {
	
  public static void main(String args[]) { 
	  String myUrl = "jdbc:mysql://localhost:3306/users";
	  String user = "root";
	  String pass = "hungcuong";
	  int count =0;
	  try {
		Connection myCon= DriverManager.getConnection(myUrl,user,pass);
		
		Statement mySta = myCon.createStatement();
		/*
		 * insert user
		 */
		
		String insert = "insert into user " 
					+" (id, username, password,nickname)"
				+" values ('16', 'hungtm', '123', 'MạnhHưng')";
		String edit = "update user "
				+" set online ='online'"
				+" where id = 3";
	
		String del = "delete from user where iduser =1";
		
		String exit = "where username = 'hungtm'";
		          
		int result = mySta.executeUpdate(edit);
		System.out.println(result);
			
		
		ResultSet myRs = mySta.executeQuery("select * from user");
		
		
		while(myRs.next())
		{
			System.out.println(myRs.getString("iduser")+myRs.getString("username") + myRs.getString("nickname"));
			count++;
		}
		
	} catch (Exception e) {
		
	}
  }
  public void InsertAcc(String username,String pass , String nickname)
  {
	  
  }
} 