package ru.pdt.st.addressbook.tests;

import org.testng.Assert;
import org.testng.annotations.*;
import ru.pdt.st.addressbook.model.GroupData;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class GroupCreationTests extends TestBase {

  @Test
  public void testGroupCreation() throws Exception {
    app.getNavigationHelper().gotoGroupPage();
    List<GroupData> befor = app.getGroupHelper().getGroupList();
    GroupData group = new GroupData(
            "Group_Name",
            null,
            null);
    app.getGroupHelper().createGroup(group);
    List<GroupData> after = app.getGroupHelper().getGroupList();
    Assert.assertEquals(after.size(), befor.size() + 1);

    int max = 0;
    for (GroupData g : after) {
      if (g.getId() > max) {
        max = g.getId();
      }
    }
    group.setId(max);
    befor.add(group);
    Assert.assertEquals(new HashSet<Object>(befor), new HashSet<Object>(after));
  }

}
