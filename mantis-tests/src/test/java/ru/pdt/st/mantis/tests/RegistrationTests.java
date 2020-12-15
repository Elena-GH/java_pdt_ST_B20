package ru.pdt.st.mantis.tests;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.lanwen.verbalregex.VerbalExpression;
import ru.pdt.st.mantis.model.MailMessage;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.List;

import static org.junit.Assert.*;

public class RegistrationTests extends TestBase {

  @BeforeMethod
  public void startMailServer() {
    app.mail().start();
  }

  @Test
  public void testRegistration() throws IOException, MessagingException {
    String user = "user1";
    String password = "password";
    String email = "user1@localhost.localdomain";
    //Заполнение и отправка формы регистрации
    app.registration().start(user, email);
    // Ожидание почтового сообщения
    List<MailMessage> mailMessages = app.mail().waitForMail(2, 10000);
    // Извлечение ссылки на подтверждение регистрации
    String confirmationLink = findConfirmationLink(mailMessages, email);
    // Установка и подтверждение пароля
    app.registration().finish(confirmationLink, password);
    // Проверка, что вход в приложение выполнен успешно
    assertTrue(app.newSession().login(user, password));
  }

  // Среди всех сообщений нужно найти то, которое отправлено на email
  // После нужно извлечь ссылку на подтверждение регистрации
  private String findConfirmationLink(List<MailMessage> mailMessages, String email) {
    MailMessage mailMessage = mailMessages.stream().filter((m) -> m.to.equals(email)).findFirst().get();
    // Поиск ссылки на подтверждение регистрации с помощью библотеки verbalregex
    // Найти подстроку "http://", за ней должны идти один или более непробельных символов
    VerbalExpression regex = VerbalExpression.regex().find("http://").nonSpace().oneOrMore().build();
    // Выполнение регулярного выражения
    return regex.getText(mailMessage.text);
  }

  @AfterMethod(alwaysRun = true)
  public void stopMailServer() {
    app.mail().stop();
  }

}
