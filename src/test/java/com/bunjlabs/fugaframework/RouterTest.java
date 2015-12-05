package com.bunjlabs.fugaframework;

import com.bunjlabs.fugaframework.router.Router;
import org.junit.Test;

public class RouterTest {

    private FugaApp prepareTestApp() {
        FugaApp fa = new FugaApp() {

            @Override
            public void prepare() {
            }
        };
        return fa;
    }

    @Test
    public void compileTest() throws Exception {
        Router r = prepareTestApp().getRouter();
        
        r.loadFromString("");
        r.loadFromString("GET ok(\"ok\")");
        r.loadFromString("GET POST ok(\"ok\")");
        r.loadFromString("GET GET ok(\"ok\")");
        r.loadFromString("$/ ok(\"ok\")");
        r.loadFromString("ok(\"ok\")");
        r.loadFromString("{}");
        r.loadFromString("{ok(\"ok\")}");
        r.loadFromString("{{{{{{{{{ok(\"ok\")}}}}}}}}}");
        r.loadFromString("GET{POST{OPTIONS{ok(\"ok\")}}}");
        r.loadFromString("use com.example");
        
        r.loadFromString("GET ok(\"ok\") POST ok(\"ok\") OPTIONS ok(\"ok\")");
        
    }
}
