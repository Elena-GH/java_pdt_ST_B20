package ru.pdt.st.addressbook.model;

public class ContactData {
  private final String firstName;
  private final String lastName;
  private final String mobile;
  private final String email;
  private String group;

  public ContactData(String firstName, String lastName, String mobile, String email, String group) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.mobile = mobile;
    this.email = email;
    this.group = group;
  }

  public String getFirstName() {
    return firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public String getMobile() {
    return mobile;
  }

  public String getEmail() {
    return email;
  }

  public String getGroup() {
    return group;
  }

}
