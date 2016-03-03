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
package com.bunjlabs.fugaframework.sessions;

import com.bunjlabs.fugaframework.FugaApp;
import com.bunjlabs.fugaframework.configuration.Configuration;
import com.bunjlabs.fugaframework.foundation.Context;
import io.netty.handler.codec.http.cookie.Cookie;
import io.netty.handler.codec.http.cookie.DefaultCookie;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class SessionManager {

    private final Map<UUID, Session> sessions = new HashMap<>();
    private final Configuration configuration;

    public SessionManager(FugaApp app) {
        this.configuration = app.getConfiguration();
        
    }
    public Session getSession(UUID sessionId) {
        return sessions.get(sessionId);
    }

    public Session getSession(String sessionId) {
        return sessions.get(UUID.fromString(sessionId));
    }

    public void process(Context ctx) {
        List<Cookie> sessionCookieList = ctx.getRequest().getCookiesDownload().get(configuration.get("fuga.sessions.cookie"));
        Session session = null;
        
        boolean flag = false;
        if (sessionCookieList != null) {
            for (Cookie sessionCookie : sessionCookieList) {
                if (sessionCookie != null && (session = sessions.get(UUID.fromString(sessionCookie.value()))) != null) {
                    flag = true;
                    break;
                }
            }
        }

        if (!flag) {
            UUID sessionId = UUID.randomUUID();
            session = new Session(sessionId);

            sessions.put(sessionId, session);

            DefaultCookie sessionCookie = new DefaultCookie(configuration.get("fuga.sessions.cookie"), session.getSessionId().toString());
            sessionCookie.setPath("/");
            ctx.getRequest().setCookie(sessionCookie);
        }
        ctx.setSession(session);
    }

    public void update() {
        sessions.entrySet().stream().forEach((e) -> {
            Session session = e.getValue();
            long currentTime = new Date().getTime();
            if (currentTime - session.getUpdateTime() >= session.getTimeout() * 60000) {
                sessions.remove(e.getKey());
            }
        });
    }

}
