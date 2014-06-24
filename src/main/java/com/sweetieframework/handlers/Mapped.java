/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sweetieframework.handlers;

import com.sweetieframework.foundation.RequestMethod;
import java.lang.annotation.*;
 
@Target(value= ElementType.TYPE)
@Retention(value= RetentionPolicy.RUNTIME)
public @interface Mapped {
    String uri() default "/";
    RequestMethod method() default RequestMethod.GET;
}