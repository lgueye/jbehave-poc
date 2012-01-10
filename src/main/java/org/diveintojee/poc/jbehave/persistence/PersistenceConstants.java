/**
 * 
 */
package org.diveintojee.poc.jbehave.persistence;

import org.diveintojee.poc.jbehave.domain.Advert;

/**
 * @author louis.gueye@gmail.com
 */
public class PersistenceConstants {

    public static final String PERSISTANCE_UNIT_NAME = "jbehave-poc-persistence-unit";

    public static final String ADVERTS_TYPE_NAME = Advert.class.getSimpleName();

    public static final String ADVERTS_INDEX_NAME = ADVERTS_TYPE_NAME + "s";

}
