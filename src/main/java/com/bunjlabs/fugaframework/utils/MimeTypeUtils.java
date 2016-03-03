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
package com.bunjlabs.fugaframework.utils;

import java.util.HashMap;
import java.util.Map;

public class MimeTypeUtils {

    private final static Map<String, String> mimeTypes = new HashMap<>();

    static {
        mimeTypes.put("css", "text/css");
        mimeTypes.put("gif", "image/gif");
        mimeTypes.put("jpeg", "image/jpeg");
        mimeTypes.put("jpg", "image/jpeg");
        mimeTypes.put("png", "image/png");
        mimeTypes.put("ico", "image/x-icon");
        mimeTypes.put("bmp", "image/x-ms-bmp");
        mimeTypes.put("js", "application/x-javascript");
        mimeTypes.put("svg", "image/svg+xml");
        mimeTypes.put("mp4", "video/mp4");
    }

    public static String getMimeTypeByExt(String ext) {
        return mimeTypes.get(ext.toLowerCase());
    }
}
