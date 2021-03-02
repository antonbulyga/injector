package org.injector.test_class;

import org.injector.inject.Injector;
import org.injector.inject.InjectorImpl;
import org.injector.provide.Provider;

public class Main {

    public static void main(String[] args) {
        Injector injector = new InjectorImpl();
        injector.bind(Service.class, ServiceImpl.class);
        injector.bind(Dao.class, DaoImpl.class);
        injector.bindSingleton(Dao.class,DaoImpl.class);
        injector.bindSingleton(Service.class,ServiceImpl.class);

        Provider<Service> provider = injector.getProvider(Service.class);
        Provider<Service> providerSingleton = injector.getProvider(Service.class);
    }

}