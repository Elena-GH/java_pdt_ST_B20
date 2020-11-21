package ru.pdt.st.addressbook.tests;

import org.testng.Assert;
import org.testng.annotations.Test;
import ru.pdt.st.addressbook.model.ContactData;

import java.util.Comparator;
import java.util.List;
import java.util.Set;

public class ContactCreationTests extends TestBase {

  @Test
  public void testContactCreation() throws Exception {
    app.goTo().homePage();
    Set<ContactData> befor = app.contact().all();
    ContactData contact = new ContactData()
            .withFirstName("Contact_First_Name")
            .withLastName("Contact_Last_Name")
            .withMobile("+7 (123) 123-45-67")
            .withEmail("contact_mail@gmail.com")
            .withGroup("Group_Name");
    app.contact().create(contact);
    Set<ContactData> after = app.contact().all();
    Assert.assertEquals(after.size(), befor.size() + 1);

    // Постулируется, что дабавленный элемент имеет максимальный идентификатор
    // Поиск объекта с максимальным идентификатором
    contact.withId(after.stream().mapToInt((c) -> c.getId()).max().getAsInt());
    befor.add(contact);
    // Сравнение списков групп до и после теста с помощью множеств (неупорядоченные коллекции)
    // При этом сравнение выполняется средствами тестовго фреймворка testng
    Assert.assertEquals(befor, after);
  }

}
