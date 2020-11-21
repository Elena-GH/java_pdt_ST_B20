package ru.pdt.st.addressbook.tests;

import org.junit.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.pdt.st.addressbook.model.ContactData;
import ru.pdt.st.addressbook.model.Contacts;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;

public class ContactModificationTests extends TestBase {

  @BeforeMethod
  public void ensurePreconditions() {
    app.goTo().homePage();
    if (app.contact().all().size() == 0) {
      app.contact().create(new ContactData()
              .withFirstName("Contact_First_Name")
              .withLastName("Contact_Last_Name")
              .withMobile("+7 (123) 123-45-67")
              .withEmail("contact_mail@gmail.com")
              .withGroup("Group_Name"));
    }
  }

  @Test
  public void testContactModification() {
    Contacts befor = app.contact().all();
    // Выбор случайного элемента из множества
    ContactData modifiedContact = befor.iterator().next();
    ContactData contact = new ContactData()
            .withId(modifiedContact.getId())
            .withFirstName("New_Contact_First_Name")
            .withLastName("New_Contact_Last_Name")
            .withMobile("+7 (321) 123-45-67")
            .withEmail("new_contact_mail@gmail.com");
    app.contact().modify(contact);
    Contacts after = app.contact().all();
    assertThat(befor.size(), equalTo(after.size()));

    // Сравнение списков групп до и после теста с помощью множеств (неупорядоченные коллекции)
    // Для реализации fluent-интерфейса (вытягивания в цепочку) сравниваются копии множества after и befor
    // Расширение методов для HashSet реализуется через интерфейс ForwardingSet библиотеки Guava +withAdded +withOut
    // При этом сравнение выполняется средствами подключенной библиотеки Hamcrest +assertThat +equalTo
    assertThat(after, equalTo(befor.withOut(modifiedContact).withAdded(contact)));
  }

}
