package ru.pdt.st.addressbook;

import org.testng.annotations.*;

public class GroupCreationTests extends TestBase{

  @Test
  public void testGroupCreation() throws Exception {
    app.gotoGroupPage();
    app.initGroupCreation();
    app.fillGroupForm(new GroupData("Group_Name", "Group_Header", "Group_Footer"));
    app.submitGroupCreation();
    app.returnGroupPage();
    app.logout();
  }

}
