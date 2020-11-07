package ru.pdt.st.addressbook.tests;

import org.testng.annotations.Test;
import ru.pdt.st.addressbook.model.ContactData;

public class ContactModificationTests extends TestBase {

  @Test
  public void testContactModification() {
    app.getContactHelper().initContactModification();
    app.getContactHelper().fillContactForm(new ContactData(
            "New_Contact_First_Name",
            "New_Contact_Last_Name",
            "+7 (321) 123-45-67",
            "new_contact_mail@gmail.com",
            null), false);
    app.getContactHelper().submitContactModification();
    app.getContactHelper().returnToHomePage();
  }

}
