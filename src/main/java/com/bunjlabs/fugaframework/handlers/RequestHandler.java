package com.bunjlabs.fugaframework.handlers;

import com.bunjlabs.fugaframework.foundation.Context;
import com.bunjlabs.fugaframework.foundation.Response;

public interface RequestHandler {

    public Response onRequest(Context ctx);
}
