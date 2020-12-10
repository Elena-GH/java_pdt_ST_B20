package ru.pdt.st.addressbook.tests;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.pdt.st.addressbook.model.ContactData;
import ru.pdt.st.addressbook.model.Contacts;
import ru.pdt.st.addressbook.model.GroupData;
import ru.pdt.st.addressbook.model.Groups;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class AddContactToGroupTest extends TestBase {

  @BeforeMethod
  public void ensurePreconditions() {
    Contacts contacts = app.db().contacts();
    Groups groups = app.db().groups();
    List<ContactData> contactList = new ArrayList<>();
    for (ContactData c : contacts) {
      if (c.getGroups().size() != groups.size()) {
        contactList.add(c);
      }
    }
    if (contactList.size() == 0) {
      app.goTo().groupPage();
      app.group().create(new GroupData().withName("Group_Name"));
    }
  }

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
    ContactData contact = contactList.iterator().next();
    // Исключаем из списка групп все группы, в которые уже входит отобранный контакт
    List<GroupData> groupList = new ArrayList<>();
    for (GroupData g : groups) {
      if (!g.getContacts().contains(contact)) {
        groupList.add(g);
      }
    }
    GroupData group = groupList.iterator().next();
    Contacts befor = group.getContacts();
    app.goTo().homePage();
    app.contact().addToGroup(contact, group);
    app.goTo().homePage();
    Contacts after = app.db().group(group.getId()).getContacts();
    assertThat(after.size(), equalTo(befor.size() + 1));
  }
}
