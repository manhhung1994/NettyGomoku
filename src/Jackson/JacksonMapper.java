package Jackson;

import org.codehaus.jackson.map.ObjectMapper;


public class JacksonMapper {

	private static final ObjectMapper MAPPER = new ObjectMapper();

	/**
	 *  create once, reuse
	 * @return ObjectMapper å�•ä¾‹
	 */
	public static ObjectMapper getInstance() {

		return MAPPER;
	}

}
