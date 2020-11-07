package ru.pdt.st.addressbook.tests;

import org.testng.annotations.*;
import ru.pdt.st.addressbook.model.GroupData;

public class GroupModificationTests extends TestBase {

  @Test
  public void testGroupmodification() throws Exception {
    app.getNavigationHelper().gotoGroupPage();
    app.getGroupHelper().selectGroup();
    app.getGroupHelper().inutGroupModification();
    app.getGroupHelper().fillGroupForm(new GroupData(
            "Group_Name",
            "Group_Header",
            "New_Group_Footer"));
    app.getGroupHelper().submitGroupModification();
    app.getGroupHelper().returnGroupPage();
  }

}
