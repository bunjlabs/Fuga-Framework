package com.bunjlabs.fuga.templates;

/**
 *
 * @author Artem Shurygin <artem.shurygin@bunjlabs.com>
 */
public class TemplateReaderException extends Exception {

    public TemplateReaderException() {
    }

    public TemplateReaderException(String message) {
        super(message);
    }

    public TemplateReaderException(String message, Throwable cause) {
        super(message, cause);
    }

    public TemplateReaderException(Throwable cause) {
        super(cause);
    }

    public TemplateReaderException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public TemplateReaderException(String message, int line, int col) {
        super("(" + line + ":" + col + ") " + message);
    }

    public TemplateReaderException(String message, Throwable cause, int line, int col) {
        super("(" + line + ":" + col + ") " + message, cause);
    }

}
