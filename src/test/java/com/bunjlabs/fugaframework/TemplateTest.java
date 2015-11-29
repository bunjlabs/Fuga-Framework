package com.bunjlabs.fugaframework;

import com.bunjlabs.fugaframework.templates.TemplateEngine;
import junit.framework.Assert;
import org.junit.Test;

public class TemplateTest {

    private FugaApp prepareTestApp() {
        FugaApp fa = new FugaApp() {

            @Override
            public void prepare() {
            }
        };
        return fa;
    }
    
    @Test
    public void controllSymbolsTest() throws Exception {

        TemplateEngine te = prepareTestApp().getTemplateEngine();

        te.compile("test-01", "<# 'data' #>");
        te.compile("test-02", "@{ 'data' }");
        te.compile("test-03", "<# 'd' #>@{ 'a' }<# 't' #>@{ 'a' }");

        Assert.assertEquals("data", te.renderToString("test-01", null));
        Assert.assertEquals("data", te.renderToString("test-02", null));
        Assert.assertEquals("data", te.renderToString("test-03", null));
    }
    
    @Test
    public void jsTest() throws Exception {
        TemplateEngine te = prepareTestApp().getTemplateEngine();

        te.compile("test-01", "<% for(var i = 1; i <= 5; i++) %><# i.toString() #>");
        te.compile("test-02", "<% for(var i = 1; i <= 5; i++) { %><# i.toString() #><% } %>");
        te.compile("test-03", "<% var i=1; while(i <= 5) { %>@{ i.toString() }<% i++; } %>");

        Assert.assertEquals("12345", te.renderToString("test-01", null));
        Assert.assertEquals("12345", te.renderToString("test-02", null));
        Assert.assertEquals("12345", te.renderToString("test-03", null));
    }
    
    @Test
    public void jsHtmlTest() throws Exception {
        TemplateEngine te = prepareTestApp().getTemplateEngine();

        te.compile("test-01", "@");
        te.compile("test-02", "@@");
        te.compile("test-03", "@{");
        te.compile("test-04", "@}");
        
        te.compile("test-05", "<");
        te.compile("test-06", ">");
        te.compile("test-07", "<#");
        te.compile("test-08", "#>");
        te.compile("test-09", "<##");
        te.compile("test-10", "##>");

        Assert.assertEquals("@",     te.renderToString("test-01", null));
        Assert.assertEquals("@@",    te.renderToString("test-02", null));
        Assert.assertEquals("@{",    te.renderToString("test-03", null));
        Assert.assertEquals("@}",    te.renderToString("test-04", null));
        
        Assert.assertEquals("<",     te.renderToString("test-05", null));
        Assert.assertEquals(">",     te.renderToString("test-06", null));
        Assert.assertEquals("<#",    te.renderToString("test-07", null));
        Assert.assertEquals("#>",    te.renderToString("test-08", null));
        Assert.assertEquals("<##",   te.renderToString("test-09", null));
        Assert.assertEquals("##>",   te.renderToString("test-10", null));
    }
    

}
