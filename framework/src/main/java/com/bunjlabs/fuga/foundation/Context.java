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

import com.bunjlabs.fuga.i18n.Messages;
import com.bunjlabs.fuga.FugaApp;
import com.bunjlabs.fuga.sessions.Session;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

public class Context {

    private final FugaApp app;
    private final Request request;
    private final Response response;
    private final Map<String, Object> attributes;
    private Session session;
    private String lang;
    private final Urls urls;
    private final Forms forms;

    /**
     * Create new context for the current request and application.
     *
     * @param app Fuga application.
     * @param request Client request.
     * @param response Server response.
     */
    public Context(FugaApp app, Request request, Response response) {
        this.app = app;
        this.request = request;
        this.response = response;
        this.attributes = new HashMap<>();
        this.urls = new Urls(this);
        this.forms = new Forms(this);
    }

    /**
     * Returns the current request.
     *
     * @return the current request.
     */
    public Request request() {
        return request;
    }

    /**
     * Returns the current response.
     *
     * @return the current response.
     */
    public Response response() {
        return response;
    }

    /**
     * Returns current fuga application.
     *
     * @return current fuga application.
     */
    public FugaApp app() {
        return app;
    }

    /**
     * Returns the session for current request.
     *
     * @return the session for current request.
     */
    public Session session() {
        if (session == null) {
            session = app.getSessionManager().getSession(this);
        }
        return session;
    }

    /**
     * Returns language code for the current context.
     *
     * At first this method check request language cookie. If no language cookie
     * exists it will extract prefered language from Accept-Language http header
     * and save this value in request cookie.
     *
     * @return language code for the current context.
     */
    public String lang() {
        if (lang != null && !lang.isEmpty()) {
            return lang;
        }

        Optional<Cookie> flcCookie = request.cookie(app.getConfiguration().get("fuga.i18n.cookie"));

        if (flcCookie.isPresent() && !flcCookie.get().value().isEmpty()) {
            return flcCookie.get().value();
        }

        List<Locale> requestAcceptLocales = request.acceptLocales();

        if (requestAcceptLocales.isEmpty()) {
            return app.getConfiguration().get("fuga.i18n.default");
        }

        String language = requestAcceptLocales.get(0).getLanguage();

        lang(language);

        return language;
    }

    /**
     * Rewrite user language by setting locale cookie.
     *
     * @param lang language code
     */
    public void lang(String lang) {
        this.lang = lang;
        response.cookie(new Cookie(app.getConfiguration().get("fuga.i18n.cookie"), lang).path("/"));
    }

    /**
     * Returns instance of Messages class for the current context.
     *
     * @return instance of Messages class for the current context.
     */
    public Messages msg() {
        return app.getMessagesManager().getMessages(this);
    }

    /**
     * Returns instance of Urls class for the current context.
     *
     * @return instance of Urls class for the current context.
     */
    public Urls urls() {
        return urls;
    }

    /**
     * Returns instance of Forms class for the current context.
     *
     * @return instance of Forms class for the current context.
     */
    public Forms forms() {
        return forms;
    }

    /**
     * Returns custom context attribute.
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
        return (T) attributes.get(name);
    }

    /**
     * Returns custom context attribute.
     *
     * Custom context attributes can be set by using
     * {@link #put(String, Object) put} method.
     *
     * @param <T> The type of attribute.
     * @param name Name of the context attribute.
     * @return Custom context attribute.
     */
    public <T> T get(String name) {
        return (T) attributes.get(name);
    }

    /**
     * Puts custom attribute to context.
     *
     * @param name Name of the context attribute.
     * @param value Value of the context attribute.
     */
    public void put(String name, Object value) {
        attributes.put(name, value);
    }
}
