package ru.pdt.st.addressbook.tests;

import org.testng.Assert;
import org.testng.annotations.*;
import ru.pdt.st.addressbook.model.GroupData;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

public class GroupModificationTests extends TestBase {

  @Test
  public void testGroupmodification() throws Exception {
    app.getNavigationHelper().gotoGroupPage();
    if (! app.getGroupHelper().isThereAGroup()) {
      app.getGroupHelper().createGroup(new GroupData(
              "Group_Name",
              null,
              null));
    }
    List<GroupData> befor = app.getGroupHelper().getGroupList();
    app.getGroupHelper().selectGroup(befor.size() - 1);
    app.getGroupHelper().initGroupModification();
    GroupData group = new GroupData(befor.get(befor.size() - 1).getId(),
            "Group_Name",
            "Group_Header",
            "New_Group_Footer");
    app.getGroupHelper().fillGroupForm(group);
    app.getGroupHelper().submitGroupModification();
    app.getGroupHelper().returnToGroupPage();
    List<GroupData> after = app.getGroupHelper().getGroupList();
    Assert.assertEquals(after.size(), befor.size());

    befor.remove(befor.size() - 1);
    befor.add(group);
    // Сортировка списков до и после теста с помощью анонимной функции - Lambda
    Comparator<? super GroupData> byId = (g1, g2) -> Integer.compare(g1.getId(), g2.getId());
    befor.sort(byId);
    after.sort(byId);
    // Сравнение списков групп до и после теста с помощью списков (упорядоченные коллекции)
    Assert.assertEquals(befor, after);
  }

}
