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

public final class Result {

    private int status;
    private InputStream stream;
    private String contentType;
    private long contentLength;
    private Map<String, String> headers = new TreeMap<>();

    /**
     *
     */
    public Result() {
        this.status = -1;
        this.contentType = "application/octet-stream";
        this.contentLength = -1;
    }

    /**
     *
     * @param is
     */
    public Result(InputStream is) {
        this.status = -1;
        this.stream = is;
        this.contentType = "application/octet-stream";
        this.contentLength = -1;
    }

    /**
     *
     * @param bytes
     */
    public Result(byte[] bytes) {
        this.status = -1;
        this.stream = new ByteArrayInputStream(bytes);
        this.contentType = "application/octet-stream";
        this.contentLength = bytes.length;
    }

    /**
     *
     * @param f
     * @throws IOException
     */
    public Result(File f) throws IOException {
        this.status = -1;
        this.stream = new FileInputStream(f);
        this.contentType = Files.probeContentType(f.toPath());
        this.contentLength = f.length();
    }

    /**
     *
     * @param s
     */
    public Result(String s) {
        this(s.getBytes());
        this.contentType = "text/html";
    }

    /**
     *
     * @return result status
     */
    public int status() {
        return status;
    }

    /**
     * Set status of current result
     *
     * @param status status
     * @return self
     */
    public Result status(int status) {
        this.status = status;
        return this;
    }

    /**
     *
     * @return crrent headers map of this result.
     */
    public Map<String, String> headers() {
        return headers;
    }

    /**
     * Add or change response http header.
     *
     * @param name Header name
     * @param value Header value
     * @return self
     */
    public Result header(String header, String value) {
        headers.put(header, value);
        return this;
    }

    /**
     *
     * @return result content length
     */
    public long length() {
        return contentLength;
    }

    /**
     *
     * @param contentLength content length of this result
     * @return self
     */
    public Result length(long contentLength) {
        this.contentLength = contentLength;
        return this;
    }

    /**
     *
     * @param stream content stream
     * @return self
     */
    public Result stream(InputStream stream) {
        this.stream = stream;
        return this;
    }

    /**
     *
     * @return content stream of this result
     */
    public InputStream stream() {
        return stream;
    }

    /**
     *
     * @return content type of this result
     */
    public String contentType() {
        return contentType;
    }

    /**
     * Set content type of current result to text/plain
     * 
     * @return self
     */
    public Result asText() {
        this.contentType = "text/plain";
        return this;
    }

    /**
     * Set content type of current result to text/html
     * 
     * @return self
     */
    public Result asHtml() {
        this.contentType = "text/html";
        return this;
    }

    /**
     * Set content type of current result to application/json
     * 
     * @return self
     */
    public Result asJson() {
        this.contentType = "application/json";
        return this;
    }

    /**
     * Set content type of current result to text/xml
     * 
     * @return self
     */
    public Result asXml() {
        this.contentType = "text/xml";
        return this;
    }

    /**
     * Set content type of current result to application/javascript
     * 
     * @return self
     */
    public Result asJavascript() {
        this.contentType = "application/javascript";
        return this;
    }

    /**
     * Set content type of current result to text/css
     * 
     * @return self
     */
    public Result asCss() {
        this.contentType = "text/css";
        return this;
    }

    /**
     * Set content type of current result
     * 
     * @param contentType
     * @return self
     */
    public Result as(String contentType) {
        this.contentType = contentType;
        return this;
    }

    /**
     * Check that content stream of this result is null.
     * @return content stream is null
     */
    public boolean isEmpty() {
        return stream == null;
    }
}
