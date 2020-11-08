package ru.pdt.st.addressbook.tests;

import org.testng.annotations.*;
import ru.pdt.st.addressbook.model.ContactData;

public class ContactCreationTests extends TestBase {

  @Test
  public void testContactCreation() throws Exception {
    app.getNavigationHelper().gotoHomePage();
    app.getContactHelper().createContact(new ContactData(
            "Contact_First_Name",
            "Contact_Last_Name",
            "+7 (123) 123-45-67",
            "contact_mail@gmail.com",
            "Group_Name"));
  }

}
