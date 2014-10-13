package com.sweetieframework.network;

import com.sweetieframework.SweetieApp;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import java.net.SocketAddress;
import java.util.logging.Logger;

public class HttpServer {

    private static final Logger log = Logger.getLogger(HttpServer.class.getName());
    private final SocketAddress addr;
    private final SweetieApp server;
 
    public HttpServer(SocketAddress addr, SweetieApp server) {
        this.addr = addr;
        this.server = server;
    }
    
    public void start() throws Exception {
        log.info("Starting up HTTP server");
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new HttpServerInitializer(server));
            Channel ch = b.bind(addr).sync().channel();
            ch.closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
