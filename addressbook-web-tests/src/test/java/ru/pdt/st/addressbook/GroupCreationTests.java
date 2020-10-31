package ru.pdt.st.addressbook;

import org.testng.annotations.*;

public class GroupCreationTests extends TestBase{

  @Test
  public void testGroupCreation() throws Exception {
    gotoGroupPage();
    initGroupCreation();
    fillGroupForm(new GroupData("Group_Name", "Group_Header", "Group_Footer"));
    submitGroupCreation();
    returnGroupPage();
    logout();
  }

}
