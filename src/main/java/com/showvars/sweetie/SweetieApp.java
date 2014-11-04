package com.showvars.sweetie;

import com.showvars.sweetie.foundation.Session;
import com.showvars.sweetie.network.HttpServer;
import com.showvars.sweetie.router.Router;
import com.showvars.sweetie.templates.TemplateApi;
import com.showvars.sweetie.templates.TemplateEngine;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SweetieApp {

    private final Router hmap = new Router();
    private final SocketAddress addr;
    private final Map<UUID, Session> sessions;
    private HttpServer httpserver;
    private final TemplateEngine templateEngine = new TemplateEngine(new TemplateApi(this));

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
    
    public Session getSession(UUID uuid) {
        return sessions.get(uuid);
    }
    
    public void putSession(UUID uuid, Session session) {
        sessions.put(uuid, session);
    }
    
    public TemplateEngine getTemplateEngine() {
        return templateEngine;
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
