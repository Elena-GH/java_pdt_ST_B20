package ru.pdt.st.addressbook.tests;

import org.testng.Assert;
import org.testng.annotations.Test;
import ru.pdt.st.addressbook.model.GroupData;

import java.util.Comparator;
import java.util.List;

public class GroupCreationTests extends TestBase {

  @Test
  public void testGroupCreation() throws Exception {
    app.getNavigationHelper().gotoGroupPage();
    List<GroupData> befor = app.getGroupHelper().getGroupList();
    GroupData group = new GroupData(
            "Group_Name",
            null,
            null);
    app.getGroupHelper().createGroup(group);
    List<GroupData> after = app.getGroupHelper().getGroupList();
    Assert.assertEquals(after.size(), befor.size() + 1);

    befor.add(group);
    // Сортировка списков до и после теста с помощью анонимной функции - Lambda
    Comparator<? super GroupData> byId = (g1, g2) -> Integer.compare(g1.getId(), g2.getId());
    befor.sort(byId);
    after.sort(byId);
    // Сравнение списков групп до и после теста с помощью списков (упорядоченные коллекции)
    // При этом сравнение выполняется средствами тестовго фреймворка testng
    Assert.assertEquals(befor, after);
  }

}
