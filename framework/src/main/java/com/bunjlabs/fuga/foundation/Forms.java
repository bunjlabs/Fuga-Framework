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

import java.util.UUID;

public class Forms {

    private static final String FORM_ID_PREFIX = "__formid__";

    private final Context ctx;

    /**
     * Create new forms for the specified context.
     *
     * @param ctx Context.
     */
    public Forms(Context ctx) {
        this.ctx = ctx;
    }

    /**
     * Generate unique form id for the given form name.
     *
     * @param formName Form name.
     * @return unique form id.
     */
    public String generateFormId(String formName) {
        String fid = UUID.randomUUID().toString();
        ctx.session().put(FORM_ID_PREFIX + formName, fid);
        return fid;
    }

    /**
     * Test form to the coincidence with form id in current context.
     *
     * @param formName Form name.
     * @param fid Form id.
     * @return true if given form id is matched with one in current context.
     */
    public boolean testFormId(String formName, String fid) {
        Object lastfid = ctx.session().get(FORM_ID_PREFIX + formName);
        if (lastfid == null) {
            return false;
        }
        ctx.session().remove(FORM_ID_PREFIX + formName);
        return fid.equals((String) lastfid);
    }
}
