package Echo;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

public class EchoClient {

	public static void main(String[] args) throws Exception {
		
		EventLoopGroup ELGroup = new NioEventLoopGroup();
		
		try {
			Bootstrap b = new Bootstrap();
			
			/*Server Application의 BootStrap 설정과 다르게 Event Loop Group이 하나만 설정되었다.
			  Client Application은 Server와 달리 Server에 연결된 Channel 하나만 존재하기 때문에
			  Event Loop Group이 하나이다.
			*/
			b.group(ELGroup)
			/* Client Application이 생성하는 Channel의 종류를 설정한다. 여기서는 NIO Socket Channel인
			   NioSocketChannel Class를 설정했다. 즉 Server에 연결된 Client의 Socket Channel은 NIO로 
			   동작하게 된다.
			*/			
			.channel(NioSocketChannel.class)
			
			//Client Application이므로 Channel Pipeline의 설정에 일반 Socket Channel Class인 SocketChannel을 설정한다.
			.handler(new ChannelInitializer<Channel>() {
				@Override
				protected void initChannel(Channel ch) throws Exception {
					ChannelPipeline p = ch.pipeline();
					p.addLast(new EchoClientHandler());					
				}				
			});
			
			/* Server에서는 접속 경로인 Port를 지정해는 bind() Method를 사용했다면 Client에서는 connect() Method로 접속할 ip와 port를 통해 Server에 접속해준다.
			 
			 * 비동기 입출력 Method인 connect를 호출한다. connect() Method는 Method의 호출 결과로 ChannleFuture 객체를 돌려주는데,
			   이 Object를 통해서 비동기 Method의 처리 결과를 확인할 수 있다.
			   ChannleFuture Object의 sync() Method는 ChannelFuture Object의 요청이 완료될 때까지 대기한다. 단,
			   요청이 실패하면 예외를 던진다. 즉 connect() Method의 처리가 완료될 때까지 다음 Line으로 진행하지 않는다.
			*/
			
			ChannelFuture future = b.connect("localhost", 8888).sync();
			
			future.channel().closeFuture().sync();
			
		} finally {
			ELGroup.shutdownGracefully();
		}
	}
}
