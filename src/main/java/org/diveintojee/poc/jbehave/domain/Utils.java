/*
 *
 */
package org.diveintojee.poc.jbehave.domain;

/**
 * @author louis.gueye@gmail.com
 */
public abstract class Utils {

    public static final String capitalize(final Class<?> type) {

        if (type == null)
            return null;

        return type.getSimpleName().toUpperCase();

    }

    public static final String minimize(final Class<?> type) {

        if (type == null)
            return null;

        return type.getSimpleName().toLowerCase();

    }

    public static final String pluralize(final Class<?> type) {

        if (type == null)
            return null;

        return minimize(type).concat("s");

    }

}
