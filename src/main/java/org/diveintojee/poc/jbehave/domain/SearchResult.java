/**
 * 
 */
package org.diveintojee.poc.jbehave.domain;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author louis.gueye@gmail.com
 */
@XmlRootElement
public class SearchResult {

	private long			totalHits;

	private List<Advert>	items;

	private SearchQuery		searchQuery;

	/**
	 * 
	 */
	public SearchResult() {
		super();
		this.items = new ArrayList<Advert>();
	}

	/**
	 * @return the totalHits
	 */
	public long getTotalHits() {
		return this.totalHits;
	}

	/**
	 * @param totalHits
	 *            the totalHits to set
	 */
	public void setTotalHits(long totalHits) {
		this.totalHits = totalHits;
	}

	/**
	 * @return the items
	 */
	public List<Advert> getItems() {
		return this.items;
	}

	/**
	 * @param items
	 *            the items to set
	 */
	public void setItems(List<Advert> items) {
		this.items = items;
	}

	/**
	 * @param advert
	 */
	public void addItem(Advert advert) {
		this.items.add(advert);
	}

	/**
	 * @return the searchQuery
	 */
	public SearchQuery getSearchQuery() {
		return this.searchQuery;
	}

	/**
	 * @param searchQuery
	 *            the searchQuery to set
	 */
	public void setSearchQuery(SearchQuery searchQuery) {
		this.searchQuery = searchQuery;
	}

}
