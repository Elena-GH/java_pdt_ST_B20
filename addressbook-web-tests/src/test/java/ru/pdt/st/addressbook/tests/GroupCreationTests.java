package ru.pdt.st.addressbook.tests;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import ru.pdt.st.addressbook.model.GroupData;
import ru.pdt.st.addressbook.model.Groups;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class GroupCreationTests extends TestBase {

  @DataProvider
  public Iterator<Object[]> validGroups() {
    List<Object[]> list = new ArrayList<Object[]>();
    list.add(new Object[] {new GroupData()
            .withName("Group_Name_1")
            .withHeader("Group_Header_1")
            .withFooter("Group_Footer_1")});
    list.add(new Object[] {new GroupData()
            .withName("Group_Name_2'")
            .withHeader("Group_Header_2")
            .withFooter("Group_Footer_2")});
    list.add(new Object[] {new GroupData()
            .withName("Group_Name_3")
            .withHeader("Group_Header_3")
            .withFooter("Group_Footer_3")});
    return list.iterator();
  }

  @Test(dataProvider = "validGroups")
  public void testGroupCreation(GroupData group) {
    app.goTo().groupPage();
    Groups befor = app.group().all();
    app.group().create(group);
    assertThat(app.group().count(), equalTo(befor.size() + 1));
    Groups after = app.group().all();

    /*
     Сравнение списков групп до и после теста с помощью множеств (неупорядоченные коллекции)
     Постулируется, что дабавленный элемент имеет максимальный идентификатор
     Для реализации fluent-интерфейса (вытягивания в цепочку) сравниваются копии множества after и befor
     Расширение методов для HashSet реализуется через интерфейс ForwardingSet библиотеки Guava +withAdded
     При этом сравнение выполняется средствами подключенной библиотеки Hamcrest +assertThat +equalTo
    */
    assertThat(after, equalTo(
            befor.withAdded(group.withId(after.stream().mapToInt((g) -> g.getId()).max().getAsInt()))));

  }

  @Test
  public void testBadGroupCreation() throws Exception {
    app.goTo().groupPage();
    Groups befor = app.group().all();
    GroupData group = new GroupData().withName("Group_Name'");
    app.group().create(group);
    assertThat(app.group().count(), equalTo(befor.size()));
    Groups after = app.group().all();
    assertThat(after, equalTo(befor));
  }

}
