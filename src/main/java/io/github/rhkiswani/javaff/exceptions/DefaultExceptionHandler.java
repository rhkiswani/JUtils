package io.github.rhkiswani.javaff.exceptions;

class DefaultExceptionHandler implements ExceptionHandler{

    public void handle(Throwable t){
        t.printStackTrace();
    }
}
