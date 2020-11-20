package ru.pdt.st.addressbook.tests;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.pdt.st.addressbook.model.GroupData;

import java.util.Comparator;
import java.util.List;

public class GroupModificationTests extends TestBase {

  @BeforeMethod
  public void ensurePreconditions() {
    app.goTo().groupPage();
    if (app.group().list().size() == 0) {
      app.group().create(new GroupData(
              "Group_Name",
              null,
              null));
    }
  }

  @Test
  public void testGroupmodification() throws Exception {
    List<GroupData> befor = app.group().list();
    int index = befor.size() - 1;
    GroupData group = new GroupData(befor.get(index).getId(),
            "New_Group_Name",
            "Group_Header",
            "New_Group_Footer");

    app.group().modify(index, group);
    List<GroupData> after = app.group().list();
    Assert.assertEquals(after.size(), befor.size());

    befor.remove(index);
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