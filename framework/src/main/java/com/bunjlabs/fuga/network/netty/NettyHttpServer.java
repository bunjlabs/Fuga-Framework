/* 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.bunjlabs.fuga.network.netty;

import com.bunjlabs.fuga.FugaApp;
import com.bunjlabs.fuga.network.HttpServer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class NettyHttpServer implements HttpServer {

    private final Logger log = LogManager.getLogger(NettyHttpServer.class);
    private final SocketAddress addr;
    private final FugaApp app;

    public NettyHttpServer(SocketAddress addr, FugaApp app) {
        this.addr = addr;
        this.app = app;
    }

    @Override
    public void start() {
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
                    .childHandler(new NettyHttpServerInitializer(app));
            Channel ch = b.bind(addr).sync().channel();
            ch.closeFuture().sync();
        } catch (Exception ex) {
            log.error(ex);
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
