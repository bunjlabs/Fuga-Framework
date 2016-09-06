package com.bunjlabs.fuga.foundation.content;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import org.apache.commons.io.IOUtils;

public class InputStreamContent extends BaseContent {

    private final InputStream is;
    private final long length;

    public InputStreamContent(InputStream is, long length) {
        this.is = is;
        this.length = length;
    }

    @Override
    public String asString() {
        return asString(StandardCharsets.UTF_8);
    }

    @Override
    public String asString(Charset charset) {
        try {
            return IOUtils.toString(is, charset);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public InputStream asInputStream() {
        return is;
    }

    @Override
    public long getLength() {
        return length;
    }

}
