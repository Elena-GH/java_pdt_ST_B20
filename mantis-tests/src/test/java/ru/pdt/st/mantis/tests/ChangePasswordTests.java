package ru.pdt.st.mantis.tests;

import org.junit.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.lanwen.verbalregex.VerbalExpression;
import ru.pdt.st.mantis.model.MailMessage;
import ru.pdt.st.mantis.model.UserData;

import javax.mail.MessagingException;
import javax.xml.rpc.ServiceException;
import java.io.IOException;
import java.util.List;

public class ChangePasswordTests extends TestBase {

  @BeforeMethod
  public void startMailServer() {
    app.mail().start();
  }

  @Test
  public void testChangePassword() throws IOException, MessagingException, InterruptedException, ServiceException {
    // Проверка на статус баг-репорта Mantis. Если статус отличается от Resolved (код 80), тест выполнять не нужно
    skipIfNotFixed(app.soap().issueId());
    // Получение информации о пользователе из БД
    UserData user = app.db().user();
    // Подготовка параметров
    String email = user.getEmail();
    String password = "new_password";
    // Авторизация в приложении
    app.changePassword().loginUI("administrator", "root");
    // Инициализация смены пароля пользователя
    app.changePassword().start(user.getId());
    // Ожидание почтового сообщения
    List<MailMessage> mailMessages = app.mail().waitForMail(1, 10000);
    // Извлечение ссылки на подтверждение регистрации
    String confirmationLink = findConfirmationLink(mailMessages, email);
    // Установка и подтверждение пароля
    app.changePassword().finish(confirmationLink, password);
    // Проверка, что вход в приложение выполнен успешно
    Assert.assertTrue(app.newSession().login(user.getUsername(), password));
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
