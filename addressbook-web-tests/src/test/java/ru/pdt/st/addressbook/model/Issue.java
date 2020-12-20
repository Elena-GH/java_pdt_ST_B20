package ru.pdt.st.addressbook.model;

public class Issue {

  private int id;
  private String status;


  public int getId() {
    return id;
  }

  public Issue withId(int id) {
    this.id = id;
    return this;
  }

  public String getStatus() {
    return status;
  }

  public Issue withStatus(String status) {
    this.status = status;
    return this;
  }

}
