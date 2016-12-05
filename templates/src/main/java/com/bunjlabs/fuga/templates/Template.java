package com.bunjlabs.fuga.templates;

import com.bunjlabs.fuga.foundation.Context;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.script.Bindings;
import javax.script.ScriptContext;
import javax.script.ScriptException;
import javax.script.SimpleBindings;
import javax.script.SimpleScriptContext;

public class Template {

    private final Map<String, ScriptBlock> blocks = new HashMap<>();
    private final List<ScriptBlock> codeblocks = new LinkedList<>();

    public Template() {
    }

    public void render(Context ctx, PrintStream ps) throws TemplateRenderException {
        ScriptContext context = new SimpleScriptContext();

        Bindings bindings = new SimpleBindings();

        insertApi(ctx, bindings);

        bindings.put("block", (VariadicConsumer<Object>) (args) -> {
            if (args.length > 0 && args[0] != null) {
                renderBlock(context, args[0].toString());
            }
        });

        bindings.put("ctx", ctx);

        context.setWriter(new PrintWriter(ps));
        context.setReader(null);
        context.setErrorWriter(null);
        context.setBindings(bindings, ScriptContext.ENGINE_SCOPE);

        try {
            for (ScriptBlock sb : codeblocks) {
                sb.getScript().eval(context);
            }

            renderBlock(context, "main");
        } catch (ScriptException ex) {
            throw new TemplateRenderException(ex);
        }
    }

    protected void extend(Template extendable) {
        this.blocks.putAll(extendable.blocks);
        this.codeblocks.addAll(extendable.codeblocks);
    }

    protected void use(Template extendable) {
        this.codeblocks.addAll(extendable.codeblocks);
    }

    protected void addBlock(String name, ScriptBlock block) {
        this.blocks.put(name, block);
    }

    protected void addCodeBlock(ScriptBlock block) {
        this.codeblocks.add(block);
    }

    private void renderBlock(ScriptContext context, String blockName) throws ScriptException {
        ScriptBlock block = blocks.get(blockName);

        if (block == null) {
            return;
        }

        block.getScript().eval(context);
    }

    private void insertApi(Context ctx, Bindings bindings) {
        TemplateApi api = new TemplateApi(ctx);

        bindings.put("that", (VariadicFunction<Object, Object>) api::that);
        bindings.put("asset", (VariadicFunction<Object, Object>) api::asset);
        bindings.put("urlencode", (VariadicFunction<Object, Object>) api::urlencode);
        bindings.put("generateFormId", (VariadicFunction<Object, Object>) api::generateFormId);
        bindings.put("testFormId", (VariadicFunction<Object, Object>) api::testFormId);
        bindings.put("msg", (VariadicFunction<Object, Object>) api::msg);
        bindings.put("combine", (VariadicFunction<Object, Object>) api::combine);
        bindings.put("bytes", (VariadicFunction<Object, Object>) api::bytes);
        bindings.put("escape", (VariadicFunction<Object, Object>) api::escape);
        bindings.put("nltobr", (VariadicFunction<Object, Object>) api::nltobr);
        bindings.put("format", (VariadicFunction<Object, Object>) api::format);
    }
}
