package ru.pdt.st.mantis.appmanager;

import org.apache.commons.net.telnet.TelnetClient;
import ru.pdt.st.mantis.model.MailMessage;

import javax.mail.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class JamesHelper {

  private final ApplicationManager app;

  private final TelnetClient telnet;
  private InputStream in;
  private PrintStream out;

  private final Session mailSession;
  private String mailserver;
  private Store store;


  public JamesHelper(ApplicationManager app) {
    this.app = app;
    telnet = new TelnetClient();
    // Почтовая сессия
    mailSession = Session.getDefaultInstance(System.getProperties());
  }

  public boolean doesUserExist(String name) {
    initTelnetSession();
    // Проверка на существование пользователя
    write("verify " + name);
    String result = readUntil("exist");
    closeTelnetSession();
    return result.trim().equals("User " + name + " exist");
  }

  public void createUser(String name, String passwd) {
    initTelnetSession();
    // Создание нового пользователя
    write("adduser " + name + " " + passwd);
    // Ожидание ответа
    String result = readUntil("User " + name + " added");
    closeTelnetSession();
  }

  public void deleteUser(String name) {
    initTelnetSession();
    // Удаление пользователя
    write("delusr " + name);
    String result = readUntil("User " + name + " deleted");
    closeTelnetSession();
  }

  public void initTelnetSession() {
    // Параметры соединения
    mailserver = app.getProperty("mailserver.host");
    int port = Integer.parseInt(app.getProperty("mailserver.port"));
    String login = app.getProperty("mailserver.adminlogin");
    String password = app.getProperty("mailserver.adminpassword");

    try {
      // Установка соединения с почтовым сервером
      telnet.connect(mailserver, port);
      // Входной поток для чтения
      in = telnet.getInputStream();
      // Выходной поток для записи
      out = new PrintStream(telnet.getOutputStream());
    } catch (Exception ex) {
      // TODO Auto-generated catch block
      ex.printStackTrace();
    }

    // Схема взаимодействия с James по Telnet

    // Don't know  why it doesn't allow login at the first attempt
    readUntil("Login id:");
    write(login);
    readUntil("Password:");
    write(password);
    //Read welcome message
    readUntil("Welcome "+login+". HELP for a list of commands");

    // Second login attempt, must be successful
    readUntil("Login id:");
    write(login);
    readUntil("Password:");
    write(password);

    //Read welcome message
    readUntil("Welcome "+login+". HELP for a list of commands");
  }

  // Посимвольно считывются данные из входного потока и сравниваются с заданным шаблоном
  // Как только прочитан фрагмент, который соответсвует этому шаблону, ожидание завершается
  public String readUntil(String pattern) {
    try {
      char lastChar = pattern.charAt(pattern.length() - 1);
      StringBuffer sb = new StringBuffer();
      char ch = (char) in.read();
      while (true) {
        System.out.println(ch);
        sb.append(ch);
        if (ch == lastChar) {
          if (sb.toString().endsWith(pattern)) {
            return sb.toString();
          }
        }
        ch = (char) in.read();
      }
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    return null;
  }

  // Запись команд в выходной поток
  private void write(String value) {
    try {
      out.println(value);
      out.flush();
      System.out.println(value);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  private void closeTelnetSession() {
    write("quit");
  }

  // Ожидание почтового сообщения
  public List<MailMessage> waitForMail(String username, String password, long timeout) throws MessagingException {
    // Получаем текущее время
    long now = System.currentTimeMillis();
    // В цикле проверяем, что время ожидания еще не истекло
    while (System.currentTimeMillis() < now + timeout) {
      List<MailMessage> allMail = getAllMail(username, password);
      // Если есть новые сообщения, выходим из цикла ожидания
      if (allMail.size() > 0) {
        return allMail;
      }
      try {
        Thread.sleep(1000);
      } catch (InterruptedException ex) {
        ex.printStackTrace();
      }
    }
    throw new Error("No mail :(");
  }

  // Получение сообщений из почтового ящика
  // Преобразование сообщений в модельные объекты MailMessage
  public List<MailMessage> getAllMail(String username, String password) throws MessagingException {
    // Открытие почтового ящика, согласно POP3
    Folder inbox = openInbox(username, password);
    List<MailMessage> messages = Arrays.asList(inbox.getMessages()).stream()
            .map((m) -> toModelMail(m)).collect(Collectors.toList());
    // Закрытие почтового ящика, согласно POP3
    closeFolder(inbox);
    return messages;
  }

  // Преобразование почтового сообщения в модельный объект MailMessage
  // Известно, что mantis присылает письма в обычном текстовом формате
  // Mantis присылает два письма, первое - Администратору, о том,что зарегистрирован новый пользователь,
  // второе - Пользователю, в нем содержится ссылка для продолжения регистрации
  public static MailMessage toModelMail(Message m) {
    try {
      // Для входящего сообщения получаем список получателей
      // Отбираем первого из них, так как известно, что получатель единственный
      return new MailMessage(m.getAllRecipients()[0].toString(), (String) m.getContent());
    } catch (MessagingException ex) {
      ex.printStackTrace();
      return null;
    } catch (IOException ex) {
      ex.printStackTrace();
      return null;
    }
  }

  // Открытие почтового ящика
  private Folder openInbox(String username, String password) throws MessagingException {
    store = mailSession.getStore("pop3");
    store.connect(mailserver, username, password);
    // По протоколу POP3 можно получить доступ только к папке INBOX
    Folder folder = store.getDefaultFolder().getFolder("INBOX");
    // Можно открыть INBOX только на чтение
    folder.open(Folder.READ_WRITE);
    return folder;
  }

  // Закрытие почтового ящика
  private void closeFolder(Folder folder) throws MessagingException {
    // Параметр true указывает на необходимость удалить все письма, помеченные флагом DELETED
    folder.close(true);
    store.close();
  }

  // Удаление всех писем, полученных пользователем или очистка почтового ящика
  // Применяется, если в тесте используется один и тот же почтовый ящик
  public void drainEmail(String username, String password) throws MessagingException {
    Folder inbox = openInbox(username, password);
    for (Message message : inbox.getMessages()) {
      // Каждое сообщение помечается флагом DELETED
      message.setFlag(Flags.Flag.DELETED, true);
    }
    closeFolder(inbox);
  }
}
