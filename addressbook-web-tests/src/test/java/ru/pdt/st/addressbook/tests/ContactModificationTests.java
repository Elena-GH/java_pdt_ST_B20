package ru.pdt.st.addressbook.tests;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.pdt.st.addressbook.model.ContactData;

import java.util.Comparator;
import java.util.List;

public class ContactModificationTests extends TestBase {

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
  public void testContactModification() {
    List<ContactData> befor = app.contact().list();
    int index = befor.size() - 1;
    ContactData contact = new ContactData(befor.get(befor.size() - 1).getId(),
            "New_Contact_First_Name",
            "New_Contact_Last_Name",
            "+7 (321) 123-45-67",
            "new_contact_mail@gmail.com",
            null);
    app.contact().modifyContact(index, contact);
    List<ContactData> after = app.contact().list();
    Assert.assertEquals(befor.size(), after.size());

    befor.remove(index);
    befor.add(contact);
    // Сортировка списков до и после теста с помощью анонимной функции - Lambda
    Comparator<? super ContactData> byId = (c1, c2) -> Integer.compare(c1.getId(), c2.getId());
    befor.sort(byId);
    after.sort(byId);
    // Сравнение списков групп до и после теста с помощью списков (упорядоченные коллекции)
    // При этом сравнение выполняется средствами тестовго фреймворка testng
    Assert.assertEquals(befor, after);
  }

}
