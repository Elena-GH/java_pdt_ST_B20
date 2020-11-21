package ru.pdt.st.addressbook.tests;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.pdt.st.addressbook.model.GroupData;
import ru.pdt.st.addressbook.model.Groups;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.testng.Assert.assertEquals;

public class GroupModificationTests extends TestBase {

  @BeforeMethod
  public void ensurePreconditions() {
    app.goTo().groupPage();
    if (app.group().all().size() == 0) {
      app.group().create(new GroupData().withName("Group_Name"));
    }
  }

  @Test
  public void testGroupModification() throws Exception {
    Groups befor = app.group().all();
    // Выбор случайного элемента из множества
    GroupData modifiedGroup = befor.iterator().next();
    GroupData group = new GroupData()
            .withId(modifiedGroup.getId())
            .withName("New_Group_Name")
            .withHeader("Group_Header")
            .withFooter("New_Group_Footer");
    app.group().modify(group);
    assertThat(app.group().count(), equalTo(befor.size()));
    Groups after = app.group().all();

    // Сравнение списков групп до и после теста с помощью множеств (неупорядоченные коллекции)
    // Для реализации fluent-интерфейса (вытягивания в цепочку) сравниваются копии множества after и befor
    // Расширение методов для HashSet реализуется через интерфейс ForwardingSet библиотеки Guava +withAdded +withOut
    // При этом сравнение выполняется средствами подключенной библиотеки Hamcrest +assertThat +equalTo
    assertThat(after, equalTo(befor.withOut(modifiedGroup).withAdded(group)));
  }

}