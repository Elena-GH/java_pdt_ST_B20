package ru.pdt.st.addressbook.tests;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.pdt.st.addressbook.model.ContactData;

import java.util.Set;

public class ContactModificationTests extends TestBase {

  @BeforeMethod
  public void ensurePreconditions() {
    app.goTo().homePage();
    if (app.contact().list().size() == 0) {
      app.contact().create(new ContactData()
              .withFirstName("Contact_First_Name")
              .withLastName("Contact_Last_Name")
              .withMobile("+7 (123) 123-45-67")
              .withEmail("contact_mail@gmail.com")
              .withGroup("Group_Name"));
    }
  }

  @Test
  public void testContactModification() {
    Set<ContactData> befor = app.contact().all();
    // Выбор случайного элемента из множества
    ContactData modifiedContact = befor.iterator().next();
    ContactData contact = new ContactData()
            .withId(modifiedContact.getId())
            .withFirstName("New_Contact_First_Name")
            .withLastName("New_Contact_Last_Name")
            .withMobile("+7 (321) 123-45-67")
            .withEmail("new_contact_mail@gmail.com");
    app.contact().modify(contact);
    Set<ContactData> after = app.contact().all();
    Assert.assertEquals(befor.size(), after.size());

    befor.remove(modifiedContact);
    befor.add(contact);
    // Сравнение списков групп до и после теста с помощью множеств (неупорядоченные коллекции)
    // При этом сравнение выполняется средствами тестовго фреймворка testng
    Assert.assertEquals(befor, after);
  }

}
