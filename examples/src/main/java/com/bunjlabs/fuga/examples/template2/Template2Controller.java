package com.bunjlabs.fuga.examples.template2;

import com.bunjlabs.fuga.foundation.Controller;
import com.bunjlabs.fuga.foundation.Result;
import com.bunjlabs.fuga.views.ViewException;

public class Template2Controller extends Controller {

    public Result index() throws ViewException {
        return ok(view("template2.html"));
    }
}
