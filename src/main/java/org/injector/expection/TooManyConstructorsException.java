package org.injector.expection;

public class TooManyConstructorsException extends RuntimeException {
    public TooManyConstructorsException(String msg, Throwable t) {
        super(msg, t);
    }

    public TooManyConstructorsException(String msg) {
        super(msg);
    }
}
