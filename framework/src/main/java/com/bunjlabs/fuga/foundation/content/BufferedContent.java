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
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * Content implementation using unpooled memory byte buffer.
 */
public class BufferedContent extends BaseContent {

    private final ByteBuffer buffer;

    public BufferedContent() {
        this.buffer = ByteBuffer.allocate(0);
    }

    public BufferedContent(ByteBuffer buffer) {
        this.buffer = buffer;
    }

    @Override
    public String asString() {
        return asString(StandardCharsets.UTF_8);
    }

    @Override
    public String asString(Charset charset) {
        byte[] bytes = new byte[buffer.remaining()];
        buffer.get(bytes);
        return new String(bytes, charset);
    }

    @Override
    public InputStream asInputStream() {
        return new ByteBufferInputStream();
    }

    @Override
    public long getLength() {
        return buffer.limit();
    }

    private class ByteBufferInputStream extends InputStream {

        @Override
        public synchronized int read() throws IOException {
            if (!buffer.hasRemaining()) {
                return -1;
            }
            return buffer.get();
        }

        @Override
        public synchronized int read(byte[] bytes, int off, int len) throws IOException {
            len = Math.min(len, buffer.remaining());
            buffer.get(bytes, off, len);
            return len;
        }

        @Override
        public int available() throws IOException {
            return buffer.remaining();
        }
    }
}
