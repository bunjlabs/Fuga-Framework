/* 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.bunjlabs.fugaframework.foundation;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
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
    private final Map<String, String> headers = new TreeMap<>();

    public Response() {
        contentType = "application/octet-stream";
        contentLength = -1;
    }

    public Response(InputStream is) {
        contentType = "application/octet-stream";
        contentLength = -1;
        stream = is;
    }

    public Response(byte[] bytes) {
        stream = new ByteArrayInputStream(bytes);
        contentType = "application/octet-stream";
        contentLength = bytes.length;
    }

    public Response(File f) throws IOException {
        stream = new FileInputStream(f);
        contentType = Files.probeContentType(f.toPath());
        contentLength = f.length();
    }

    public Response(String s) {
        this(s.getBytes());
        contentType = "text/html";
    }

    public Response status(int status) {
        this.status = status;
        return this;
    }

    public Response length(long contentLength) {
        this.contentLength = contentLength;
        return this;
    }

    public Response header(String name, String value) {
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

    public Response as(String contentType) {
        this.contentType = contentType;
        return this;
    }

    public boolean isEmpty() {
        return stream == null;
    }
}
