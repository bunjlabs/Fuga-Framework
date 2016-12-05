package com.bunjlabs.fuga.templates;

@FunctionalInterface
public interface VariadicFunction<T, R> {

    public R apply(T... args) throws Exception;
}
