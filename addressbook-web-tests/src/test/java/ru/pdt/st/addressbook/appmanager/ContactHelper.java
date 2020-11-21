package ru.pdt.st.addressbook.appmanager;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import ru.pdt.st.addressbook.model.ContactData;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ContactHelper extends HelperBase {

  public ContactHelper(WebDriver wd) {
    super(wd);
  }

  public void initContactCreation() {
    click(By.linkText("add new"));
  }

  public void fillContactForm(ContactData contactData, boolean creation) {
    type(By.name("firstname"), contactData.getFirstName());
    type(By.name("lastname"), contactData.getLastName());
    type(By.name("mobile"), contactData.getMobile());
    type(By.name("email"), contactData.getEmail());

    if (creation) {
      new Select(wd.findElement(By.name("new_group"))).selectByVisibleText(contactData.getGroup());
    } else {
      Assert.assertFalse(isElementPresent(By.name("new_group")));
    }
  }

  public void submitContactCreation() {
    click(By.xpath("(//input[@name='submit'])[2]"));
  }

  public void initContactModification(int index) {
    wd.findElements(By.xpath("//img[@alt='Edit']")).get(index).click();
  }

  public void submitContactModification() {
    click(By.xpath("(//input[@name='update'])[2]"));
  }

  public void selectContact(int index) {
    wd.findElements(By.name("selected[]")).get(index).click();
  }

  public void selectContactById(int id) {
    wd.findElement(By.cssSelector("input[id='" + id + "']")).click();
  }

  public void deleteSelectedGroups() {
    click(By.xpath("//input[@value='Delete']"));
  }

  public void confirmAction() {
    wd.switchTo().alert().accept();
    wd.findElement(By.cssSelector("div.msgbox"));
  }

  public void returnToHomePage() {
    click(By.linkText("home page"));
  }

  public void create(ContactData contact) {
    initContactCreation();
    fillContactForm(contact, true);
    submitContactCreation();
    returnToHomePage();
  }

  public void modify(int index, ContactData contact) {
    initContactModification(index);
    fillContactForm(contact, false);
    submitContactModification();
    returnToHomePage();
  }

  public void delete(int index) {
    selectContact(index);
    deleteSelectedGroups();
    confirmAction();
  }

  public void delete(ContactData contact) {
    selectContactById(contact.getId());
    deleteSelectedGroups();
    confirmAction();
  }

  public boolean isThereAContact() {
    return isElementPresent(By.name("selected[]"));
  }

  public List<ContactData> list() {
    List<ContactData> contacts = new ArrayList<>();
    List<WebElement> elements = wd.findElements(By.name("entry"));
    for (WebElement element : elements) {
      String lastName = element.findElement(By.xpath("td[2]")).getText();
      String firstName = element.findElement(By.xpath("td[3]")).getText();
      int id = Integer.parseInt(element.findElement(By.tagName("input")).getAttribute("id"));
      contacts.add(new ContactData()
              .withId(id)
              .withFirstName(firstName)
              .withLastName(lastName));
    }
   return contacts;
  }

  public Set<ContactData> all() {
    Set<ContactData> contacts = new HashSet<ContactData>();
    List<WebElement> elements = wd.findElements(By.name("entry"));
    for (WebElement element : elements) {
      String lastName = element.findElement(By.xpath("td[2]")).getText();
      String firstName = element.findElement(By.xpath("td[3]")).getText();
      int id = Integer.parseInt(element.findElement(By.tagName("input")).getAttribute("id"));
      contacts.add(new ContactData()
              .withId(id)
              .withFirstName(firstName)
              .withLastName(lastName));
    }
    return contacts;
  }

}
