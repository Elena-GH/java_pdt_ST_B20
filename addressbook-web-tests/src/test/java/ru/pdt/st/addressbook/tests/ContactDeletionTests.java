package ru.pdt.st.addressbook.tests;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.pdt.st.addressbook.model.ContactData;

import java.util.List;

public class ContactDeletionTests extends TestBase{

  @BeforeMethod
  public void ensurePreconditions() {
    app.goTo().homePage();
    if (! app.contact().isThereAContact()) {
      app.contact().createContact(new ContactData(
              "Contact_First_Name",
              "Contact_Last_Name",
              "+7 (123) 123-45-67",
              "contact_mail@gmail.com",
              "Group_Name"));
    }
  }

  @Test
  public void testContactDeletion() {
    List<ContactData> befor = app.contact().list();
    app.contact().selectContact(befor.size() - 1);
    app.contact().deleteSelectedGroups();
    app.contact().confirmAction();
    app.goTo().homePage();
    List<ContactData> after = app.contact().list();
    Assert.assertEquals(after.size(), befor.size() - 1);

    befor.remove(befor.size() - 1);
    // Сравнение списков групп до и после теста с помощью списков (упорядоченные коллекции)
    // При этом сравнение выполняется средствами тестовго фреймворка testng
    Assert.assertEquals(befor, after);
  }

}
