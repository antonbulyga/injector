package org.injector.provide;

public interface Provider<T> {

    T getInstance();

    void setInstance(T instance);

}
