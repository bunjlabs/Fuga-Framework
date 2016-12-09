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

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class Urls {

    private final Context ctx;

    /**
     * Create new Urls class instance for the specified context.
     *
     * @param ctx request context
     */
    public Urls(Context ctx) {
        this.ctx = ctx;
    }

    /**
     * Returns full url to the specified location.
     *
     * Returned url always contains scheme, host and port. If args array length
     * greater than equal, each element will be merged with slash symbol.
     *
     * For example, this calling:
     * <pre>
     * urls.that("p1", "p2");
     * </pre> will return <code>http://localhost/p1/p2</code>
     *
     * @param args Location
     * @return full url to the specified location.
     */
    public String that(Object... args) {
        StringBuilder sb = new StringBuilder();
        sb.append(ctx.request().isSecure() ? "https" : "http").append("://")
                .append(ctx.request().host());

        if (args.length <= 0) {
            sb.append("/");
        } else {
            for (Object arg : args) {
                if (arg != null) {
                    sb.append('/').append(arg);
                }
            }
        }

        return sb.toString();
    }

    /**
     * Returns full url to the specified asset.
     *
     * Returned url always contains scheme, host and port. If args array length
     * greater than equal, each element will be merged with slash symbol.
     *
     * For example, this calling:
     * <pre>
     * urls.that("p1", "p2");
     * </pre> will return <code>http://localhost/assets/p1/p2</code>
     *
     * @param args Asset
     * @return full url to the specified asset.
     */
    public String asset(Object... args) {
        StringBuilder sb = new StringBuilder();

        if (args.length <= 0) {
            sb.append("/");
        } else {
            for (Object arg : args) {
                if (arg != null) {
                    sb.append('/').append(arg);
                }
            }
        }

        if (!ctx.app().getConfiguration().getBoolean("fuga.assets.external")) {
            return that("assets" + sb.toString());
        } else {
            return ctx.app().getConfiguration().get("fuga.assets.path") + sb.toString();
        }
    }

    /**
     * Returns given string translated into application/x-www-form-urlencoded
     * format string.
     *
     * If args array length greater than equal, each element just will be
     * merged.
     *
     * @param args String
     * @return url encoded string.
     */
    public String urlencode(Object... args) {
        StringBuilder sb = new StringBuilder();

        for (Object arg : args) {
            if (arg != null) {
                sb.append(arg);
            }
        }

        try {
            return URLEncoder.encode(sb.toString(), "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            return "";
        }
    }
}
