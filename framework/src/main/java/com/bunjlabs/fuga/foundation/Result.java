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
    public Result status(int status) {
        this.status = status;
        return this;
    }

    public Map<String, String> headers() {
        return headers;
    }

    public Result header(String header, String value) {
        headers.put(header, value);
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
    public Result length(long contentLength) {
        this.contentLength = contentLength;
        return this;
    }

    /**
     *
     * @param stream
     * @return
     */
    public Result stream(InputStream stream) {
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
    public Result asText() {
        this.contentType = "text/plain";
        return this;
    }

    /**
     *
     * @return
     */
    public Result asHtml() {
        this.contentType = "text/html";
        return this;
    }

    /**
     *
     * @return
     */
    public Result asJson() {
        this.contentType = "application/json";
        return this;
    }

    /**
     *
     * @return
     */
    public Result asXml() {
        this.contentType = "text/xml";
        return this;
    }

    /**
     *
     * @return
     */
    public Result asJavascript() {
        this.contentType = "application/javascript";
        return this;
    }

    /**
     *
     * @return
     */
    public Result asCss() {
        this.contentType = "text/css";
        return this;
    }

    /**
     *
     * @param contentType
     * @return
     */
    public Result as(String contentType) {
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
