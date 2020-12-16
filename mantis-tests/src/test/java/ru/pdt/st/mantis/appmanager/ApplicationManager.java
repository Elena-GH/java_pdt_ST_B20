package ru.pdt.st.mantis.appmanager;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.BrowserType;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class ApplicationManager {

  private final Properties properties;
  private WebDriver wd;
  private String browser;
  private RegistrationHelper registrationHelper;
  private FtpHelper ftp;
  private MailHelper mailHelper;
  private JamesHelper jamesHelper;
  private DbHelper dbHelper;

  public ApplicationManager(String browser) {
    this.browser = browser;
    properties = new Properties();
  }

  public void init() throws IOException {
    // Загрузка конфигурационных параметров из файла .properties
    // Приоритет для файла target.properties, иначе конфигурация по-умолчанию из local.properties
    String target = System.getProperty("target", "local");
    properties.load(new FileReader(new File(String.format("src/test/resources/%s.properties", target))));
  }

  // Шаблон проектирования "Ленивая инициализация"
  public WebDriver getDriver() {
    if (wd == null) {
      if (browser.equals(BrowserType.FIREFOX)) {
        wd = new FirefoxDriver();
      } else if (browser.equals(BrowserType.CHROME)) {
        wd = new ChromeDriver();
      } else if (browser.equals(BrowserType.IE)) {
        wd = new InternetExplorerDriver();
      }
      wd.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
      wd.get(properties.getProperty("web.baseUrl"));
    }
    return wd;
  }

  public void stop() {
    if (wd != null) {
      wd.quit();
    }
  }

  public HttpSession newSession() {
    return new HttpSession(this);
  }

  public String getProperty(String key) {
    return properties.getProperty(key);
  }

  // Шаблон проектирования "Ленивая инициализация"
  // Нужен только один экземпляр RegistrationHelper. Если помощник еще не инициализирован,
  // только в этом случае выполнить инициализацию
  public RegistrationHelper registration() {
    // return new RegistrationHelper(this);
    if (registrationHelper == null) {
      registrationHelper = new RegistrationHelper(this);
    }
    return registrationHelper;
  }

  // Шаблон проектирования "Ленивая инициализация"
  public FtpHelper ftp() {
    if (ftp == null) {
      ftp = new FtpHelper(this);
    }
    return ftp;
  }

  // Шаблон проектирования "Ленивая инициализация"
  public MailHelper mail() {
    if (mailHelper == null) {
      mailHelper = new MailHelper(this);
    }
    return mailHelper;
  }

  // Шаблон проектирования "Ленивая инициализация"
  public JamesHelper james() {
    if (jamesHelper == null) {
      jamesHelper = new JamesHelper(this);
    }
    return jamesHelper;
  }

  // Шаблон проектирования "Ленивая инициализация"
  public DbHelper db() {
    if (dbHelper == null) {
      dbHelper = new DbHelper(this);
    }
    return dbHelper;
  }
}
