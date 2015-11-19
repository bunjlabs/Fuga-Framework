package com.bunjlabs.fugaframework.network;

import com.bunjlabs.fugaframework.FugaApp;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.stream.ChunkedWriteHandler;

class HttpServerInitializer extends ChannelInitializer<SocketChannel> {

    private final FugaApp app;

    HttpServerInitializer(FugaApp app) {
        this.app = app;
    }

    @Override
    public void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline p = ch.pipeline();

        p.addLast(new HttpRequestDecoder());
        p.addLast(new HttpResponseEncoder());
        p.addLast(new ChunkedWriteHandler());
        
        p.addLast(new HttpServerHandler(app));

    }
}
