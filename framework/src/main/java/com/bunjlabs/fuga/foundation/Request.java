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

import java.net.SocketAddress;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Request {

    private final RequestMethod requestMethod;
    private final Map<String, String> headers;
    private final String uri;
    private final String path;
    private final SocketAddress remoteAddress;
    private final boolean isSecure;
    private final Map<String, List<String>> query;
    private final Map<String, List<Cookie>> cookiesDownload;
    private final Map<String, Cookie> cookiesUpload;
    private final Content content;
    private final List<Locale> acceptLocales;

    /**
     * Request builder
     */
    public static class Builder {

        private RequestMethod requestMethod;
        private Map<String, String> headers;
        private String uri;
        private String path;
        private SocketAddress remoteAddress;
        private boolean isSecure;
        private Map<String, List<String>> query;
        private Map<String, List<Cookie>> cookiesDownload;
        private Map<String, Cookie> cookiesUpload;
        private Content content;
        private List<Locale> acceptLocales;

        public Builder requestMethod(RequestMethod requestMethod) {
            this.requestMethod = requestMethod;
            return this;
        }

        public Builder headers(Map<String, String> headers) {
            this.headers = headers;
            return this;
        }

        public Builder uri(String uri) {
            this.uri = uri;
            return this;
        }

        public Builder path(String path) {
            this.path = path;
            return this;
        }

        public Builder remoteAddress(SocketAddress remoteAddress) {
            this.remoteAddress = remoteAddress;
            return this;
        }

        public Builder isSecure(boolean isSecure) {
            this.isSecure = isSecure;
            return this;
        }

        public Builder query(Map<String, List<String>> query) {
            this.query = query;
            return this;
        }

        public Builder cookiesDownload(Map<String, List<Cookie>> cookiesDownload) {
            this.cookiesDownload = cookiesDownload;
            return this;
        }

        public Builder cookiesUpload(Map<String, Cookie> cookiesUpload) {
            this.cookiesUpload = cookiesUpload;
            return this;
        }

        public Builder content(Content content) {
            this.content = content;
            return this;
        }

        public Builder acceptLocales(List<Locale> acceptLocales) {
            this.acceptLocales = acceptLocales;
            return this;
        }

        public Request build() {
            return new Request(this);
        }
    }

    private Request(Builder builder) {
        this.requestMethod = builder.requestMethod;
        this.headers = builder.headers;
        this.uri = builder.uri;
        this.path = builder.path;
        this.remoteAddress = builder.remoteAddress;
        this.isSecure = builder.isSecure;
        this.query = builder.query;
        this.cookiesDownload = builder.cookiesDownload;
        this.cookiesUpload = builder.cookiesUpload;
        this.content = builder.content;
        this.acceptLocales = builder.acceptLocales;
    }

    /**
     * Returns request method.
     *
     * @return request method.
     */
    public RequestMethod getRequestMethod() {
        return requestMethod;
    }

    /**
     * Returns host.
     *
     * @return host.
     */
    public String getHost() {
        return headers.get("Host");
    }

    /**
     * Returns map of http client headers.
     *
     * @return map of http client headers.
     */
    public Map<String, String> getHeaders() {
        return headers;
    }

    /**
     * Returns the URI.
     *
     * @return the URI.
     */
    public String getUri() {
        return uri;
    }

    /**
     * Returns path.
     *
     * @return path.
     */
    public String getPath() {
        return path;
    }

    /**
     * Returns remote socket address.
     *
     * @return remote socket address.
     */
    public SocketAddress getRemoteAddress() {
        return remoteAddress;
    }

    /**
     * Returns true if the request uses secure connection.
     *
     * @return true if the request uses secure connection.
     */
    public boolean isSecure() {
        return isSecure;
    }

    /**
     * Returns map of get query parameters.
     *
     * @return map of get query parameters.
     */
    public Map<String, List<String>> getQuery() {
        return query;
    }

    /**
     * Returns request cookie by given cookie name.
     *
     * If given cookie name does not exists, null value will be returned.
     *
     * @param name Cookie name.
     * @return request cookie by given cookie name or null.
     */
    public Cookie getCookie(String name) {
        List<Cookie> cookies = cookiesDownload.get(name);

        if (cookies == null) {
            return null;
        }

        return cookies.get(0);
    }

    /**
     * Returns map of request cookies.
     *
     * @return map of request cookies.
     */
    public Map<String, List<Cookie>> getCookiesDownload() {
        return cookiesDownload;
    }

    /**
     * Returns map of response cookies.
     *
     * @return map of response cookies.
     */
    public Map<String, Cookie> getCookiesUpload() {
        return cookiesUpload;
    }

    /**
     * Set cookie for the current request.
     *
     * @param cookie Cookie.
     */
    public void setCookie(Cookie cookie) {
        this.cookiesUpload.put(cookie.getName(), cookie);
    }

    /**
     * Returns request content.
     *
     * @return request content.
     */
    public Content getContent() {
        return content;
    }

    /**
     * Return list of acceptable locales by current request.
     *
     * This list extracted from Accept-Language header and sorted by preference.
     *
     * @return list of acceptable locales by current request.
     */
    public List<Locale> getAcceptLocales() {
        return acceptLocales;
    }
}
