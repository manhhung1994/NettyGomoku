package AccountList;

import java.util.Hashtable;

public class DefaultAccountList implements AccountList{
	

	public  Hashtable<Integer, Account> ALL_ACCOUNT = new Hashtable<Integer, Account>();
	
	public DefaultAccountList(Hashtable<Integer, Account> map)
	{
		ALL_ACCOUNT = map;
		ALL_ACCOUNT.put(1, new DefaultAccount(1,null, "admin1", "admin"));
		ALL_ACCOUNT.put(2, new DefaultAccount(2,null, "admin2", "admin"));
		ALL_ACCOUNT.put(3, new DefaultAccount(3,null, "admin3", "admin"));
		ALL_ACCOUNT.put(4, new DefaultAccount(4,null, "admin4", "admin"));

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
