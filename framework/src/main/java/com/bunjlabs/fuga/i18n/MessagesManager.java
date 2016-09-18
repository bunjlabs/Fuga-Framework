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

import com.bunjlabs.fuga.FugaApp;
import com.bunjlabs.fuga.foundation.Context;
import com.bunjlabs.fuga.resources.ResourceRepresenter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MessagesManager {

    private final Logger log = LogManager.getLogger(this);

    private final Map<String, Messages> availableLangs = new HashMap<>();
    private final FugaApp app;
    private final ResourceRepresenter resourceRepresenter;

    public MessagesManager(FugaApp app) {
        this.app = app;
        this.resourceRepresenter = app.getResourceManager().getResourceRepresenter("messages");

    }

    public void load() {
        List<String> configuredLangs = app.getConfiguration().getList("fuga.i18n.available");

        configuredLangs.forEach((s) -> {
            Properties p = new Properties();
            try {
                p.load(new InputStreamReader(resourceRepresenter.load("messages." + s), StandardCharsets.UTF_8));
                availableLangs.put(s, new Messages(
                        p.entrySet().stream().collect(Collectors.toMap(
                                e -> e.getKey().toString(),
                                e -> e.getValue().toString())
                        )));
            } catch (IOException ex) {
            }
        });
    }

    public Messages getMessages(Context ctx) {
        return availableLangs.get(ctx.lang());
    }
}
