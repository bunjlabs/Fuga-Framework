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
package com.bunjlabs.fuga.sessions;

import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

public class Session extends HashMap<String, Object> {

    private long updateTime;
    private int timeout = 30; // 30 minutes
    private final UUID sessionId;

    /**
     * Create new session with specified session id.
     *
     * @param sessionId session id.
     */
    public Session(UUID sessionId) {
        updateTime = new Date().getTime();
        this.sessionId = sessionId;
    }

    /**
     * Returns last session update time.
     *
     * @return last session update time.
     */
    public long getUpdateTime() {
        return updateTime;
    }

    /**
     * Update session.
     *
     * This method actualy sets <code>updateTime</code> to the current local
     * time.
     */
    public void update() {
        updateTime = new Date().getTime();
    }

    /**
     * Returns session timeout.
     *
     * @return session timeout.
     */
    public long getTimeout() {
        return timeout;
    }

    /**
     * Set session timeout.
     *
     * @param timeout Session timeout.
     */
    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    /**
     * Returns session attribute.
     *
     * If specified session attribute does not exists, null value will be
     * returned.
     *
     * @param <T> Type of the attribute.
     * @param name Name of the attribute.
     * @param type Type of the attribute.
     * @return session attribute value or null.
     */
    public <T> T get(String name, Class<T> type) {
        return (T) this.get(name);
    }

    /**
     * Returns session attribute.
     *
     * If specified session attribute does not exists, null value will be
     * returned.
     *
     * @param <T> Type of the attribute.
     * @param name Name of the attribute.
     * @return session attribute
     */
    public <T> T get(String name) {
        return (T) super.get(name);
    }

    /**
     * Compare specified session attribute with specified object value.
     *
     * @param name Name of the attribute.
     * @param value Compared value.
     * @return true if session attribute is equal to specified object value.
     */
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

    /**
     * Returns unique session id.
     *
     * @return unique session id.
     */
    public UUID getSessionId() {
        return sessionId;
    }
}
