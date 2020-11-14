package ru.pdt.st.addressbook.tests;

import org.testng.Assert;
import org.testng.annotations.*;
import ru.pdt.st.addressbook.model.GroupData;

import java.util.List;

public class GroupDeletionTests extends TestBase {

  @Test
  public void testGroupDeletion() throws Exception {
    app.getNavigationHelper().gotoGroupPage();
    if (! app.getGroupHelper().isThereAGroup()) {
      app.getGroupHelper().createGroup(new GroupData(
              "Group_Name",
              null,
              null));
    }
    List<GroupData> befor = app.getGroupHelper().getGroupList();
    app.getGroupHelper().selectGroup(befor.size() - 1);
    app.getGroupHelper().deleteSelectedGroups();
    app.getGroupHelper().returnToGroupPage();
    List<GroupData> after = app.getGroupHelper().getGroupList();
    Assert.assertEquals(after.size(), befor.size() - 1);

    befor.remove(befor.size() - 1);
    for (int i = 0; i < after.size(); i++) {
      Assert.assertEquals(befor.get(i), after.get(i));
    }
  }

}
