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
package com.bunjlabs.fuga.router;

public class RoutesMapLoadException extends Exception {

    public RoutesMapLoadException() {
    }

    public RoutesMapLoadException(Throwable cause) {
        super(cause);
    }

    public RoutesMapLoadException(String msg) {
        super(msg);
    }

    public RoutesMapLoadException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public RoutesMapLoadException(Tokenizer t, String msg) {
        super("(" + t.line + ":" + t.column + ") " + msg);
    }

    public RoutesMapLoadException(Tokenizer t, String msg, Throwable cause) {
        super("(" + t.line + ":" + t.column + ") " + msg, cause);
    }
}
