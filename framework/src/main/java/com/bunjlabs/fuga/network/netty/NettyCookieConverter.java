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
package com.bunjlabs.fuga.network.netty;

import com.bunjlabs.fuga.foundation.Cookie;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class NettyCookieConverter {

    public static io.netty.handler.codec.http.cookie.Cookie convertToNetty(Cookie c) {
        io.netty.handler.codec.http.cookie.Cookie nc
                = new io.netty.handler.codec.http.cookie.DefaultCookie(c.getName(), c.getValue());

        nc.setDomain(c.getDomain());
        nc.setHttpOnly(c.isHttpOnly());
        nc.setMaxAge(c.getMaxAge());
        nc.setPath(c.getPath());
        nc.setSecure(c.isSecure());
        nc.setWrap(c.getWrap());

        return nc;
    }

    public static List<io.netty.handler.codec.http.cookie.Cookie> convertListToNetty(Cookie... cs) {
        return Stream.of(cs).map(NettyCookieConverter::convertToNetty).collect(Collectors.toList());
    }

    public static List<io.netty.handler.codec.http.cookie.Cookie> convertListToNetty(List<Cookie> cs) {
        return cs.stream().map(NettyCookieConverter::convertToNetty).collect(Collectors.toList());
    }

    public static Cookie convertToFuga(io.netty.handler.codec.http.cookie.Cookie nc) {
        Cookie c = new Cookie(nc.name(), nc.value());

        c.setDomain(nc.domain());
        c.setHttpOnly(nc.isHttpOnly());
        c.setMaxAge(nc.maxAge());
        c.setPath(nc.path());
        c.setSecure(nc.isSecure());
        c.setWrap(nc.wrap());

        return c;
    }

    public static List<Cookie> convertListToFuga(io.netty.handler.codec.http.cookie.Cookie... ncs) {
        return Stream.of(ncs).map(NettyCookieConverter::convertToFuga).collect(Collectors.toList());
    }

    public static List<Cookie> convertListToFuga(List<io.netty.handler.codec.http.cookie.Cookie> ncs) {
        return ncs.stream().map(NettyCookieConverter::convertToFuga).collect(Collectors.toList());
    }
}
