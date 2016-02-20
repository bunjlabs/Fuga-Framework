package com.bunjlabs.fugaframework.foundation;

import java.util.UUID;

public class Forms {

    private final Context ctx;

    public Forms(Context ctx) {
        this.ctx = ctx;
    }

    public String generateFormId(String formName) {
        String fid = UUID.randomUUID().toString();
        ctx.getSession().put("__formid__" + formName, fid);
        return fid;
    }

    public boolean testFormId(String formName, String fid) {
        Object lastfid = ctx.getSession().get("__formid__" + formName);
        if (lastfid == null) {
            return false;
        }
        ctx.getSession().remove("__formid__" + formName);
        return fid.equals((String) lastfid);
    }
}
