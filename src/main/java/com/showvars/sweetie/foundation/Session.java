package com.showvars.sweetie.foundation;

import java.util.Date;
import java.util.HashMap;

public class Session extends HashMap<String, Object> {

    private long updateTime;
    private int timeout = 30; // 30 minutes

    public Session() {
        updateTime = new Date().getTime();
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
}
