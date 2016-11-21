package io.github.rhkiswani.javaff.json.exceptions;

import io.github.rhkiswani.javaff.exceptions.SmartException;

public class JsonException extends SmartException{
    public JsonException(String errorMsg, Object... errorMsgParams) {
        super(errorMsg, errorMsgParams);
    }

    public JsonException(Throwable e) {
        super(e);
    }
}
