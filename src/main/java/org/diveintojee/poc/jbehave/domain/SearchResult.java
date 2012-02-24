/**
 *
 */
package org.diveintojee.poc.jbehave.domain;

import org.diveintojee.poc.jbehave.domain.representation.Link;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author louis.gueye@gmail.com
 */
@XmlRootElement
public class SearchResult extends AbstractObject {

  private long totalHits;

  private List<Advert> items;

  private List<Link> links;

  private SearchQuery searchQuery;

  /**
   *
   */
  public SearchResult() {
    super();
    items = new ArrayList<Advert>();
  }

  /**
   * @param advert
   */
  public void addItem(final Advert advert) {
    items.add(advert);
  }

  /**
   * @return the items
   */
  public List<Advert> getItems() {
    return items;
  }

  /**
   * @return the searchQuery
   */
  public SearchQuery getSearchQuery() {
    return searchQuery;
  }

  /**
   * @return the totalHits
   */
  public long getTotalHits() {
    return totalHits;
  }

  /**
   * @param items the items to set
   */
  public void setItems(final List<Advert> items) {
    this.items = items;
  }

  /**
   * @param searchQuery the searchQuery to set
   */
  public void setSearchQuery(final SearchQuery searchQuery) {
    this.searchQuery = searchQuery;
  }

  /**
   * @param totalHits the totalHits to set
   */
  public void setTotalHits(final long totalHits) {
    this.totalHits = totalHits;
  }

  public List<Link> getLinks() {
    return links;
  }

  public void setLinks(List<Link> links) {
    this.links = links;
  }

  public void buildLinks() {
    addFirstPageLink();
    addPreviousLink();
    addNextLink();
    addLastPageLink();
  }

  private void addLastPageLink() {
    //To change body of created methods use File | Settings | File Templates.
  }

  private void addNextLink() {
    //To change body of created methods use File | Settings | File Templates.
  }

  private void addPreviousLink() {
    //To change body of created methods use File | Settings | File Templates.
  }

  private void addFirstPageLink() {
    //To change body of created methods use File | Settings | File Templates.
  }
}
