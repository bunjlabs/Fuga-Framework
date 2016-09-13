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

import java.io.InputStream;
import java.nio.charset.Charset;
import org.json.JSONObject;

/**
 * Represents client http post content.
 */
public interface Content {

    /**
     * Returns current content as string.
     *
     * @return current content as string
     */
    public String asString();

    /**
     * Returns current content as string with given charset.
     *
     * @param charset content charset.
     * @return
     */
    public String asString(Charset charset);

    /**
     * Returns current content as json object.
     *
     * @return current content as json object.
     */
    public JSONObject asJson();

    /**
     * Returns current content as input stream.
     *
     * @return current content as input stream.
     */
    public InputStream asInputStream();

    /**
     * Returns length of the current content in bytes.
     *
     * @return current content length in bytes.
     */
    public long getLength();

}
