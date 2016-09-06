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
package com.bunjlabs.fuga.handlers;

import com.bunjlabs.fuga.foundation.Request;
import com.bunjlabs.fuga.foundation.Response;
import com.bunjlabs.fuga.foundation.Responses;

public class DefaultErrorHandler implements ErrorHandler {

    @Override
    public Response onClientError(Request request, int statusCode) {
        return new Response(statusCode + "").status(statusCode).asText();
    }

    @Override
    public Response onServerError(Request request, Throwable e) {
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
