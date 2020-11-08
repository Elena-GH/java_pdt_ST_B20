package ru.pdt.st.addressbook.tests;

import org.testng.annotations.*;
import ru.pdt.st.addressbook.model.GroupData;

public class GroupModificationTests extends TestBase {

  @Test
  public void testGroupmodification() throws Exception {
    app.getNavigationHelper().gotoGroupPage();
    if (! app.getGroupHelper().isThereAGroup()) {
      app.getGroupHelper().createGroup(new GroupData(
              "Group_Name",
              null,
              null));
    }
    app.getGroupHelper().selectGroup();
    app.getGroupHelper().initGroupModification();
    app.getGroupHelper().fillGroupForm(new GroupData(
            "Group_Name",
            "Group_Header",
            "New_Group_Footer"));
    app.getGroupHelper().submitGroupModification();
    app.getGroupHelper().returnToGroupPage();
  }

}
