package Echo;

import java.nio.charset.Charset;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class EchoClientHandler extends ChannelInboundHandlerAdapter {
	
	/* ChannelActive Event는 ChannelInboundHandler에 정의된 Event로써 Socket Channel이
	   최초 활성화 되었을 때 실행된다.	 
	*/	
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		
		String sendMessage = "Hello, Netty!";
		
		ByteBuf messageBuffer = Unpooled.buffer();
		messageBuffer.writeBytes(sendMessage.getBytes());
		
		StringBuilder builder = new StringBuilder();
		builder.append("전송할 문자열 [");
		builder.append(sendMessage);
		builder.append("]");
		
		System.out.println(builder.toString());
		
		/* writeAndFlush() Method는 내부적으로 데이터 기록과 전송의 두 가지 Method를 호출한다.
		   첫 번째 Channel에 Data를 기록하는 write() Method며, 두 번째 Channel에 기록된 Data를 
		   Server로 전송하는 flush() Method이다. 
		*/
		ctx.writeAndFlush(messageBuffer);
	}
	
	// Server로부터 수신된 Data가 있을 때 호출되는 Netty Event Method이다.
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		
		// Server로 부터 수신된 Data가 저장된 msg Object에서 문자열 Data를 추출한다.
		String readMessage = ((ByteBuf)msg).toString(Charset.defaultCharset());
		
		StringBuilder builder = new StringBuilder();
		builder.append("수신할 문자열 [");
		builder.append(readMessage);
		builder.append("]");
		
		System.out.println(builder.toString());		
	}
	
	/* 수신된 Data를 모두 읽었을 때 호출되는 Event Method이다. channelRead() Method의 수행이 완료되고
	   나서 자동으로 호출된다.
	*/
	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		/* 수신된 Data를 모두 읽은 후 Server와 연결된 Channel을 닫는다. 이후 Data 송수신 Channel은 닫히게 되고
		   Client Program은 종료된다.		
		*/
		ctx.close();
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}
	
}
