package com.bunjlabs.fuga.templates;

@FunctionalInterface
public interface VariadicConsumer<T> {

    public void apply(T... args) throws Exception;
}
