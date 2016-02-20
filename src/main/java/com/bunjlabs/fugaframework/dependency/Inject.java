package com.bunjlabs.fugaframework.dependency;

import static java.lang.annotation.ElementType.FIELD;
import java.lang.annotation.Retention;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Target;

@Target(value={FIELD})
@Retention(value=RUNTIME)
public @interface Inject {
    
}
