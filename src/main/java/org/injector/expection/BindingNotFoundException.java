package org.injector.expection;

public class BindingNotFoundException extends RuntimeException {
    public BindingNotFoundException(String msg, Throwable t) {
        super(msg, t);
    }

    public BindingNotFoundException(String msg) {
        super(msg);
    }
}
