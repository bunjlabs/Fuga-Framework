package com.bunjlabs.fugaframework.foundation;

import java.io.InputStream;
import java.nio.charset.Charset;

public interface Content {

    public String asString();

    public String asString(Charset charset);

    public InputStream asInputStream();

    public long getLength();

}
