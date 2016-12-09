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
package com.bunjlabs.fuga.foundation.content;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import org.apache.commons.io.IOUtils;

/**
 * Content implementation using specified input stream.
 */
public class InputStreamContent extends BaseContent {

    private final InputStream is;
    private final long length;

    /**
     * Create new InputStreamContent from input stream and length
     * 
     * @param is
     * @param length
     */
    public InputStreamContent(InputStream is, long length) {
        this.is = is;
        this.length = length;
    }

    @Override
    public String asString() {
        return asString(StandardCharsets.UTF_8);
    }

    @Override
    public String asString(Charset charset) {
        try {
            return IOUtils.toString(is, charset);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public InputStream asInputStream() {
        return is;
    }

    @Override
    public long getLength() {
        return length;
    }

}
