package com.sweetieframework;

import com.sweetieframework.foundation.network.HttpServer;
import com.sweetieframework.handlers.Router;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;

public class SweetieApp {

    private final Router hmap = new Router();
    private final SocketAddress addr;
    private HttpServer httpserver;

    private SweetieApp(SocketAddress addr) {
        this.addr = addr;
    }

    public Router getRouter() {
        return hmap;
    }

    public void getStarted() throws Exception {
        httpserver = new HttpServer(addr, this);
        httpserver.start();
    }

    public static SweetieApp prepare() {
        SweetieApp ss = new SweetieApp(new InetSocketAddress(8080));
        return ss;
    }

    public static SweetieApp prepare(int port) {
        return new SweetieApp(new InetSocketAddress(port));
    }

    public static SweetieApp prepare(String host, int port) {
        return new SweetieApp(new InetSocketAddress(host, port));
    }

    public static SweetieApp prepare(InetAddress addr, int port) {
        return new SweetieApp(new InetSocketAddress(addr, port));
    }

    public static SweetieApp prepare(SocketAddress addr) {
        return new SweetieApp(addr);
    }
}
