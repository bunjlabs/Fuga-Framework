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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

public final class Response {

    private int status;
    private InputStream stream;
    private String contentType;
    private long contentLength;
    private final Map<String, String> headers = new TreeMap<>();
    private final Collection<Cookie> cookies = new ArrayList<>();

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
     * @return
     */
    public int status() {
        return status;
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
     * @return
     */
    public long length() {
        return contentLength;
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
    public Map<String, String> headers() {
        return headers;
    }

    /**
     *
     * @param stream
     * @return
     */
    public Response stream(InputStream stream) {
        this.stream = stream;
        return this;
    }

    /**
     *
     * @return
     */
    public InputStream stream() {
        return stream;
    }

    /**
     * Set cookie for the current response.
     *
     * @param cookie Cookie to set
     * @return self
     */
    public Response cookie(Cookie cookie) {
        this.cookies.add(cookie);
        return this;
    }

    /**
     * Returns collection of all cookies for current response.
     *
     * @return collection of all cookies for current response.
     */
    public Collection<Cookie> cookies() {
        return cookies;
    }

    /**
     *
     * @return
     */
    public String contentType() {
        return contentType;
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