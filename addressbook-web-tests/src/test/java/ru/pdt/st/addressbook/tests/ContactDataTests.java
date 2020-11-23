package ru.pdt.st.addressbook.tests;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.pdt.st.addressbook.model.ContactData;

import java.util.Arrays;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class ContactDataTests extends TestBase {

  @BeforeMethod
  public void ensurePreconditions() {
    app.goTo().homePage();
    if (app.contact().all().size() == 0) {
      app.contact().create(new ContactData()
              .withFirstName("Contact_First_Name")
              .withLastName("Contact_Last_Name")
              .withAddress("123456, г.Москва, Московский пр-т, д.10, стр.1")
              .withMobilePhone("+7 (123) 123-45-67")
              .withWorkPhone("+7 (456) 123-45-67")
              .withEmail("contact_mail@gmail.com")
              .withEmail2("contact_mail_2@gmail.com")
              .withGroup("Group_Name"));
    }
  }

  @Test
  public void testContactData() {
    app.goTo().homePage();
    // Выбор случайного элемента из множества
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
    assertThat(contact.getAllEmail(), equalTo(mergeEmail(contactInfoFromEditForm)));
    assertThat(contact.getAddress(), equalTo((contactInfoFromEditForm.getAddress())));

  }

  private String mergePhones(ContactData contact) {
    return Arrays.asList(contact.getHomePhone(), contact.getMobilePhone(), contact.getWorkPhone())
            .stream().filter((s) -> ! s.equals(""))
            .map(ContactDataTests::cleaned)
            .collect(Collectors.joining("\n"));
  }

  private String mergeEmail(ContactData contact) {
    return Arrays.asList(contact.getEmail(), contact.getEmail2(), contact.getEmail3())
            .stream().collect(Collectors.joining("\n"));
  }

  public static String cleaned(String phone) {
    return phone.replaceAll("\\s", "").replaceAll("[-()]", "");
  }

}
