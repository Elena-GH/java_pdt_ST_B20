package ru.pdt.st.addressbook.tests;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.pdt.st.addressbook.model.ContactData;
import ru.pdt.st.addressbook.model.Contacts;
import ru.pdt.st.addressbook.model.GroupData;
import ru.pdt.st.addressbook.model.Groups;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class RemoveContactFromGroupTest extends TestBase {

  @BeforeMethod
  public void ensurePreconditions() {
    Contacts contacts = app.db().contacts();
    Groups groups = app.db().groups();
    List<GroupData> groupList = new ArrayList<>();
    for (GroupData g : groups) {
      if (g.getContacts().size() != 0) {
        groupList.add(g);
      }
    }
    if (groupList.size() == 0 && contacts.size() != 0) {
      ContactData contact = contacts.iterator().next();
      GroupData group = groups.iterator().next();
      app.goTo().homePage();
      app.contact().addToGroup(contact, group);
    }
    if (contacts.size() == 0) {
      app.goTo().homePage();
      app.contact().create(new ContactData()
              .withFirstName("Contact_First_Name")
              .withLastName("Contact_Last_Name")
              .withAddress("New Contact for Remove from Group")
              .withGroup("Group_Name"));
    }
  }

  @Test
  public void testRemoveContactFromGroup() {
    // Contacts contacts = app.db().contacts();
    Groups groups = app.db().groups();
    // Исключаем из списка групп все группы, в которых нет ни одного контакта
    List<GroupData> groupList = new ArrayList<>();
    for (GroupData g : groups) {
      if (g.getContacts().size() != 0) {
        groupList.add(g);
      }
    }
    GroupData group = groupList.iterator().next();
    Contacts beforeContacts = group.getContacts();
    ContactData contact = group.getContacts().iterator().next();
    Groups beforeGroups = contact.getGroups();
    app.goTo().homePage();
    app.contact().removeFromGroup(group, contact);
    Contacts afterContacts = app.db().group(group.getId()).getContacts();
    Groups afterGroups = app.db().contact(contact.getId()).getGroups();
    assertThat(afterContacts.size(), equalTo(beforeContacts.size() - 1));
    assertThat(afterGroups.size(), equalTo(beforeGroups.size() - 1));
  }
}
