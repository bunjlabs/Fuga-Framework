/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.showvars.sweetie.templates;

/**
 *
 * @author Show
 */
public class TemplateNotFoundException extends Exception {

    /**
     * Creates a new instance of <code>BrookieTemplateNotFoundException</code>
     * without detail message.
     */
    public TemplateNotFoundException() {
    }

    /**
     * Constructs an instance of <code>BrookieTemplateNotFoundException</code>
     * with the specified detail message.
     *
     * @param msg the detail message.
     */
    public TemplateNotFoundException(String msg) {
        super(msg);
    }
}
