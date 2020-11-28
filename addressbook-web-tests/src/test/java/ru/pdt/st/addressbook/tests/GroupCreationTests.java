package ru.pdt.st.addressbook.tests;

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
  public Iterator<Object[]> validGroups() throws IOException {
    BufferedReader reader = new BufferedReader(new FileReader(new File("src/test/resources/groups.xml")));
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
