/* 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.bunjlabs.fugaframework;

import com.bunjlabs.fugaframework.templates.TemplateEngine;
import com.bunjlabs.fugaframework.views.DefaultViewRenderer;
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
        FugaApp app = prepareTestApp();
        
        DefaultViewRenderer vr = ((DefaultViewRenderer) app.getViewRenderer());
        TemplateEngine te = vr.getTemplateEngine();

        te.compile("test-01", "<# 'data' #>");
        te.compile("test-02", "@{ 'data' }");
        te.compile("test-03", "<# 'd' #>@{ 'a' }<# 't' #>@{ 'a' }");

        Assert.assertEquals("data", vr.renderToString("test-01", null));
        Assert.assertEquals("data", vr.renderToString("test-02", null));
        Assert.assertEquals("data", vr.renderToString("test-03", null));
    }
    
    @Test
    public void jsTest() throws Exception {
        FugaApp app = prepareTestApp();
        
        DefaultViewRenderer vr = ((DefaultViewRenderer) app.getViewRenderer());
        TemplateEngine te = vr.getTemplateEngine();

        te.compile("test-01", "<% for(var i = 1; i <= 5; i++) %><# i.toString() #>");
        te.compile("test-02", "<% for(var i = 1; i <= 5; i++) { %><# i.toString() #><% } %>");
        te.compile("test-03", "<% var i=1; while(i <= 5) { %>@{ i.toString() }<% i++; } %>");

        Assert.assertEquals("12345", vr.renderToString("test-01", null));
        Assert.assertEquals("12345", vr.renderToString("test-02", null));
        Assert.assertEquals("12345", vr.renderToString("test-03", null));
    }
    
    @Test
    public void jsHtmlTest() throws Exception {
        FugaApp app = prepareTestApp();
        
        DefaultViewRenderer vr = ((DefaultViewRenderer) app.getViewRenderer());
        TemplateEngine te = vr.getTemplateEngine();

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

        
        Assert.assertEquals("@",     vr.renderToString("test-01", null));
        Assert.assertEquals("@@",    vr.renderToString("test-02", null));
        Assert.assertEquals("@{",    vr.renderToString("test-03", null));
        Assert.assertEquals("@}",    vr.renderToString("test-04", null));
        
        Assert.assertEquals("<",     vr.renderToString("test-05", null));
        Assert.assertEquals(">",     vr.renderToString("test-06", null));
        Assert.assertEquals("<#",    vr.renderToString("test-07", null));
        Assert.assertEquals("#>",    vr.renderToString("test-08", null));
        Assert.assertEquals("<##",   vr.renderToString("test-09", null));
        Assert.assertEquals("##>",   vr.renderToString("test-10", null));
    }
    

}
