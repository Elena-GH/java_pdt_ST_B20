package ru.pdt.st.addressbook.tests;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.pdt.st.addressbook.model.ContactData;

import java.util.List;
import java.util.Set;

public class ContactDeletionTests extends TestBase{

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
  public void testContactDeletion() {
    Set<ContactData> befor = app.contact().all();
    ContactData deletedContact = befor.iterator().next();
    app.contact().delete(deletedContact);
    Set<ContactData> after = app.contact().all();
    Assert.assertEquals(after.size(), befor.size() - 1);

    befor.remove(deletedContact);
    // Сравнение списков групп до и после теста с помощью множеств (неупорядоченные коллекции)
    // При этом сравнение выполняется средствами тестовго фреймворка testng
    Assert.assertEquals(befor, after);
  }

}
