package com.rex.core.components;

/**
 * Simple entity class to be used as item data for the ComboBox.
 */
public class CountryBean {

  private Long id;
  private String value;

  public CountryBean() {
    super();
  }

  public CountryBean(Long id, String name) {
    super();
    this.id = id;
    this.value = name;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return value;
  }

  public void setName(String name) {
    this.value = name;
  }

  @Override
  public String toString() {
    return value;
  }

}