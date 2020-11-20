package ru.pdt.st.addressbook.tests;

import org.testng.Assert;
import org.testng.annotations.*;
import ru.pdt.st.addressbook.model.GroupData;

import java.util.List;

public class GroupDeletionTests extends TestBase {

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
  public void testGroupDeletion() throws Exception {
    List<GroupData> befor = app.group().list();
    int index = befor.size() - 1;
   app.group().delete(index);
    List<GroupData> after = app.group().list();
    Assert.assertEquals(after.size(), befor.size() - 1);

    befor.remove(index);
    // Сравнение списков групп до и после теста с помощью списков (упорядоченные коллекции)
    // При этом сравнение выполняется средствами тестовго фреймворка testng
    Assert.assertEquals(befor, after);
  }

}
