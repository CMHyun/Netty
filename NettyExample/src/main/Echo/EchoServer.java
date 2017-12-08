package Echo;

import Discard.DiscardServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class EchoServer {

	public static void main(String[] args) throws Exception {
		
		EventLoopGroup bossGroup = new NioEventLoopGroup(1);
 		EventLoopGroup workerGroup = new NioEventLoopGroup();
 		
 		try {
 			
 			ServerBootstrap sbs = new ServerBootstrap();
 			
 			sbs.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class).childHandler(new ChannelInitializer<SocketChannel>() {
 
 				@Override
 				protected void initChannel(SocketChannel ch) throws Exception {
 					
 					ChannelPipeline pipe = ch.pipeline();
 					pipe.addLast(new EchoServerHandler());
 					
 				}
 			});
 			
 			ChannelFuture future = sbs.bind(8888).sync();
 			
 			future.channel().closeFuture().sync();
 			
 		} finally {
 			workerGroup.shutdownGracefully();
 			bossGroup.shutdownGracefully();
 		}

	}

}
