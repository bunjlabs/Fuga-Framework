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
package com.bunjlabs.fuga.templates;

import com.bunjlabs.fuga.foundation.Context;
import java.nio.charset.Charset;
import java.util.Arrays;
import org.apache.commons.lang3.StringEscapeUtils;

public class TemplateApi {

    /**
     * The current request context
     */
    public final Context ctx;

    /**
     *
     * @param ctx The request context
     */
    public TemplateApi(Context ctx) {
        this.ctx = ctx;
    }

    /**
     *
     * @param args Input arguments
     * @return Produced string
     */
    public String that(Object... args) {
        return ctx.urls().that(args);
    }

    /**
     *
     * @param args Input arguments
     * @return Produced string
     */
    public String asset(Object... args) {
        return ctx.urls().asset(args);
    }

    /**
     *
     * @param args Input arguments
     * @return Produced string
     */
    public String urlencode(Object... args) {
        return ctx.urls().urlencode(args);
    }

    /**
     *
     * @param args Input arguments
     * @return Produced string
     */
    public String generateFormId(Object... args) {
        if (args.length < 1 || args[0] == null) {
            return "";
        }
        return ctx.forms().generateFormId(args[0].toString());
    }

    /**
     *
     * @param args Input arguments
     * @return Produced string
     */
    public boolean testFormId(Object... args) {
        if (args.length < 2 || args[0] == null || args[1] == null) {
            return false;
        }
        return ctx.forms().testFormId(args[0].toString(), args[1].toString());
    }

    /**
     *
     * @param args Input arguments
     * @return Produced string
     */
    public String msg(Object... args) {
        if (args.length == 0) {
            return "";
        }

        if (args.length == 1) {
            return args[0] != null ? ctx.msg().get(args[0].toString()) : "";
        }

        return ctx.msg().get(args[0].toString(), Arrays.copyOfRange(args, 1, args.length));
    }

    /**
     *
     * @param args Input arguments
     * @return Produced string
     */
    public String combine(Object... args) {
        StringBuilder sb = new StringBuilder();

        for (Object arg : args) {
            if (arg != null) {
                sb.append(arg);
            }
        }

        return sb.toString();
    }

    /**
     *
     * @param args Input arguments
     * @return Produced string
     */
    public byte[] bytes(Object... args) {
        StringBuilder sb = new StringBuilder();

        for (Object arg : args) {
            if (arg != null) {
                sb.append(arg);
            }
        }

        return sb.toString().getBytes(Charset.forName("UTF-8"));
    }

    /**
     *
     * @param args Input arguments
     * @return Produced string
     */
    public String escape(Object... args) {
        StringBuilder sb = new StringBuilder();

        for (Object arg : args) {
            if (arg != null) {
                sb.append(arg);
            }
        }

        return StringEscapeUtils.escapeHtml4(sb.toString());
    }

    /**
     *
     * @param args Input arguments
     * @return Produced string
     */
    public String nltobr(Object... args) {
        StringBuilder sb = new StringBuilder();

        for (Object arg : args) {
            if (arg != null) {
                sb.append(arg);
            }
        }

        return sb.toString().replaceAll("\n", "<br>");
    }

    /**
     *
     * @param args Input arguments
     * @return Produced string
     */
    public String format(Object... args) {
        if (args.length == 0) {
            return "";
        }
        if (args.length == 1) {
            return args[0] != null ? args[0].toString() : "";
        }

        return String.format(args[0].toString(), Arrays.copyOfRange(args, 1, args.length));
    }
}
