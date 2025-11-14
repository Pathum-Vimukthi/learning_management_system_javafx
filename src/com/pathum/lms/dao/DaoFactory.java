package com.pathum.lms.dao;

import com.pathum.lms.dao.custom.impl.EntrollDaoImpl;
import com.pathum.lms.dao.custom.impl.StudentDaoImpl;
import com.pathum.lms.dao.custom.impl.UserDaoImpl;
import com.pathum.lms.utils.DaoType;

public class DaoFactory {
    private static DaoFactory daoFactory;
    private DaoFactory() {}
    public static DaoFactory getInstance() {
        if (daoFactory == null) {
            daoFactory = new DaoFactory();
        }
        return daoFactory;
    }
    public <T>T getDao(DaoType daoType) {
        switch (daoType) {
            case USER:
                return (T) new UserDaoImpl();
            case STUDENT:
                return (T) new StudentDaoImpl();
            case ENTROLL:
                return (T) new EntrollDaoImpl();
            default:
                return null;
        }
    }
}
