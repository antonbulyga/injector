package org.injector.provide;

public class ProviderImpl<T> implements Provider<T> {

    private T instance;

    @Override
    public T getInstance() {
        return instance;
    }

    @Override
    public void setInstance(T instance) {
        this.instance = instance;
    }
}
