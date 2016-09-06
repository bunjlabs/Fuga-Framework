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
import java.util.Map;

public class Context {

    private final Request request;
    private final FugaApp app;
    private final Map<String, Object> data;
    private Session session;

    public Context(Request request, FugaApp app) {
        this.request = request;
        this.app = app;
        data = new HashMap<>();
    }

    public Request getRequest() {
        return request;
    }

    public FugaApp getApp() {
        return app;
    }

    public Session getSession() {
        if (session == null) {
            session = app.getSessionManager().getSession(this);
        }
        return session;
    }

    public <T> T get(String name, Class<T> type) {
        return (T) data.get(name);
    }

    public <T> T get(String name) {
        return (T) data.get(name);
    }

    public void put(String name, Object obj) {
        data.put(name, obj);
    }
}
