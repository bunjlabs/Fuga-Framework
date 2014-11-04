package com.showvars.sweetie.templates;

import com.showvars.sweetie.SweetieApp;

public class TemplateApi {

    private final SweetieApp app;

    public TemplateApi(SweetieApp app) {
        this.app = app;

    }

    public String asset(Object... args) {
        if (args.length > 0 && args[0] != null && args[0] instanceof String) {
            return "/assets/" + ((String) args[0]);
        } else {
            return "/assets/";
        }
    }
}
