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

/**
 *
 * @author Artem Shurygin <artem.shurygin@bunjlabs.com>
 */
public class Template {

    private final Map<String, List<ScriptBlock>> blocks = new HashMap<>();
    private final Map<String, ScriptBlock> tags = new HashMap<>();
    private final List<ScriptBlock> codeblocks = new LinkedList<>();

    /**
     * Creates new empty template
     */
    public Template() {
    }

    /**
     *
     * @param context The request context
     * @param ps Print stream to render template
     * @throws TemplateRenderException
     */
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

    /**
     * Extend another template with this template
     *
     * @param extendable template to extend
     */
    protected void extend(Template extendable) {
        blocks.putAll(extendable.blocks);
        tags.putAll(extendable.tags);
        codeblocks.addAll(extendable.codeblocks);
    }

    /**
     * Use all code blocks and tags from template
     *
     * @param extendable template to be used
     */
    protected void use(Template extendable) {
        this.codeblocks.addAll(extendable.codeblocks);
    }

    /**
     * Add block to this template
     *
     * @param name block name
     * @param block compiled script of this block
     * @param append perfom append if this block already exists
     */
    protected void addBlock(String name, ScriptBlock block, boolean append) {
        if (blocks.containsKey(name) && append) {
            blocks.get(name).add(block);
        } else {
            List<ScriptBlock> list = new LinkedList<>();
            list.add(block);
            blocks.put(name, list);
        }
    }

    /**
     * Add tag to this template
     *
     * @param tagName name of the tag
     * @param compileBlock compiled script of this tag
     */
    protected void addTag(String tagName, ScriptBlock compileBlock) {
        tags.put(tagName, compileBlock);
    }

    /**
     * Add code block to this template
     *
     * @param block Compiled script of this code block
     */
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
