package ru.pdt.st.addressbook.tests;

import org.testng.annotations.Test;

public class ContactDeletionTests extends TestBase{

  @Test
  public void testContactDeletion() {
    app.getContactHelper().selectContact();
    app.getContactHelper().deleteSelectedGroups();
    app.getContactHelper().confirmAction();
    app.getContactHelper().returnHomePage();
  }

}
