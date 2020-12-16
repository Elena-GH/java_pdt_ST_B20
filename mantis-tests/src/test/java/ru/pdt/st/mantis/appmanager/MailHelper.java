package ru.pdt.st.mantis.appmanager;

import org.subethamail.wiser.Wiser;
import org.subethamail.wiser.WiserMessage;
import ru.pdt.st.mantis.model.MailMessage;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class MailHelper {

  private ApplicationManager app;
  private final Wiser wiser;

  // инициализация почтового сервера Wiser
  public MailHelper(ApplicationManager app) {
    this.app = app;
    wiser = new Wiser();
  }

  // Ожидание почтового сообщения
  public List<MailMessage> waitForMail(int count, long timeout) throws MessagingException, IOException {
    // Получаем текущее время
    long start = System.currentTimeMillis();
    // В цикле проверяем, что время ожидания еще не истекло
    while (System.currentTimeMillis() < start + timeout) {
      // Если есть новые сообщения, выходим из цикла ожидания
      if (wiser.getMessages().size() >= count) {
        // Преобразование почтового сообщения в модельный объект MailMessage
        return wiser.getMessages().stream().map((m) -> toModelMail(m)).collect(Collectors.toList());
      }
      try {
        // Подождать 1000мс и повторно проверить входящие сообщения
        Thread.sleep(1000);
      } catch (InterruptedException ex) {
        ex.printStackTrace();
      }
    }
    throw new Error("No mail :(");
  }

  // Преобразование почтового сообщения в модельный объект MailMessage
  // Известно, что mantis присылает письма в обычном текстовом формате
  // Mantis присылает два письма, первое - Администратору, о том,что зарегистрирован новый пользователь,
  // второе - Пользователю, в нем содержится ссылка для продолжения регистрации
  public static MailMessage toModelMail(WiserMessage m) {
    try {
      MimeMessage mm = m.getMimeMessage();
      // Для входящего сообщения получаем список получателей
      // Отбираем первого из них, так как известно, что получатель единственный
      return new MailMessage(mm.getAllRecipients()[0].toString(), (String) mm.getContent());
      // Перехват возможных ошибок при чтении почтового сообщения
    } catch (MessagingException ex) {
      ex.printStackTrace();
      return null;
    } catch (IOException ex) {
      ex.printStackTrace();
      return null;
    }
  }

  // Запуск почтового сервера
  public void start() {
    wiser.start();
  }

  // Остановка почтового сервере
  public void stop() {
    wiser.stop();
  }

}
