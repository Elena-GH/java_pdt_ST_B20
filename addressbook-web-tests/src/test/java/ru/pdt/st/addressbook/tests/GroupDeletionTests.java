package ru.pdt.st.addressbook.tests;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.pdt.st.addressbook.model.GroupData;
import ru.pdt.st.addressbook.model.Groups;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class GroupDeletionTests extends TestBase {

  @BeforeMethod
  public void ensurePreconditions() {
    if (app.db().groups().size() ==0 ) {
      app.goTo().groupPage();
      app.group().create(new GroupData().withName("Group_Name"));
    }
  }

  @Test
  public void testGroupDeletion() throws Exception {
    Groups befor = app.db().groups();
    // Выбор случайного элемента из множества
    GroupData deletedGroup = befor.iterator().next();
    app.goTo().groupPage();
    app.group().delete(deletedGroup);
    assertThat(app.group().count(), equalTo(befor.size() - 1));
    Groups after = app.db().groups();

    /*
     Сравнение списков групп до и после теста с помощью множеств (неупорядоченные коллекции)
     Для реализации fluent-интерфейса (вытягивания в цепочку) сравниваются копии множества after и befor
     Расширение методов для HashSet реализуется через интерфейс ForwardingSet библиотеки Guava +withOut
     При этом сравнение выполняется средствами подключенной библиотеки Hamcrest +assertThat +equalTo
    */
    assertThat(after, equalTo(befor.withOut(deletedGroup)));
  }

}
