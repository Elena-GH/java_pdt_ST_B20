package ru.pdt.st.addressbook;

import org.testng.annotations.*;
import org.openqa.selenium.*;

public class GroupDeletionTests extends TestBase{

  @Test
  public void testGroupDeletion() throws Exception {
    gotoGroupPage();
    selectGroup();
    deleteSelectedGroups();
    returnGroupPage();
  }

}
