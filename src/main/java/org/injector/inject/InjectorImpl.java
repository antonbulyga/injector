package org.injector.inject;

import org.injector.expection.BindingNotFoundException;
import org.injector.expection.ConstructorNotFoundException;
import org.injector.expection.TooManyConstructorsException;
import org.injector.provide.Provider;
import org.injector.provide.ProviderImpl;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InjectorImpl implements Injector {

    private final Map<Class<?>, Class<?>> prototypes = new HashMap<>();
    private final Map<Class<?>, Class<?>> singletons = new HashMap<>();
    private final Map<Class<?>, Object> singletonResults = new ConcurrentHashMap<>();

    @Override
    public synchronized <T> Provider<T> getProvider(Class<T> type) {

        Class<T> aClass = (Class<T>) prototypes.get(type);
        if (aClass == null) {
            aClass = (Class<T>) singletons.get(type);
            if (aClass == null) {
                return null;
            }
            return getProviderSingleton(type);
        }

        Provider<T> provider = new ProviderImpl<>();
        Object result = createResult(aClass);
        provider.setInstance((T) result);

        return provider;
    }


    private <T> Provider<T> getProviderSingleton(Class<T> type) {
        Class<T> instanceInContainer = (Class<T>) singletonResults.get(type);
        Provider<T> provider = new ProviderImpl<>();
        if (instanceInContainer == null) {
            Class<T> aClass = (Class<T>) singletons.get(type);
            if (aClass == null) return null;

            Object result = createResult(aClass);
            provider.setInstance((T) result);
        } else {
            provider.setInstance((T) instanceInContainer);
        }
        return provider;
    }

    private <T> Object createResult(Class<T> aClass) {
        T result = null;
        List<Constructor<T>> constructors = getConstructorsWithInject(aClass);

        if (constructors.isEmpty()) {
            result = createWithoutParameters(aClass);

        } else {

            for (Constructor<T> constructor : constructors) {
                try {
                    Class<?>[] parameterTypes = constructor.getParameterTypes();

                    for (int i = 0; i < parameterTypes.length; i++) {
                        Class<?> paramClazz = prototypes.get(parameterTypes[i]);
                        if (paramClazz == null) {
                            throw new BindingNotFoundException("Binding not found exception");
                        }
                        parameterTypes[i] = paramClazz;
                    }

                    Object[] parameterObjects = new Object[parameterTypes.length];
                    for (int i = 0; i < parameterTypes.length; i++) {
                        parameterObjects[i] = createResult(parameterTypes[i]);
                    }
                    result = constructor.newInstance(parameterObjects);
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }

        }
        return result;
    }

    private <T> T createWithoutParameters(Class<T> type) {
        try {
            Constructor<T> constructor = type.getDeclaredConstructor();
            return constructor.newInstance();

        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new ConstructorNotFoundException("No default constructor");
        }
    }


    @Override
    public <T> void bind(Class<T> type, Class<? extends T> impl) {
        List<Constructor<T>> withInject = getConstructorsWithInject(impl);
        if (withInject.size() > 1) {
            throw new TooManyConstructorsException("Too many constructors");
        }
        prototypes.put(type, impl);
    }

    @Override
    public <T> void bindSingleton(Class<T> type, Class<? extends T> impl) {
        List<Constructor<T>> withInject = getConstructorsWithInject(impl);
        if (withInject.size() > 1) {
            throw new TooManyConstructorsException("Too many constructors");
        }
        singletons.put(type, impl);
    }

    private <T> List<Constructor<T>> getConstructorsWithInject(Class<? extends T> impl) {
        List<Constructor<T>> withInject = new ArrayList<>();
        Constructor<T>[] constructors = (Constructor<T>[]) impl.getConstructors();
        for (Constructor<T> constructor : constructors) {
            if (constructor.getAnnotation(Inject.class) != null) {
                withInject.add(constructor);
            }
        }
        return withInject;
    }

    public Map<Class<?>, Class<?>> getPrototypes() {
        return prototypes;
    }

    public Map<Class<?>, Class<?>> getSingletons() {
        return singletons;
    }

    public Map<Class<?>, Object> getSingletonResults() {
        return singletonResults;
    }
}
