package com.bunjlabs.fugaframework.handlers;

import com.bunjlabs.fugaframework.foundation.Context;
import com.bunjlabs.fugaframework.foundation.Response;

public class DefaultRequestHandler implements RequestHandler{

    @Override
    public Response onRequest(Context ctx) {
        return ctx.getApp().getRouter().forward(ctx);
    }

}
