package ru.pdt.st.addressbook.appmanager;

import org.openqa.selenium.By;
import org.openqa.selenium.firefox.FirefoxDriver;
import ru.pdt.st.addressbook.model.GroupData;

public class GroupHelper {

  private FirefoxDriver wd;

  public GroupHelper( FirefoxDriver wd) {
    this.wd = wd;
  }

  public void initGroupCreation() {
    click(By.name("new"));
  }

 public void fillGroupForm(GroupData groupData) {
    type(By.name("group_name"), groupData.getName());
    type(By.name("group_header"), groupData.getHeader());
    type(By.name("group_footer"), groupData.getFooter());
  }

  public void submitGroupCreation() {
    click(By.name("submit"));
  }

  public void selectGroup() {
    click(By.name("selected[]"));
  }

  public void deleteSelectedGroups() {
    click(By.xpath("(//input[@name='delete'])[2]"));
  }

  public void returnGroupPage() {
    click(By.linkText("group page"));
  }

  private void click(By locator) {
    wd.findElement(locator).click();
  }

  private void type(By locator, String text) {
    click(locator);
    wd.findElement(locator).clear();
    wd.findElement(locator).sendKeys(text);
  }

}
