package com.showvars.sweetie.templates;

public class TemplateRenderException extends Exception {

    /**
     * Creates a new instance of <code>TemplateRenderException</code> without
     * detail message.
     */
    public TemplateRenderException() {
    }

    /**
     * Constructs an instance of <code>TemplateRenderException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public TemplateRenderException(String msg) {
        super(msg);
    }
}
