package io.github.rhkiswani.javaff.security.encode;

import io.github.rhkiswani.javaff.factory.AbstractFactory;

public class EncodeFactory extends AbstractFactory<EncodeHandler> {

    private static EncodeFactory factory = new EncodeFactory();

    private EncodeFactory(){

    }

    public static EncodeFactory instance(){
        return factory;
    }

    @Override
    public EncodeHandler getDefault(Class targetClazz) {
        return new StringEncodeHandler();
    }
}
