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
package com.bunjlabs.fugaframework.foundation;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class Urls {

    private final Context ctx;

    public Urls(Context ctx) {
        this.ctx = ctx;
    }

    public String that(Object... args) {
        StringBuilder sb = new StringBuilder();
        sb.append("http://") // TODO!
                .append(ctx.getRequest().getHost());

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

        if (!ctx.getApp().getConfiguration().getBoolean("fuga.assets.external")) {
            return that("assets" + sb.toString());
        } else {
            return ctx.getApp().getConfiguration().get("fuga.assets.path") + sb.toString();
        }
    }

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
