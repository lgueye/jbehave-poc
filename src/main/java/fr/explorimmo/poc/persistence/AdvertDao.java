/*
 *
 */
package fr.explorimmo.poc.persistence;

import java.util.List;

import fr.explorimmo.poc.domain.Advert;

/**
 * @author louis.gueye@gmail.com
 */
public interface AdvertDao {

    String BEAN_ID = "advertDao";

    /**
     * @param exampleInstance
     * @return
     */
    List<Advert> findByExample(final Advert exampleInstance);

}
