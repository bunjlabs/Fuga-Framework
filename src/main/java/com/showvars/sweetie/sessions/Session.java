package com.showvars.sweetie.sessions;

import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

public class Session extends HashMap<String, Object> {

    private long updateTime;
    private int timeout = 30; // 30 minutes
    private final UUID sessionId;

    public Session(UUID sessionId) {
        updateTime = new Date().getTime();
        this.sessionId = sessionId;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void update() {
        updateTime = new Date().getTime();
    }

    public long getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public String getString(String name) {
        return (String) this.get(name);
    }

    public int getInteger(String name) {
        return (int) this.get(name);
    }

    public long getLong(String name) {
        return (long) this.get(name);
    }

    public boolean getBoolean(String name) {
        return (boolean) this.get(name);
    }

    public boolean test(String name, Object value) {
        Object ivalue;
        if ((ivalue = this.get(name)) == null) {
            return false;
        }
        if (!ivalue.getClass().equals(value.getClass())) {
            return false;
        }
        return ivalue.equals(value);
    }

    public UUID getSessionId() {
        return sessionId;
    }
}
