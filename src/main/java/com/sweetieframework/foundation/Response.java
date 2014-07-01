package com.sweetieframework.foundation;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

public class Response {

    private final ResponseType type;
    private int status;
    private InputStream stream;
    private String contentType;
    private long contentLength;

    public Response() {
        type = ResponseType.Null;
        status = 200;
        contentType = "application/octet-stream";   // or anything else
        contentLength = 0;
    }

    public Response(String s) {
        type = ResponseType.Text;
        status = 200;
        stream = new ByteArrayInputStream(s.getBytes());
        contentType = "text/html";  // we assume that the html will be more often
        contentLength = s.length();
    }

    public Response(File f) {
        type = ResponseType.File;
        status = 200;
        try {
            stream = new FileInputStream(f);
            contentType = Files.probeContentType(f.toPath());
            contentLength = f.length();
        } catch (FileNotFoundException ex) {
            // TODO: file not found exception catching
        } catch (IOException ex) {
            // TODO: and IO exception too
        }
    }

    public Response(InputStream is) {
        type = ResponseType.Stream;
        status = 200;
        stream = is;
        contentType = "application/octet-stream";
        contentLength = -1;
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

    public ResponseType getResponseType() {
        return type;
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

}
