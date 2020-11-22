package ru.pdt.st.addressbook.tests;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.pdt.st.addressbook.model.ContactData;

import java.util.Arrays;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class ContactPhoneTests extends TestBase {

  @BeforeMethod
  public void ensurePreconditions() {
    app.goTo().homePage();
    if (app.contact().all().size() == 0) {
      app.contact().create(new ContactData()
              .withFirstName("Contact_First_Name")
              .withLastName("Contact_Last_Name")
              .withMobilePhone("+7 (123) 123-45-67")
              .withEmail("contact_mail@gmail.com")
              .withGroup("Group_Name"));
    }
  }

  @Test
  public void testContactPhones() {
    app.goTo().homePage();
    ContactData contact = app.contact().all().stream().iterator().next();
    ContactData contactInfoFromEditForm = app.contact().infoFromEditForm(contact);

    /*
     Метод обратой проверки.
     Проверяется, что если склеить все телефоны со страницы модификации контакта,
     в результате получиться строка с номерами телефонов на главной страницы
     Ранее проверялось, что если разрезать строку с номерами телефона с главной страницы,
     в результате получиться массив номеров телефонов со страницы модификации контакта
     Этот вариант приводил к падению метода public Contacts all() для строки,
     у которой заданы менее 3-х телефонов
    */
    assertThat(contact.getAllPhones(), equalTo(mergePhones(contactInfoFromEditForm)));
  }

  private String mergePhones(ContactData contact) {
    return Arrays.asList(contact.getHomePhone(), contact.getMobilePhone(), contact.getWorkPhone())
            .stream().filter((s) -> ! s.equals(""))
            .map(ContactPhoneTests::cleaned)
            .collect(Collectors.joining("\n"));
  }

  public static String cleaned(String phone) {
    return phone.replaceAll("\\s", "").replaceAll("[-()]", "");
  }

}
