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
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class NettyCookieConverter {

    public static io.netty.handler.codec.http.cookie.Cookie convertToNetty(Cookie c) {
        io.netty.handler.codec.http.cookie.Cookie nc
                = new io.netty.handler.codec.http.cookie.DefaultCookie(c.name(), c.value());

        nc.setDomain(c.domain());
        nc.setHttpOnly(c.isHttpOnly());
        nc.setMaxAge(c.maxAge());
        nc.setPath(c.path());
        nc.setSecure(c.isSecure());
        nc.setWrap(c.wrap());

        return nc;
    }

    public static Collection<io.netty.handler.codec.http.cookie.Cookie> convertListToNetty(Cookie... cs) {
        return Stream.of(cs).map(NettyCookieConverter::convertToNetty).collect(Collectors.toList());
    }

    public static Collection<io.netty.handler.codec.http.cookie.Cookie> convertListToNetty(Collection<Cookie> cs) {
        return cs.stream().map(NettyCookieConverter::convertToNetty).collect(Collectors.toList());
    }

    public static Cookie convertToFuga(io.netty.handler.codec.http.cookie.Cookie nc) {
        Cookie c = new Cookie(nc.name(), nc.value());

        c.domain(nc.domain());
        c.isHttpOnly(nc.isHttpOnly());
        c.maxAge(nc.maxAge());
        c.path(nc.path());
        c.isSecure(nc.isSecure());
        c.wrap(nc.wrap());

        return c;
    }

    public static Collection<Cookie> convertListToFuga(io.netty.handler.codec.http.cookie.Cookie... ncs) {
        return Stream.of(ncs).map(NettyCookieConverter::convertToFuga).collect(Collectors.toList());
    }

    public static Collection<Cookie> convertListToFuga(Collection<io.netty.handler.codec.http.cookie.Cookie> ncs) {
        return ncs.stream().map(NettyCookieConverter::convertToFuga).collect(Collectors.toList());
    }
}
