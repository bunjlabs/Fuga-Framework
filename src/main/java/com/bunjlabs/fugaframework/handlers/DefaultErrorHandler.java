package com.bunjlabs.fugaframework.handlers;

import com.bunjlabs.fugaframework.foundation.Context;
import com.bunjlabs.fugaframework.foundation.Response;
import com.bunjlabs.fugaframework.foundation.Responses;

public class DefaultErrorHandler implements ErrorHandler {

    @Override
    public Response onClientError(Context context, int statusCode) {
        return new Response(statusCode + "").status(statusCode).asText();
    }

    @Override
    public Response onServerError(Context context, Throwable e) {
        StackTraceElement[] ste = e.getStackTrace();

        StringBuilder sb = new StringBuilder();

        sb.append("<h2>").append(e.toString()).append("</h2><p>");
        for (StackTraceElement el : ste) {
            sb.append(el.toString()).append("<br>");
        }
        sb.append("</p>");

        Throwable cause = e.getCause();
        if (cause != null) {
            ste = cause.getStackTrace();
            sb.append("<h3>Caused by: ").append(cause.toString()).append("</h3><p>");
            for (StackTraceElement el : ste) {
                sb.append(el.toString()).append("<br>");
            }
            sb.append("</p>");
        }

        return Responses.internalServerError(sb.toString());
    }

}
