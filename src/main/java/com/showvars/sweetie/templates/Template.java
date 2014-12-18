package com.showvars.sweetie.templates;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.script.ScriptEngine;
import javax.script.ScriptException;

public class Template {

    private static final Pattern codePattern = Pattern.compile("(\\{%([\\s\\S]*?)%\\})|(\\{#([^\\n\\r]*?)#\\})");
    private static final Pattern namePattern = Pattern.compile("[a-zA-Z0-9_-]+");
    private final String tid;
    private final String templateClassName;
    private final Map<String, String> blocks = new HashMap<>();
    private String input;

    public Template(String input) {
        this.tid = UUID.randomUUID().toString().replaceAll("-", "_");
        this.templateClassName = "Template_" + tid;
        this.input = input;
    }

    public String compile(ScriptEngine engine) throws TemplateRenderException {

        StringBuilder jsCode = new StringBuilder();
        StringBuilder tplCode = new StringBuilder();

        String extendName = "";

        jsCode.append(templateClassName).append("=function(stream){this.stream=stream;};");
        jsCode.append(templateClassName).append(".prototype.render=function(context, data){");

        if (input.startsWith("@extend ")) {
            extendName = input.substring(8, input.indexOf('\n')).trim();
            input = input.substring(input.indexOf('\n') + 1);
        }

        jsCode.append(parseBlock(false));

        jsCode.append("};");
        for (Map.Entry<String, String> e : blocks.entrySet()) {
            jsCode.append(templateClassName).append(".prototype.block_").append(e.getKey()).append("=function(){");
            jsCode.append(e.getValue());
            jsCode.append("};");
        }
        jsCode.append(templateClassName).append(".prototype.extend=function(context, data){");
        jsCode.append("return '").append(extendName).append("';");
        jsCode.append("};");
        jsCode.append("process_").append(tid).append(" = function(stream, context, data) { ")
                .append("var tpl = new ").append(templateClassName)
                .append("(stream); tpl.render(context, data);")
                .append("};");

        try {
            engine.eval(jsCode.toString());
        } catch (ScriptException ex) {
            throw new TemplateRenderException(ex.getLocalizedMessage());
        }
        return tid;

    }

    private String parseBlock(boolean inBlock) throws TemplateRenderException {
        StringBuilder jsCode = new StringBuilder();
        while (input.length() > 0) {
            Matcher m = codePattern.matcher(input);
            if (m.find()) {
                String text = input.substring(0, m.start());
                if (text.length() > 0) {
                    text = escapeSpaces(text);
                    jsCode.append("this.stream.print('").append(text).append("');");
                }
                if (m.group(2) != null) {
                    String codeBlock = m.group(2).trim();
                    if (codeBlock.startsWith("block ")) {
                        if (inBlock) {
                            throw new TemplateRenderException("Unexpected block start");
                        }
                        String blockName = codeBlock.substring(6);
                        if (!namePattern.matcher(blockName).matches()) {
                            throw new TemplateRenderException("Disallowed block name");
                        }
                        input = input.substring(m.end());
                        blocks.put(blockName, parseBlock(true));
                    } else if (codeBlock.startsWith("endblock")) {
                        if (!inBlock) {
                            throw new TemplateRenderException("Unexpected block end");
                        }
                        input = input.substring(m.end());
                        return jsCode.toString();
                    } else {
                        jsCode.append(m.group(2).trim());
                        input = input.substring(m.end());
                    }
                } else {
                    jsCode.append("this.stream.print(").append(m.group(4).trim()).append(");");
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

    private static String escapeSpaces(String input) {
        return input.replaceAll("\\\\", "\\\\\\\\")
                .replaceAll("\\t", "\\\\t")
                .replaceAll("\\n", "\\\\n")
                .replaceAll("\\r", "\\\\r")
                .replaceAll("\\f", "\\\\f")
                .replaceAll("\\'", "\\\\'")
                .replaceAll("\\\"", "\\\\\""); // and it's works as needed
    }
}
