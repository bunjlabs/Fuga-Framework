package com.showvars.sweetie.templates;

import com.showvars.sweetie.foundation.Context;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.Reader;
import java.io.StringReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class Template {

    private static final Pattern codePattern = Pattern.compile("(<%([\\s\\S]*?)%>)|(<#([^\\n\\r]*?)#>)");

    private final ScriptEngineManager factory = new ScriptEngineManager();
    private final ScriptEngine engine = factory.getEngineByName("JavaScript");

    public void compile(String input) throws TemplateRenderException {
        compile(new StringReader(input));
    }

    public void compile(Reader input) throws TemplateRenderException {
        try {
            StringBuilder b = new StringBuilder();
            BufferedReader in = new BufferedReader(input);

            b.append("Template=function(stream){this.stream=stream;};");
            b.append("Template.prototype.render=function(context, data){");

            String line;
            while ((line = in.readLine()) != null) {
                if (line.length() <= 0) {
                    continue;
                }
                if (codePattern.matcher(line).find()) {
                    while (line.length() > 0) {
                        Matcher m = codePattern.matcher(line);

                        boolean finded = m.find();
                        int matchStartIndex = finded ? m.start() : line.length();
                        int matchLength = finded ? m.end() - m.start() : 0;

                        String part = line.substring(0, matchStartIndex);

                        if (part.length() > 0) {
                            b.append("this.stream.print('").append(part).append("');");
                        }

                        line = line.substring(matchStartIndex);

                        String codeBlock = line.substring(0, matchLength);

                        Matcher cm = codePattern.matcher(codeBlock);
                        if (cm.matches()) {
                            if (cm.group(2) != null) {
                                b.append(cm.group(2).trim());
                            } else {
                                b.append("this.stream.print(").append(cm.group(4).trim()).append(");");
                            }
                        }

                        line = line.substring(matchLength);
                    }
                    b.append("this.stream.println();");
                } else {
                    b.append("this.stream.println('").append(line).append("');");
                }
            }
            b.append("};");
            b.append("process = function(stream, context, obj) { ")
                    .append("var tpl = new Template(stream); tpl.render(context, obj);")
                    .append("};");

            engine.eval(b.toString());

        } catch (IOException | ScriptException ex) {
            throw new TemplateRenderException(ex.getLocalizedMessage());
        }
    }

    public void render(PrintStream ps, Context ctx, Object obj) throws TemplateRenderException {
        try {
            Invocable inv = (Invocable) engine;
            inv.invokeFunction("process", ps, ctx, obj);
        } catch (ScriptException | NoSuchMethodException ex) {
            throw new TemplateRenderException(ex.getLocalizedMessage());
        }
    }

    public static String render(String input, Context ctx, Object obj) throws TemplateRenderException {
        Template bt = new Template();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        bt.compile(input);
        bt.render(new PrintStream(out), ctx, obj);

        return out.toString();
    }
}
