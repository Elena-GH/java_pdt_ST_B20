package ru.pdt.st.addressbook.tests;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.thoughtworks.xstream.XStream;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import ru.pdt.st.addressbook.model.GroupData;
import ru.pdt.st.addressbook.model.Groups;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class GroupCreationTests extends TestBase {

  @DataProvider
  public Iterator<Object[]> validGroupsFromXml() throws IOException {
    try (BufferedReader reader = new BufferedReader(new FileReader(new File("src/test/resources/groups.xml")))) {
      String xml = "";
      String line = reader.readLine();
      while (line != null) {
        xml += line;
        line = reader.readLine();
      }
      XStream xstream = new XStream();
      xstream.processAnnotations(GroupData.class);
      List<GroupData> groups = (List<GroupData>) xstream.fromXML(xml);
      return groups.stream().map((g) -> new Object[] {g}).collect(Collectors.toList()).iterator();
    }
  }

  @DataProvider
  public Iterator<Object[]> validGroupsFromJson() throws IOException {
    try (BufferedReader reader = new BufferedReader(new FileReader(new File("src/test/resources/groups.json")))) {
      String json = "";
      String line = reader.readLine();
      while (line != null) {
        json += line;
        line = reader.readLine();
      }
      Gson gson = new Gson();
      // List<GroupData>.class - для обычных классов применимо, а для классов с угловыми скобками "<>",
      // т.е. которые содержат уточняющий тип, в частности для контейнерных типов, - не применимо,
      // поэтому нужно использовать обходной путь или "костыль" - new TypeToken<List<GroupData>>() {}.getType()
      List<GroupData> groups = gson.fromJson(json, new TypeToken<List<GroupData>>() {}.getType());
      return groups.stream().map((g) -> new Object[] {g}).collect(Collectors.toList()).iterator();
    }
  }

  @Test(dataProvider = "validGroupsFromJson")
  public void testGroupCreation(GroupData group) {
    app.goTo().groupPage();
    Groups before = app.db().groups();
    app.group().create(group);
    assertThat(app.group().count(), equalTo(before.size() + 1));
    Groups after = app.db().groups();
    /*
      Сравнение списков групп до и после теста с помощью множеств (неупорядоченные коллекции)
      Постулируется, что дабавленный элемент имеет максимальный идентификатор
      Для реализации fluent-интерфейса (вытягивания в цепочку) сравниваются копии множества after и before
      Расширение методов для HashSet реализуется через интерфейс ForwardingSet библиотеки Guava +withAdded
      При этом сравнение выполняется средствами подключенной библиотеки Hamcrest +assertThat +equalTo
    */
    assertThat(after, equalTo(
            before.withAdded(group.withId(after.stream().mapToInt((g) -> g.getId()).max().getAsInt()))));
    /*
      Контроль списков объектов на UI. Отключаемая проверка
      Основана на сравнении списков UI по данным БД
    */
    verifyGroupListInUI();
  }

  @Test
  public void testBadGroupCreation() throws Exception {
    app.goTo().groupPage();
    Groups before = app.db().groups();
    GroupData group = new GroupData().withName("Group_Name'");
    app.group().create(group);
    assertThat(app.group().count(), equalTo(before.size()));
    Groups after = app.db().groups();
    assertThat(after, equalTo(before));
  }

}
