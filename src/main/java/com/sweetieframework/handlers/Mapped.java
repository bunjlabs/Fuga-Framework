package com.sweetieframework.handlers;

import com.sweetieframework.foundation.RequestMethod;
import java.lang.annotation.*;

@Target(value = ElementType.TYPE)
@Retention(value = RetentionPolicy.RUNTIME)
public @interface Mapped {

    String pattern() default "/";

    RequestMethod method() default RequestMethod.GET;
}
