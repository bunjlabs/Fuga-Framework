package com.showvars.sweetie.foundation;

import java.util.Date;
import java.util.HashMap;

public class Session extends HashMap<String, Object> {

    private final long creationTime;
    private int timeout = 30; // 30 minutes

    public Session() {
        creationTime = new Date().getTime();
    }

    public long getCreationTime() {
        return creationTime;
    }

    public long getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }
}
