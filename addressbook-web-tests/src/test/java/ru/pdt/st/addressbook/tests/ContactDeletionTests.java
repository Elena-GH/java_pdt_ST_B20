package ru.pdt.st.addressbook.tests;

import org.testng.Assert;
import org.testng.annotations.Test;
import ru.pdt.st.addressbook.model.ContactData;

import java.util.List;

public class ContactDeletionTests extends TestBase{

  @Test (enabled = false)
  public void testContactDeletion() {
    app.getNavigationHelper().gotoHomePage();
    if (! app.getContactHelper().isThereAContact()) {
      app.getContactHelper().createContact(new ContactData(
              "Contact_First_Name",
              "Contact_Last_Name",
              "+7 (123) 123-45-67",
              "contact_mail@gmail.com",
              "Group_Name"));
    }
    List<ContactData> befor = app.getContactHelper().getContactList();
    app.getContactHelper().selectContact(befor.size() - 1);
    app.getContactHelper().deleteSelectedGroups();
    app.getContactHelper().confirmAction();
    app.getNavigationHelper().gotoHomePage();
    List<ContactData> after = app.getContactHelper().getContactList();
    Assert.assertEquals(after.size(), befor.size() - 1);

    befor.remove(befor.size() - 1);
    // Сравнение списков групп до и после теста с помощью списков (упорядоченные коллекции)
    // При этом сравнение выполняется средствами тестовго фреймворка testng
    Assert.assertEquals(befor, after);
  }

}
