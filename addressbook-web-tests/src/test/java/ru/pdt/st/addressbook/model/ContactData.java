package ru.pdt.st.addressbook.model;

public class ContactData {
  private final String firstName;
  private final String lastName;
  private final String mobile;
  private final String email;

  public ContactData(String firstName, String lastName, String mobile, String email) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.mobile = mobile;
    this.email = email;
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
}
