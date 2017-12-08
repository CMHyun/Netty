package Echo;

import java.nio.charset.Charset;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;

public class EchoServerHandler extends ChannelInboundHandlerAdapter{

	// 데이터 수신 이벤트 처리 메소드다. 클라이언트로부터 데이터의 수신이 이루어졌을 때 네티가 자동으로 호출하는 이벤트 메소드.
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		
		// 수신된 데이터를 가지고 있는 네티의 바이트 버퍼 객체로부터 문자열 데이터를 읽어온다.
		String readMessage = ((ByteBuf)msg).toString(Charset.defaultCharset());
		
		// 수신된 문자열을 콘솔로 출력한다.
		System.out.println("수신된 문자열: " + readMessage);
		
		// ctx는 ChannelHandlerContext Interface의 Object로서 Channel Pipeline에 대한 Event를 처리한다. 
		// 여기서는 Server에 연결된 Client Channel로 입력받은 Data를 그대로 전송한다.
		// ( Client Channel은 개념적으로 일반적인 Network Program에서의 Socket으로 이해하면 쉽다. )
		ctx.write(msg);
	}

	// ChannelRead Event의 처리가 완료된 후 자동으로 수행되는 Event Method로서 Channel Pipeline에 저장된 Buffer를 전송하는 flush() Method를 호출한다.
	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		ctx.flush();
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}
}
