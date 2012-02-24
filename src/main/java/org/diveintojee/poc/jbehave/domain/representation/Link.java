package org.diveintojee.poc.jbehave.domain.representation;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.xml.bind.annotation.XmlAttribute;

/**
 * User: lgueye Date: 24/02/12 Time: 18:43
 */
public class Link {

  @XmlAttribute
  private String href;
  @XmlAttribute
  private String type;
  @XmlAttribute
  private String rel;

  public String getHref() {
    return href;
  }

  public void setHref(String href) {
    this.href = href;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getRel() {
    return rel;
  }

  public void setRel(String rel) {
    this.rel = rel;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).
        append("rel", rel).
        append("href", href).
        append("type", type).
        toString();
  }
}
