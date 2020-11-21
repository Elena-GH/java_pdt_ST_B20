package ru.pdt.st.addressbook.tests;

import org.testng.annotations.Test;
import ru.pdt.st.addressbook.model.ContactData;
import ru.pdt.st.addressbook.model.Contacts;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class ContactCreationTests extends TestBase {

  @Test
  public void testContactCreation() throws Exception {
    app.goTo().homePage();
    Contacts befor = app.contact().all();
    ContactData contact = new ContactData()
            .withFirstName("Contact_First_Name")
            .withLastName("Contact_Last_Name")
            .withMobile("+7 (123) 123-45-67")
            .withEmail("contact_mail@gmail.com")
            .withGroup("Group_Name");
    app.contact().create(contact);
    Contacts after = app.contact().all();
    assertThat(after.size(), equalTo(befor.size() + 1));

    // Сравнение списков групп до и после теста с помощью множеств (неупорядоченные коллекции)
    // Постулируется, что дабавленный элемент имеет максимальный идентификатор
    // Для реализации fluent-интерфейса (вытягивания в цепочку) сравниваются копии множества after и befor
    // Расширение методов для HashSet реализуется через интерфейс ForwardingSet библиотеки Guava +withAdded
    // При этом сравнение выполняется средствами подключенной библиотеки Hamcrest +assertThat +equalTo
    assertThat(after, equalTo(
            befor.withAdded(contact.withId(after.stream().mapToInt((c) -> c.getId()).max().getAsInt()))));
  }

}
