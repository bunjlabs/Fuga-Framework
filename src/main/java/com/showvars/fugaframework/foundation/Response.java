package com.showvars.fugaframework.foundation;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Map;
import java.util.TreeMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class Response {

    private final Logger log = LogManager.getLogger(Response.class);

    private int status;
    private InputStream stream;
    private String contentType;
    private long contentLength;
    private Map<String, String> headers = new TreeMap<>();

    public Response() {
        contentType = "application/octet-stream";   // or anything else
        contentLength = -1;
    }

    public Response(InputStream is) {
        this();
        stream = is;
    }

    public Response(byte[] bytes) {
        this(new ByteArrayInputStream(bytes));
        contentLength = bytes.length;
    }

    public Response(String s) {
        this(s.getBytes());
        contentType = "text/html";  // I assume that text/html will be more often
    }

    public Response(File f) throws IOException {
        this(new FileInputStream(f));
        contentType = Files.probeContentType(f.toPath());
        contentLength = f.length();
    }

    public Response setStatus(int status) {
        this.status = status;
        return this;
    }

    public Response setContentType(String contentType) {
        this.contentType = contentType;
        return this;
    }

    public Response setContentLength(long contentLength) {
        this.contentLength = contentLength;
        return this;
    }

    public Response setHeader(String name, String value) {
        headers.put(name, value);
        return this;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public int getStatus() {
        return status;
    }

    public InputStream getStream() {
        return stream;
    }

    public String getContentType() {
        return contentType;
    }

    public long getContentLength() {
        return contentLength;
    }

    public Response asText() {
        this.contentType = "text/plain";
        return this;
    }

    public Response asHtml() {
        this.contentType = "text/html";
        return this;
    }

    public Response asJson() {
        this.contentType = "application/json";
        return this;
    }

    public Response asXml() {
        this.contentType = "text/xml";
        return this;
    }

    public Response asJavascript() {
        this.contentType = "application/javascript";
        return this;
    }

    public Response asCss() {
        this.contentType = "text/css";
        return this;
    }
}
