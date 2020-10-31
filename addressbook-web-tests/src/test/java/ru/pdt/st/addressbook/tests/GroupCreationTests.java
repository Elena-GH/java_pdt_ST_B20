package ru.pdt.st.addressbook.tests;

import org.testng.annotations.*;
import ru.pdt.st.addressbook.model.GroupData;

public class GroupCreationTests extends TestBase{

  @Test
  public void testGroupCreation() throws Exception {
    app.gotoGroupPage();
    app.getGroupHelper().initGroupCreation();
    app.getGroupHelper().fillGroupForm(new GroupData("Group_Name", "Group_Header", "Group_Footer"));
    app.getGroupHelper().submitGroupCreation();
    app.getGroupHelper().returnGroupPage();
    app.logout();
  }

}
