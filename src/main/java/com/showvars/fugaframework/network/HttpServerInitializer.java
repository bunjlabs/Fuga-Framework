package com.showvars.fugaframework.network;

import com.showvars.fugaframework.FugaApp;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.stream.ChunkedWriteHandler;

class HttpServerInitializer extends ChannelInitializer<SocketChannel> {

    private final FugaApp server;

    HttpServerInitializer(FugaApp server) {
        this.server = server;
    }

    @Override
    public void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline p = ch.pipeline();

        p.addLast(new HttpRequestDecoder());
        p.addLast(new HttpResponseEncoder());
        p.addLast(new ChunkedWriteHandler());
        
        p.addLast(new HttpServerHandler(server));

    }
}
