package com.bunjlabs.fugaframework.foundation;

import java.io.InputStream;
import java.nio.charset.Charset;
import org.json.JSONObject;

public interface Content {

    public String asString();

    public String asString(Charset charset);

    public JSONObject asJson();

    public InputStream asInputStream();

    public long getLength();

}
