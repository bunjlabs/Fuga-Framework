package com.bunjlabs.fuga.templates;

import com.bunjlabs.fuga.foundation.Context;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.Collections;
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

    private final Map<String, List<ScriptBlock>> blocks = new HashMap<>();
    private final Map<String, ScriptBlock> tags = new HashMap<>();
    private final List<ScriptBlock> codeblocks = new LinkedList<>();

    public Template() {
    }

    public void render(Context context, PrintStream ps) throws TemplateRenderException {
        ScriptContext scriptContext = new SimpleScriptContext();
        Bindings bindings = new SimpleBindings();

        insertApi(context, bindings);

        bindings.put("block", (VariadicConsumer<Object>) (args) -> {
            if (args.length > 0 && args[0] != null) {
                renderBlock(scriptContext, args[0].toString());
            }
        });

        bindings.put("tag", (VariadicConsumer<Object>) (args) -> {
            if (args.length > 0 && args[0] != null) {
                renderTag(scriptContext, args[0].toString(), args.length > 1 ? args[1] : null);
            }
        });

        bindings.put("ctx", context);

        scriptContext.setWriter(new PrintWriter(ps));
        scriptContext.setReader(null);
        scriptContext.setErrorWriter(null);
        scriptContext.setBindings(bindings, ScriptContext.ENGINE_SCOPE);

        try {
            for (ScriptBlock sb : codeblocks) {
                sb.getScript().eval(scriptContext);
            }

            renderBlock(scriptContext, "main");
        } catch (ScriptException ex) {
            throw new TemplateRenderException(ex);
        }
    }

    protected void extend(Template extendable) {
        blocks.putAll(extendable.blocks);
        tags.putAll(extendable.tags);
        codeblocks.addAll(extendable.codeblocks);
    }

    protected void use(Template extendable) {
        this.codeblocks.addAll(extendable.codeblocks);
    }

    protected void addBlock(String name, ScriptBlock block, boolean append) {
        if (blocks.containsKey(name) && append) {
            blocks.get(name).add(block);
        } else {
            List<ScriptBlock> list = new LinkedList<>();
            list.add(block);
            blocks.put(name, list);
        }
    }

    void addTag(String tagName, ScriptBlock compileBlock) {
        tags.put(tagName, compileBlock);
    }

    protected void addCodeBlock(ScriptBlock block) {
        codeblocks.add(block);
    }

    private void renderBlock(ScriptContext context, String blockName) throws ScriptException {
        List<ScriptBlock> blockList = blocks.get(blockName);

        if (blockList != null) {
            for (ScriptBlock b : blockList) {
                b.getScript().eval(context);
            }
        }
    }

    private void renderTag(ScriptContext context, String TagName, Object args) throws ScriptException {
        ScriptBlock tag = tags.get(TagName);
        if (tag != null) {
            Bindings oldBindings = context.getBindings(ScriptContext.ENGINE_SCOPE);
            Bindings bindings = new SimpleBindings(oldBindings);

            if (args != null) {
                bindings.put("args", args);
            }

            context.setBindings(bindings, ScriptContext.ENGINE_SCOPE);

            tag.getScript().eval(context);

            context.setBindings(oldBindings, ScriptContext.ENGINE_SCOPE);
        }
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
