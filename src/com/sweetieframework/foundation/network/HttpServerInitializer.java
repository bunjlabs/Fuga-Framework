/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sweetieframework.foundation.network;

import com.sweetieframework.SweetieServer;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;


class HttpServerInitializer extends ChannelInitializer<SocketChannel> {
    private final SweetieServer server;
    
    HttpServerInitializer(SweetieServer server) {
        this.server = server;
    }
    
    @Override
    public void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline p = ch.pipeline();
        p.addLast("decoder", new HttpRequestDecoder());
        p.addLast("encoder", new HttpResponseEncoder());
        p.addLast("handler", new HttpServerHandler(server));
    }
}
