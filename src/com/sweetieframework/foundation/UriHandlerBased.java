/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sweetieframework.foundation;

import io.netty.handler.codec.http.HttpRequest;
 
public abstract class UriHandlerBased{
 
    public abstract void process(HttpRequest request, StringBuilder buff);
 
    public String getContentType() {
        return "text/plain; charset=UTF-8";
    }
}
