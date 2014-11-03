package com.showvars.sweetie.foundation.controllers;

import com.showvars.sweetie.foundation.Context;
import com.showvars.sweetie.foundation.Controller;
import com.showvars.sweetie.foundation.Response;

public class DefaultExceptionController extends Controller {

    public static Response process(Context ctx, Exception e) {
        StackTraceElement[] ste = e.getStackTrace();

        StringBuilder sb = new StringBuilder();

        for (StackTraceElement el : ste) {
            sb.append(el.toString()).append("\n");
        }
        return ok("<h3>" + e.toString() + "</h3><pre>" + sb.toString() + "</pre>");
    }
}
