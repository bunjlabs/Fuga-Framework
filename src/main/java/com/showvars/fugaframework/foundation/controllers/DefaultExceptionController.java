package com.showvars.fugaframework.foundation.controllers;

import com.showvars.fugaframework.foundation.Context;
import com.showvars.fugaframework.foundation.Controller;
import com.showvars.fugaframework.foundation.Response;

public class DefaultExceptionController extends Controller {

    public static Response process(Context ctx, Exception e) {
        StackTraceElement[] ste = e.getStackTrace();

        StringBuilder sb = new StringBuilder();

        for (StackTraceElement el : ste) {
            sb.append(el.toString()).append("\n");
        }
        return ok("<code>" + e.toString() + "</code><br><pre>" + sb.toString() + "</pre>");
    }
}
