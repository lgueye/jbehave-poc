/*
 *
 */
package org.diveintojee.poc.jbehave.domain.validation;


/**
 * @author louis.gueye@gmail.com
 */
public enum ValidationContext {
    CREATE(new Class<?>[] { Create.class }),
    UPDATE(new Class<?>[] { Update.class }),
    DELETE(new Class<?>[] { Delete.class }),
    ALL(new Class<?>[] { Create.class, Update.class, Delete.class });

    private Class<?>[] context;

    private ValidationContext(final Class<?>... context) {
        this.context = context;
    }

    public Class<?>[] getContext() {
        final Class<?>[] copy = new Class[context.length];
        System.arraycopy(context, 0, copy, 0, context.length);
        return copy;
    }

}
