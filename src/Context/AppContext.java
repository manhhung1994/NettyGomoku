package Context;

import org.springframework.context.ApplicationContext;


public class AppContext {
	// Servers
		public static final String SERVER_MANAGER = "serverManager";
		public static final String TCP_SERVER = "tcpServer";
		public static final String UDP_SERVER = "udpServer";
		public static final String FLASH_POLICY_SERVER = "flashPolicyServer";
		//Room
		public static final String[] ROOM_NAME = { 
				"Hải Dương Room 1", 
				"Hải Dương Room 2", 
				"Hải Dương Room 3", 
				"Bắc Giang Room 1",
				"Bắc Giang Room 2", 
				"Bắc Giang Room 3" };
		private static ApplicationContext applicationContext;
		
		public static Object getBean(String beanName)
		{
			if (null == beanName)
			{
				return null;
			}
			return applicationContext.getBean(beanName);
		}
}
