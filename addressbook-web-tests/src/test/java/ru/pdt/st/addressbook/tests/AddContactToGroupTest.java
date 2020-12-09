package ru.pdt.st.addressbook.tests;

import org.testng.annotations.Test;
import ru.pdt.st.addressbook.model.ContactData;
import ru.pdt.st.addressbook.model.GroupData;

public class AddContactToGroupTest extends TestBase {

  @Test
  public void testAddContactToGroup() {
    ContactData contact = app.db().contacts().iterator().next();
    GroupData group = app.db().groups().iterator().next();
    app.goTo().homePage();
    app.contact().addToGroup(contact, group);
    app.goTo().homePage();
  }
}
