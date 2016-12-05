package com.bunjlabs.fuga.templates;

import javax.script.CompiledScript;

/**
 *
 * @author Artem Shurygin <artem.shurygin@bunjlabs.com>
 */
public class ScriptBlock {

    private CompiledScript script;

    public ScriptBlock(CompiledScript script) {
        if (script == null) {
            throw new NullPointerException("CompiledScript is null");
        }

        this.script = script;
    }

    public CompiledScript getScript() {
        return script;
    }

}
