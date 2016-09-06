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

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.script.ScriptEngine;
import javax.script.ScriptException;

public class Template {

    private static final Pattern codePattern = Pattern.compile(
            "(\\@\\{([^\\{\\}\\n\\r]*?)\\})"
            + "|(\\<%([\\s\\S]*?)%\\>)"
            + "|(\\<#([^\\n\\r]*?)#\\>)");
    private static final Pattern namePattern = Pattern.compile("[a-zA-Z0-9_-]+");
    private final String tid;
    private final String templateClassName;
    private final Map<String, String> blocks = new HashMap<>();
    private final TemplateEngine templateEngine;
    private String input;
    private String jsSource;

    public Template(TemplateEngine templateEngine, String tid, String input) {
        this.tid = tid;
        this.templateClassName = "Template_" + tid;
        this.templateEngine = templateEngine;
        this.input = input;
    }

    public String getTid() {
        return tid;
    }

    public String getJsSource() {
        return jsSource;
    }

    public String compile() throws TemplateRenderException, TemplateNotFoundException {

        StringBuilder jsCode = new StringBuilder();

        String extendClassName = "";

        if (input.startsWith("@extends ")) {
            String extendName = input.substring(8, input.indexOf('\n')).trim();
            input = input.substring(input.indexOf('\n') + 1);
            if (extendName != null && extendName.length() > 0) {
                extendClassName = "Template_" + templateEngine.compile(extendName);
            }
        }

        jsCode.append(templateClassName).append("=function(stream){this.stream=stream;};");

        jsCode.append(templateClassName).append(".prototype.render=function(ctx,api){");
        jsCode.append(parseBlock(false));
        jsCode.append("};");

        if (extendClassName.length() > 0) {
            jsCode.append(templateClassName).append(".prototype=new ").append(extendClassName).append("();");
        }

        for (Map.Entry<String, String> e : blocks.entrySet()) {
            jsCode.append(templateClassName).append(".prototype.block_").append(e.getKey()).append("=function(ctx,api){");
            jsCode.append(e.getValue());
            jsCode.append("};");
        }

        jsCode.append("process_").append(tid).append("=function(stream,ctx,api) { ")
                .append("var tpl=new ").append(templateClassName)
                .append("(stream);tpl.render(ctx,api);")
                .append("};");

        input = null;
        jsSource = jsCode.toString();

        return tid;

    }

    public void eval(ScriptEngine engine) throws TemplateRenderException {
        try {
            engine.eval(jsSource);
        } catch (ScriptException ex) {
            throw new TemplateRenderException(ex.getLocalizedMessage());
        }
    }

    private String parseBlock(boolean inBlock) throws TemplateRenderException {
        StringBuilder jsCode = new StringBuilder();

        generateApiFunctions(jsCode);

        while (input.length() > 0) {
            Matcher m = codePattern.matcher(input);
            if (m.find()) {
                String text = input.substring(0, m.start());
                if (text.length() > 0) {
                    text = escapeSpaces(text);
                    jsCode.append("this.stream.print('").append(text).append("');");
                }
                if (m.group(2) != null) {
                    jsCode.append("this.stream.print(").append(m.group(2).trim()).append(");");
                    input = input.substring(m.end());
                } else if (m.group(4) != null) {
                    String codeBlock = m.group(4).trim();
                    if (codeBlock.startsWith("block ")) {
                        //if (inBlock) {
                        //    throw new TemplateRenderException("Unexpected block start");
                        //}
                        String blockName = codeBlock.substring(6);
                        if (!namePattern.matcher(blockName).matches()) {
                            throw new TemplateRenderException("Disallowed block name");
                        }
                        input = input.substring(m.end());
                        blocks.put(blockName, parseBlock(true));
                        jsCode.append("this.block_").append(blockName).append("(ctx,api);");
                    } else if (codeBlock.startsWith("endblock")) {
                        if (!inBlock) {
                            throw new TemplateRenderException("Unexpected block end");
                        }
                        input = input.substring(m.end());
                        return jsCode.toString();
                    } else {
                        jsCode.append(codeBlock);
                        input = input.substring(m.end());
                    }
                } else if (m.group(6) != null) {
                    jsCode.append("this.stream.print(").append(m.group(6).trim()).append(");");
                    input = input.substring(m.end());
                }

            } else {
                if (input.length() > 0) {
                    input = escapeSpaces(input);
                    jsCode.append("this.stream.print('").append(input).append("');");
                    input = "";
                }
            }
        }
        return jsCode.toString();
    }

    private static void generateApiFunctions(StringBuilder jsCode) {
        jsCode.append("var that = function() { return api.urls.that(Array.prototype.slice.call(arguments, 0)); };");
        jsCode.append("var asset = function() { return api.urls.asset(Array.prototype.slice.call(arguments, 0)); };");
        jsCode.append("var urlencode = function() { return api.urls.urlencode(Array.prototype.slice.call(arguments, 0)); };");

        jsCode.append("var generateFormId = function() { return api.forms.generateFormId(Array.prototype.slice.call(arguments, 0)); };");
        jsCode.append("var testFormId = function() { return api.forms.testFormId(Array.prototype.slice.call(arguments, 0)); };");

        jsCode.append("var bytes = function() { return api.bytes(Array.prototype.slice.call(arguments, 0)); };");
        jsCode.append("var escape = function() { return api.escape(Array.prototype.slice.call(arguments, 0)); };");
        jsCode.append("var nltobr = function() { return api.nltobr(Array.prototype.slice.call(arguments, 0)); };");
        jsCode.append("var format = function() { return api.format(Array.prototype.slice.call(arguments, 0)); };");
    }

    private static String escapeSpaces(String input) {
        return input.replaceAll("\\\\", "\\\\\\\\")
                .replaceAll("\\t", "\\\\t")
                .replaceAll("\\n", "\\\\n")
                .replaceAll("\\r", "\\\\r")
                .replaceAll("\\f", "\\\\f")
                .replaceAll("\\'", "\\\\'")
                .replaceAll("\\\"", "\\\\\"");
    }

    public static String generateTid() {
        return UUID.randomUUID().toString().replaceAll("-", "_");
    }
}
