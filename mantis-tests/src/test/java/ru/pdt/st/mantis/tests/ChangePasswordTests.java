package ru.pdt.st.mantis.tests;

import org.testng.annotations.Test;
import ru.pdt.st.mantis.model.MailMessage;
import ru.pdt.st.mantis.model.UserData;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.List;

import static org.testng.Assert.assertTrue;

public class ChangePasswordTests extends TestBase {

  @Test
  public void testChangePassword() throws IOException, MessagingException {
    UserData user = app.db().user();
    // Автризация в приложении
    app.changePassword().loginUI("administrator", "root");
    // Инициализация смены пароля пользователя
    app.changePassword().start(user.getId());
    System.out.println(user);
  }

}
