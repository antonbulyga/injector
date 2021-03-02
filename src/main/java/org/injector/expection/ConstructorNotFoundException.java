package org.injector.expection;

public class ConstructorNotFoundException extends RuntimeException {
    public ConstructorNotFoundException(String msg, Throwable t) {
        super(msg, t);
    }

    public ConstructorNotFoundException(String msg) {
        super(msg);
    }
}
