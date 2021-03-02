package org.injector.inject;

import org.injector.expection.BindingNotFoundException;
import org.injector.provide.Provider;
import org.injector.test_class.Dao;
import org.injector.test_class.DaoImpl;
import org.injector.test_class.Service;
import org.injector.test_class.ServiceImpl;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InjectorImplTest {

    @Test
    void getProvider() {
        Injector injector = new InjectorImpl();
        injector.bind(Service.class, ServiceImpl.class);
        injector.bind(Dao.class, DaoImpl.class);
        Provider<Service> serviceProvider = injector.getProvider(Service.class);
        assertNotNull(serviceProvider);
        assertNotNull(serviceProvider.getInstance());
        assertSame(ServiceImpl.class, serviceProvider.getInstance().getClass());
    }

    @Test
    void getProviderWithBindNotFoundException() {
        Injector injector = new InjectorImpl();
        injector.bind(Service.class, ServiceImpl.class);
        assertThrows(BindingNotFoundException.class, () -> {
            injector.getProvider(Service.class);
        });
    }

    @Test
    void bind() {
        InjectorImpl injector = new InjectorImpl();
        injector.bind(Service.class, ServiceImpl.class);
        injector.bind(Dao.class, DaoImpl.class);
        assertNotNull(injector.getPrototypes());
    }

    @Test
    void bindSingleton() {
        InjectorImpl injector = new InjectorImpl();
        injector.bindSingleton(Service.class, ServiceImpl.class);
        injector.bindSingleton(Dao.class, DaoImpl.class);
        assertNotNull(injector.getSingletons());
    }
}