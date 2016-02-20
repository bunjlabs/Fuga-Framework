package com.bunjlabs.fugaframework.dependency;

public class InjectException extends Exception {

    public InjectException() {
    }
    
    public InjectException(String msg) {
        super(msg);
    }
    
    public InjectException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
