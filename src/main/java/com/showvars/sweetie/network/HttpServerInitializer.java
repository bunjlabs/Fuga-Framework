package com.showvars.sweetie.network;

import com.showvars.sweetie.SweetieApp;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.stream.ChunkedWriteHandler;


class HttpServerInitializer extends ChannelInitializer<SocketChannel> {
    private final SweetieApp server;
    
    HttpServerInitializer(SweetieApp server) {
        this.server = server;
    }
    
    @Override
    public void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline p = ch.pipeline();
        //p.addLast("decoder", new HttpRequestDecoder());
        //p.addLast("encoder", new HttpResponseEncoder());
        p.addLast(new HttpServerCodec());
        p.addLast(new HttpObjectAggregator(65536));
        p.addLast(new ChunkedWriteHandler());
        p.addLast(new HttpServerHandler(server));
    }
}
