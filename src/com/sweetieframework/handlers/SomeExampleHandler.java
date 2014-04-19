/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sweetieframework.handlers;
import com.sweetieframework.foundation.Mapped;
import com.sweetieframework.foundation.UriHandlerBased;
import io.netty.handler.codec.http.HttpRequest;



@Mapped(uri = "/h1")
public class SomeExampleHandler  extends UriHandlerBased {

    @Override
    public void process(HttpRequest request, StringBuilder buff) {
        buff.append("HELLO HANDLER1!");
    }

}
