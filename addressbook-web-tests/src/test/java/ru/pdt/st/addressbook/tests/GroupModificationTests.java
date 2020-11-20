package ru.pdt.st.addressbook.tests;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.pdt.st.addressbook.model.GroupData;

import java.util.Set;

public class GroupModificationTests extends TestBase {

  @BeforeMethod
  public void ensurePreconditions() {
    app.goTo().groupPage();
    if (app.group().all().size() == 0) {
      app.group().create(new GroupData().withName("Group_Name"));
    }
  }

  @Test
  public void testGroupmodification() throws Exception {
    Set<GroupData> befor = app.group().all();
    // Получение случайного идентификатора группы
    GroupData modifiedGroup = befor.iterator().next();
    GroupData group = new GroupData()
            .withId(modifiedGroup.getId())
            .withName("New_Group_Name")
            .withHeader("Group_Header")
            .withFooter("New_Group_Footer");
    app.group().modify(group);
    Set<GroupData> after = app.group().all();
    Assert.assertEquals(after.size(), befor.size());

    befor.remove(modifiedGroup);
    befor.add(group);
    // Сравнение списков групп до и после теста с помощью множеств (неупорядоченные коллекции)
    // При этом сравнение выполняется средствами тестовго фреймворка testng
    Assert.assertEquals(befor, after);
  }

}