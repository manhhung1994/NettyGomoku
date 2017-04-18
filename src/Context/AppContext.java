package Context;

import org.springframework.context.ApplicationContext;


public class AppContext {
	// Servers
		public static final String SERVER_MANAGER = "serverManager";
		public static final String TCP_SERVER = "tcpServer";
		public static final String UDP_SERVER = "udpServer";
		public static final String FLASH_POLICY_SERVER = "flashPolicyServer";
		
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
