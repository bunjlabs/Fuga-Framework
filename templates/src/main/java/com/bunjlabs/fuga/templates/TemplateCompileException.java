package com.bunjlabs.fuga.templates;

import com.bunjlabs.fuga.views.ViewException;

/**
 *
 * @author Artem Shurygin <artem.shurygin@bunjlabs.com>
 */
public class TemplateCompileException extends ViewException {

    public TemplateCompileException() {
    }

    public TemplateCompileException(String msg) {
        super(msg);
    }

    public TemplateCompileException(String message, Throwable cause) {
        super(message, cause);
    }

    public TemplateCompileException(Throwable cause) {
        super(cause);
    }

}
