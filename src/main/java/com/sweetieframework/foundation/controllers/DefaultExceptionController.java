package com.sweetieframework.foundation.controllers;

import com.sweetieframework.foundation.Controller;
import com.sweetieframework.foundation.Request;
import com.sweetieframework.foundation.Response;

public class DefaultExceptionController extends Controller {

    public static Response process(Request r, Exception e) {
        StackTraceElement[] ste = e.getStackTrace();
        
        StringBuilder sb = new StringBuilder();
        
        for(StackTraceElement el : ste) {
            sb.append(el.toString()).append("\n");
        }
        return ok("<h3>" + e.toString() + "</h3><pre>" + sb.toString() + "</pre>");
    }
}
