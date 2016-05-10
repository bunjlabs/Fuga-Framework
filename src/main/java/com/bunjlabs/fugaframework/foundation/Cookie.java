package com.bunjlabs.fugaframework.foundation;

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

    public Cookie(String name, String value) {
        this.name = name.trim();
        if (name.isEmpty()) {
            throw new IllegalArgumentException("empty name");
        }
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean getWrap() {
        return wrap;
    }

    public void setWrap(boolean wrap) {
        this.wrap = wrap;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = domain;
    }

    public long getMaxAge() {
        return maxAge;
    }

    public void setMaxAge(long maxAge) {
        this.maxAge = maxAge;
    }

    public boolean isSecure() {
        return secure;
    }

    public void setSecure(boolean secure) {
        this.secure = secure;
    }

    public boolean isHttpOnly() {
        return httpOnly;
    }

    public void setHttpOnly(boolean httpOnly) {
        this.httpOnly = httpOnly;
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
