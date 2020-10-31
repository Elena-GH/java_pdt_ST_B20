package ru.pdt.st.addressbook;

import org.testng.annotations.*;
import org.openqa.selenium.*;

public class GroupDeletionTests extends TestBase{

  @Test
  public void testGroupDeletion() throws Exception {
    wd.findElement(By.linkText("groups")).click();
    wd.findElement(By.name("selected[]")).click();
    wd.findElement(By.xpath("(//input[@name='delete'])[2]")).click();
    wd.findElement(By.linkText("group page")).click();
  }

}
