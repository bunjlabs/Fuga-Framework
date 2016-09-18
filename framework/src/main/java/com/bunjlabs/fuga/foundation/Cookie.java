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

import java.util.Objects;

public class Cookie {

    private final String name;
    private String value;
    private boolean wrap;
    private String domain;
    private String path;
    private long maxAge = Long.MIN_VALUE;
    private boolean secure;
    private boolean httpOnly;

    /**
     * Create new cookie with given name and value.
     *
     * @param name Cookie name.
     * @param value Cookie value.
     */
    public Cookie(String name, String value) {
        this.name = name.trim();
        if (name.isEmpty()) {
            throw new IllegalArgumentException("empty name");
        }
        this.value = value;
    }

    /**
     * Returns cookie name.
     *
     * @return cookie name.
     */
    public String name() {
        return name;
    }

    /**
     * Returns cookie value.
     *
     * @return cookie value.
     */
    public String value() {
        return value;
    }

    /**
     * Set cookie value.
     *
     * @param value Cookie value
     * @return self
     */
    public Cookie value(String value) {
        this.value = value;
        return this;
    }

    /**
     * Returns cookie wrap.
     *
     * @return cookie wrap
     */
    public boolean wrap() {
        return wrap;
    }

    /**
     * Set cookie wrap.
     *
     * @param wrap Cookie wrap.
     * @return self
     */
    public Cookie wrap(boolean wrap) {
        this.wrap = wrap;
        return this;
    }

    /**
     * Returns cookie domain.
     *
     * @return cookie domain.
     */
    public String domain() {
        return domain;
    }

    /**
     * Set cookie domain.
     *
     * @param domain Cookie domain.
     * @return self
     */
    public Cookie domain(String domain) {
        this.domain = domain;
        return this;
    }

    /**
     * Returns cookie path.
     *
     * @return cookie path.
     */
    public String path() {
        return path;
    }

    /**
     * Set cookie path.
     *
     * @param path Cookie path.
     * @return self
     */
    public Cookie path(String path) {
        this.path = path;
        return this;
    }

    /**
     * Returns cookie maximum age.
     *
     * @return cookie max age.
     */
    public long maxAge() {
        return maxAge;
    }

    /**
     * Set cookie maximum age.
     *
     * @param maxAge Cookie maximum age.
     * @return self
     */
    public Cookie maxAge(long maxAge) {
        this.maxAge = maxAge;
        return this;
    }

    /**
     * Returns true if cookie is secure.
     *
     * @return true if cookie is secure.
     */
    public boolean isSecure() {
        return secure;
    }

    /**
     * Set cookie is secure
     *
     * @param secure Cookie is secure value
     * @return self
     */
    public Cookie isSecure(boolean secure) {
        this.secure = secure;
        return this;
    }

    /**
     * Returns true if cookie is HTTP only.
     *
     * @return true if cookie is HTTP only.
     */
    public boolean isHttpOnly() {
        return httpOnly;
    }

    /**
     * Set cookie is HTTP only.
     *
     * @param httpOnly Cookie is HTTP only value.
     * @return self
     */
    public Cookie isHttpOnly(boolean httpOnly) {
        this.httpOnly = httpOnly;
        return this;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Cookie other = (Cookie) obj;
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        if (!Objects.equals(this.value, other.value)) {
            return false;
        }
        if (this.wrap != other.wrap) {
            return false;
        }
        if (!Objects.equals(this.domain, other.domain)) {
            return false;
        }
        if (!Objects.equals(this.path, other.path)) {
            return false;
        }
        if (this.maxAge != other.maxAge) {
            return false;
        }
        if (this.secure != other.secure) {
            return false;
        }
        if (this.httpOnly != other.httpOnly) {
            return false;
        }
        return true;
    }
}
