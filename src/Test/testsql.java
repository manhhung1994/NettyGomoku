package Test;

import java.util.Hashtable;

import AccountList.Account;
import AccountList.AccountList;
import AccountList.DefaultAccountList;
import Database.DefaultSQL;
import Database.MySQL;

public class testsql {
	public static MySQL myDatabase = new DefaultSQL();
	
	public static void main(String[] args) {
		
		//myDatabase.setOnline(5, true);
		myDatabase.resetAll();
	}

}
