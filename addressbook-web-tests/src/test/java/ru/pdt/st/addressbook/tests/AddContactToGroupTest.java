package ru.pdt.st.addressbook.tests;

import org.testng.annotations.Test;
import ru.pdt.st.addressbook.model.ContactData;
import ru.pdt.st.addressbook.model.Contacts;
import ru.pdt.st.addressbook.model.GroupData;
import ru.pdt.st.addressbook.model.Groups;

import java.util.ArrayList;
import java.util.List;

public class AddContactToGroupTest extends TestBase {

  @Test
  public void testAddContactToGroup() {
    Contacts contacts = app.db().contacts();
    Groups groups = app.db().groups();
    // Исключаем из списка контактов все контакты, которые уже входят во все группы
    List<ContactData> contactList = new ArrayList<>();
    for (ContactData c : contacts) {
      if (c.getGroups().size() != groups.size()) {
        contactList.add(c);
      }
    }
    System.out.println(contactList);
    ContactData contact = contactList.iterator().next();
    // Исключаем из списка групп все группы, в которые уже входит отобранный контакт
    List<GroupData> groupList = new ArrayList<>();
    for (GroupData g : groups) {
      if (!g.getContacts().contains(contact)) {
        groupList.add(g);
      }
    }
    System.out.println(groupList);
    GroupData group = groupList.iterator().next();
    app.goTo().homePage();
    app.contact().addToGroup(contact, group);
    app.goTo().homePage();
  }

}
