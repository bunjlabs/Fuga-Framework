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

    public final Context ctx;

    public TemplateApi(Context ctx) {
        this.ctx = ctx;
    }

    public byte[] bytes(Object... args) {
        StringBuilder sb = new StringBuilder();

        for (Object arg : args) {
            if (arg != null) {
                sb.append(arg);
            }
        }

        return sb.toString().getBytes(Charset.forName("UTF-8"));
    }

    public String escape(Object... args) {
        StringBuilder sb = new StringBuilder();

        for (Object arg : args) {
            if (arg != null) {
                sb.append(arg);
            }
        }

        return StringEscapeUtils.escapeHtml4(sb.toString());
    }

    public String nltobr(Object... args) {
        StringBuilder sb = new StringBuilder();

        for (Object arg : args) {
            if (arg != null) {
                sb.append(arg);
            }
        }

        return sb.toString().replaceAll("\n", "<br>");
    }

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
