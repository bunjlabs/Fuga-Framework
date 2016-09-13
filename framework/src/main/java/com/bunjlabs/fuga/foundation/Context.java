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
package com.bunjlabs.fuga.foundation;

import com.bunjlabs.fuga.FugaApp;
import com.bunjlabs.fuga.sessions.Session;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Context {

    private final Request request;
    private final FugaApp app;
    private final Map<String, Object> data;
    private Session session;

    /**
     * Create new context for the current request and application.
     *
     * @param request Client request.
     * @param app Fuga application.
     */
    public Context(Request request, FugaApp app) {
        this.request = request;
        this.app = app;
        data = new HashMap<>();
    }

    /**
     * Returns the current request.
     *
     * @return the current request.
     */
    public Request getRequest() {
        return request;
    }

    /**
     * Returns current fuga application.
     *
     * @return current fuga application.
     */
    public FugaApp getApp() {
        return app;
    }

    /**
     * Return the session for current request.
     *
     * @return the session for current request.
     */
    public Session getSession() {
        if (session == null) {
            session = app.getSessionManager().getSession(this);
        }
        return session;
    }

    /**
     * Return language code for the current request.
     *
     * At first this method check client language cookie. If no language cookie
     * exists it will extract prefered language from Accept-Language http header
     * and save this value in client cookie.
     *
     * @return language code for the current request.
     */
    public String getLang() {
        List<Locale> requestAcceptLocales = request.getAcceptLocales();

        if (requestAcceptLocales.isEmpty()) {
            return app.getConfiguration().get("fuga.i18n.default");
        }

        return requestAcceptLocales.get(0).getLanguage();
    }

    /**
     * Return custom context attribute.
     *
     * Custom context attributes can be set by using
     * {@link #put(String, Object) put} method.
     *
     * @param <T> The type of attribute.
     * @param name Name of the context value.
     * @param type The type of attribute.
     * @return custom context attribute.
     */
    public <T> T get(String name, Class<T> type) {
        return (T) data.get(name);
    }

    /**
     * Return custom context attribute.
     *
     * Custom context attributes can be set by using
     * {@link #put(String, Object) put} method.
     *
     * @param <T> The type of attribute.
     * @param name Name of the context attribute.
     * @return Custom context attribute.
     */
    public <T> T get(String name) {
        return (T) data.get(name);
    }

    /**
     * Puts custom attribute to context.
     *
     * @param name Name of the context attribute.
     * @param value Value of the context attribute.
     */
    public void put(String name, Object value) {
        data.put(name, value);
    }
}
