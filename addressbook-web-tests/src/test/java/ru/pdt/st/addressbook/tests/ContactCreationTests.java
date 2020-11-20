package ru.pdt.st.addressbook.tests;

import org.testng.Assert;
import org.testng.annotations.Test;
import ru.pdt.st.addressbook.model.ContactData;

import java.util.Comparator;
import java.util.List;

public class ContactCreationTests extends TestBase {

  @Test (enabled = false)
  public void testContactCreation() throws Exception {
    app.goTo().homePage();
    List<ContactData> befor = app.contact().list();
    ContactData contact = new ContactData(
            "Contact_First_Name",
            "Contact_Last_Name",
            "+7 (123) 123-45-67",
            "contact_mail@gmail.com",
            "Group_Name");
    app.contact().createContact(contact);
    List<ContactData> after = app.contact().list();
    Assert.assertEquals(after.size(), befor.size() + 1);

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
