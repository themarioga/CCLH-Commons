package org.themarioga.cclh.commons.util;

import org.themarioga.cclh.commons.exceptions.ApplicationException;

public class Assert {

    public static void assertNotNull(Object object, String message) {
        if (object == null) throw new ApplicationException() {
            @Override
            public String getMessage() {
                return message;
            }
        };
    }

    public static void assertNull(Object object, String message) {
        if (object != null) throw new ApplicationException() {
            @Override
            public String getMessage() {
                return message;
            }
        };
    }

    public static void assertNotEmpty(String string, String message) {
        if (string.isBlank()) throw new ApplicationException() {
            @Override
            public String getMessage() {
                return message;
            }
        };
    }

    public static void assertEmpty(String string, String message) {
        if (string != null) throw new ApplicationException() {
            @Override
            public String getMessage() {
                return message;
            }
        };
    }

}
