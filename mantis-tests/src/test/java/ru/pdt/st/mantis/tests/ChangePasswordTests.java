package ru.pdt.st.mantis.tests;

import org.testng.annotations.Test;
import ru.pdt.st.mantis.model.UserData;

import java.io.IOException;

public class ChangePasswordTests extends TestBase {

  @Test
  public void testChangePassword() throws IOException {
    app.newSession().login("administator", "root");
    UserData user = app.db().user();
    System.out.println(user);
  }

}
