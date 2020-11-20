package ru.pdt.st.addressbook.tests;

import org.testng.annotations.Test;
import ru.pdt.st.addressbook.model.GroupData;
import ru.pdt.st.addressbook.model.Groups;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class GroupCreationTests extends TestBase {

  @Test
  public void testGroupCreation() throws Exception {
    app.goTo().groupPage();
    Groups befor = app.group().all();
    GroupData group = new GroupData().withName("Group_Name");
    app.group().create(group);
    Groups after = app.group().all();
    assertThat(after.size(), equalTo(befor.size() + 1));

    // Сравнение списков групп до и после теста с помощью множеств (неупорядоченные коллекции)
    // Для реализации fluent-интерфейса (вытягивания в цепочку) сравниваются копии множества after и befor
    // Расширение методов для HashSet реализуется через интерфейс ForwardingSet библиотеки Guava +withAdded
    // При этом сравнение выполняется средствами подключенной библиотеки Hamcrest +assertThat +equalTo
    assertThat(after, equalTo(
            befor.withAdded(group.withId(after.stream().mapToInt((g) -> g.getId()).max().getAsInt()))));

  }

}
