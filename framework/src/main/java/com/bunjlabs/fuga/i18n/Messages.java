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
package com.bunjlabs.fuga.i18n;

import java.text.MessageFormat;
import java.util.Map;

public class Messages {

    private final Map<String, String> messages;

    public Messages(Map<String, String> messages) {
        this.messages = messages;
    }

    public String get(String name) {
        String msg = messages.get(name);
        return msg == null ? name : msg;
    }

    public String get(String name, Object... args) {
        String msg = messages.get(name);
        return msg == null ? name : MessageFormat.format(messages.get(name), args);
    }

}
