package com.bunjlabs.fugaframework.network;

import com.bunjlabs.fugaframework.FugaApp;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class HttpServer {

    private static final Logger log = LogManager.getLogger(HttpServer.class);
    private final SocketAddress addr;
    private final FugaApp app;

    public HttpServer(SocketAddress addr, FugaApp app) {
        this.addr = addr;
        this.app = app;
    }

    public void start() throws Exception {
        if (addr instanceof InetSocketAddress) {
            InetSocketAddress inetAddr = (InetSocketAddress) addr;
            log.info("Starting up HTTP server at {}{}{}/",
                    "http://",
                    inetAddr.getHostString(),
                    inetAddr.getPort() == 80 ? "" : ":" + inetAddr.getPort());
        } else {
            log.info("Starting up HTTP server");
        }

        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new HttpServerInitializer(app));
            Channel ch = b.bind(addr).sync().channel();
            ch.closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
