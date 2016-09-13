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
package com.bunjlabs.fuga.foundation;

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

    /**
     *
     */
    public Response() {
        contentType = "application/octet-stream";
        contentLength = -1;
    }

    /**
     *
     * @param is
     */
    public Response(InputStream is) {
        contentType = "application/octet-stream";
        contentLength = -1;
        stream = is;
    }

    /**
     *
     * @param bytes
     */
    public Response(byte[] bytes) {
        stream = new ByteArrayInputStream(bytes);
        contentType = "application/octet-stream";
        contentLength = bytes.length;
    }

    /**
     *
     * @param f
     * @throws IOException
     */
    public Response(File f) throws IOException {
        stream = new FileInputStream(f);
        contentType = Files.probeContentType(f.toPath());
        contentLength = f.length();
    }

    /**
     *
     * @param s
     */
    public Response(String s) {
        this(s.getBytes());
        contentType = "text/html";
    }

    /**
     *
     * @param status
     * @return
     */
    public Response status(int status) {
        this.status = status;
        return this;
    }

    /**
     *
     * @param contentLength
     * @return
     */
    public Response length(long contentLength) {
        this.contentLength = contentLength;
        return this;
    }

    /**
     *
     * @param name
     * @param value
     * @return
     */
    public Response header(String name, String value) {
        headers.put(name, value);
        return this;
    }

    /**
     *
     * @return
     */
    public Map<String, String> getHeaders() {
        return headers;
    }

    /**
     *
     * @return
     */
    public int getStatus() {
        return status;
    }

    /**
     *
     * @return
     */
    public InputStream getStream() {
        return stream;
    }

    /**
     *
     * @return
     */
    public String getContentType() {
        return contentType;
    }

    /**
     *
     * @return
     */
    public long getContentLength() {
        return contentLength;
    }

    /**
     *
     * @return
     */
    public Response asText() {
        this.contentType = "text/plain";
        return this;
    }

    /**
     *
     * @return
     */
    public Response asHtml() {
        this.contentType = "text/html";
        return this;
    }

    /**
     *
     * @return
     */
    public Response asJson() {
        this.contentType = "application/json";
        return this;
    }

    /**
     *
     * @return
     */
    public Response asXml() {
        this.contentType = "text/xml";
        return this;
    }

    /**
     *
     * @return
     */
    public Response asJavascript() {
        this.contentType = "application/javascript";
        return this;
    }

    /**
     *
     * @return
     */
    public Response asCss() {
        this.contentType = "text/css";
        return this;
    }

    /**
     *
     * @param contentType
     * @return
     */
    public Response as(String contentType) {
        this.contentType = contentType;
        return this;
    }

    /**
     *
     * @return
     */
    public boolean isEmpty() {
        return stream == null;
    }
}
