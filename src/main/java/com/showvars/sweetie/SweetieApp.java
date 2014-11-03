package com.showvars.sweetie;

import com.showvars.sweetie.network.HttpServer;
import com.showvars.sweetie.router.Router;
import com.showvars.sweetie.templates.TemplateEngine;
import io.netty.handler.codec.http.Cookie;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SweetieApp {

    private final Router hmap = new Router();
    private final SocketAddress addr;
    private final Map<UUID, HashMap<String, Cookie>> sessions;
    private HttpServer httpserver;
    private final TemplateEngine templateEngine = new TemplateEngine();

    private SweetieApp(SocketAddress addr) {
        this.addr = addr;
        sessions = new HashMap<>();
    }

    public Router getRouter() {
        return hmap;
    }


    public void getStarted() throws Exception {
        httpserver = new HttpServer(addr, this);
        httpserver.start();
    }
    
    public Map<String, Cookie> getSession(UUID uuid) {
        return sessions.get(uuid);
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
