package ru.pdt.st.addressbook.tests;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.pdt.st.addressbook.model.ContactData;
import ru.pdt.st.addressbook.model.Contacts;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class ContactDeletionTests extends TestBase{

  @BeforeMethod
  public void ensurePreconditions() {
    if (app.db().contacts().size() == 0) {
      app.goTo().homePage();
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
  public void testContactDeletion() {
    Contacts before = app.db().contacts();
    // Выбор случайного элемента из множества
    ContactData deletedContact = before.iterator().next();
    app.goTo().homePage();
    app.contact().delete(deletedContact);
    assertThat(app.contact().count(), equalTo(before.size() - 1));
    Contacts after = app.db().contacts();
    /*
      Сравнение списков групп до и после теста с помощью множеств (неупорядоченные коллекции)
      Для реализации fluent-интерфейса (вытягивания в цепочку) сравниваются копии множества after и befor
      Расширение методов для HashSet реализуется через интерфейс ForwardingSet библиотеки Guava +withOut
      При этом сравнение выполняется средствами подключенной библиотеки Hamcrest +assertThat +equalTo
    */
    assertThat(after, equalTo(before.withOut(deletedContact)));
    /*
      Контроль списков объектов на UI. Отключаемая проверка
      Основана на сравнении списков UI по данным БД
    */
    verifyContactListInUI();
  }

}
