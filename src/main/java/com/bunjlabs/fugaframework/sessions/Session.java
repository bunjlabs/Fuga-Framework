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

import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

public class Session extends HashMap<String, Object> {

    private long updateTime;
    private int timeout = 30; // 30 minutes
    private final UUID sessionId;

    public Session(UUID sessionId) {
        updateTime = new Date().getTime();
        this.sessionId = sessionId;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void update() {
        updateTime = new Date().getTime();
    }

    public long getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public <T> T get(String name, Class<T> type) {
        return (T) this.get(name);
    }

    public <T> T get(String name) {
        return (T) super.get(name);
    }

    public boolean test(String name, Object value) {
        Object ivalue;
        if ((ivalue = this.get(name)) == null) {
            return false;
        }
        if (!ivalue.getClass().equals(value.getClass())) {
            return false;
        }
        return ivalue.equals(value);
    }

    public UUID getSessionId() {
        return sessionId;
    }
}
