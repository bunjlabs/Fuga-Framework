package com.bunjlabs.fugaframework.templates;

import java.nio.charset.Charset;

public class JString {

    private final String data;
    
    public JString(String s) {
        this.data = s;
    }
    
    public JString(char[] c) {
        this.data = new String(c);
    }
    
    public JString(byte[] b) {
        this.data = new String(b, Charset.forName("UTF-8"));
    }
    
    @Override
    public String toString() {
        return data;
    }
    
    @Override
    public int hashCode() {
        return data.hashCode();
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (String.class != obj.getClass()) {
            return false;
        }
        final String other = (String) obj;
        return other.equals(data);
    }
}
