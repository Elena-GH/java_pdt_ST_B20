package ru.pdt.st.addressbook.tests;

import org.testng.Assert;
import org.testng.annotations.Test;
import ru.pdt.st.addressbook.model.ContactData;

import java.util.Comparator;
import java.util.List;

public class ContactModificationTests extends TestBase {

  @Test
  public void testContactModification() {
    app.getNavigationHelper().gotoHomePage();
    List<ContactData> befor = app.getContactHelper().getContactList();
    if (! app.getContactHelper().isThereAContact()) {
      app.getContactHelper().createContact(new ContactData(
              "Contact_First_Name",
              "Contact_Last_Name",
              "+7 (123) 123-45-67",
              "contact_mail@gmail.com",
              "Group_Name"));
    }
    app.getContactHelper().initContactModification(befor.size() - 1);
    ContactData contact = new ContactData(
            "Elena",
            "New_Contact_Last_Name",
            "+7 (321) 123-45-67",
            "new_contact_mail@gmail.com",
            null);
    app.getContactHelper().fillContactForm(contact, false);
    app.getContactHelper().submitContactModification();
    app.getContactHelper().returnToHomePage();
    List<ContactData> after = app.getContactHelper().getContactList();
    Assert.assertEquals(befor.size(), after.size());

    befor.remove(befor.size() - 1);
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
