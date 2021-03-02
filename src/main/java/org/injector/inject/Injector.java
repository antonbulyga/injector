package org.injector.inject;

import org.injector.provide.Provider;

public interface Injector {

    <T> Provider<T> getProvider(Class<T> type);
    <T> void bind(Class<T> type, Class<? extends T> impl);
    <T> void bindSingleton(Class<T> type, Class<? extends T> impl);

}