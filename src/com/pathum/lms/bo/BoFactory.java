package com.pathum.lms.bo;

import com.pathum.lms.bo.custom.impl.UserBoImpl;
import com.pathum.lms.utils.BoType;

public class BoFactory {
    private static BoFactory boFactory;
    private BoFactory() {}
    public static BoFactory getInstance() {
        if (boFactory == null) {
            boFactory = new BoFactory();
        }
        return boFactory;
    }
    public <T> T getBo(BoType boType) {
        switch (boType) {
            case USER:
                return (T) new UserBoImpl();
            default:
                return null;
        }
    }
}
