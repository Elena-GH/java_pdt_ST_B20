package ru.pdt.st.addressbook.tests;

import org.testng.annotations.Test;
import ru.pdt.st.addressbook.model.ContactData;
import ru.pdt.st.addressbook.model.Contacts;
import ru.pdt.st.addressbook.model.GroupData;
import ru.pdt.st.addressbook.model.Groups;

import java.util.ArrayList;
import java.util.List;

public class RemoveContactFromGroupTest extends TestBase {

  @Test
  public void testRemoveContactFromGroup() {
    Contacts contacts = app.db().contacts();
    Groups groups = app.db().groups();
    // Исключаем из списка групп все группы, в которых нет ни одного контакта
    List<GroupData> groupList = new ArrayList<>();
    for (GroupData g : groups) {
      if (g.getContacts().size() != 0) {
        groupList.add(g);
      }
    }
    GroupData group = groupList.iterator().next();
    ContactData contact = group.getContacts().iterator().next();
    app.goTo().homePage();
    app.contact().removeFromGroup(group, contact);
  }
}
