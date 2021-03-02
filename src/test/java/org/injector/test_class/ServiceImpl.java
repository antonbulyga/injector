package org.injector.test_class;

import org.injector.inject.Inject;

public class ServiceImpl implements Service {

    private String name;
    private Dao dao;

    @Inject
    public ServiceImpl(Dao dao) {
        this.dao = dao;
    }

    @Override
    public String toString() {
        return "serv, dao";
    }
}
