package server.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import lombok.extern.slf4j.Slf4j;

@Slf4j // движок lombok генерирует код логгера
public class Server {

    public Server(){

            EventLoopGroup auth = new NioEventLoopGroup(1);
            EventLoopGroup worker = new NioEventLoopGroup();
            try {
                ServerBootstrap serverBootstrap = new ServerBootstrap();
                serverBootstrap.group(auth, worker)
                        .channel(NioServerSocketChannel.class)
                        .childHandler(new ChannelInitializer<SocketChannel>() {
                            @Override
                            protected void initChannel(SocketChannel channel) throws Exception {
                                channel.pipeline().addLast(
                                     //   new ByteInboundHandler());
                                        new ObjectEncoder(),
                                        new ObjectDecoder(ClassResolvers.cacheDisabled(null)),
                                        new MessageHandler()
                                );
                            }
                        });
                ChannelFuture channelFuture = serverBootstrap.bind(8981).sync();
                log.debug("Server started...");
                channelFuture.channel().closeFuture().sync();  //block
            } catch (Exception e) {
                log.error("  ", e);
            } finally {
                auth.shutdownGracefully();
                worker.shutdownGracefully();
            }
    }

    public static void main(String[] args) {
        new Server();
    }
}
