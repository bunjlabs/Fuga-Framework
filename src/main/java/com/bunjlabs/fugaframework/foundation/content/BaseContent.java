package com.bunjlabs.fugaframework.foundation.content;

import com.bunjlabs.fugaframework.foundation.Content;
import org.json.JSONObject;
import org.json.JSONTokener;

public abstract class BaseContent implements Content {

    public JSONObject asJson() {
        return new JSONObject(new JSONTokener(this.asInputStream()));
    }
}
