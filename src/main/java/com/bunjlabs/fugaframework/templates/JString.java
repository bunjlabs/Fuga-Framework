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
package com.bunjlabs.fugaframework.templates;

import java.nio.charset.Charset;

public class JString {

    private final String data;
    
    public JString(String s) {
        this.data = s;
    }
    
    public JString(char[] c) {
        this.data = new String(c);
    }
    
    public JString(byte[] b) {
        this.data = new String(b, Charset.forName("UTF-8"));
    }
    
    @Override
    public String toString() {
        return data;
    }
    
    @Override
    public int hashCode() {
        return data.hashCode();
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (String.class != obj.getClass()) {
            return false;
        }
        final String other = (String) obj;
        return other.equals(data);
    }
}
