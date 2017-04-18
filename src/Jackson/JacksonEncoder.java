/**
 * 
 */
package Jackson;



import org.codehaus.jackson.map.ObjectMapper;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;


public class JacksonEncoder extends MessageToByteEncoder<Object> {

	@Override
	protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out)
			throws Exception {
        
		ObjectMapper mapper = JacksonMapper.getInstance();
        ByteBufOutputStream byteBufOutputStream = new ByteBufOutputStream(out);
        mapper.writeValue(byteBufOutputStream, msg);
	}

}
 
