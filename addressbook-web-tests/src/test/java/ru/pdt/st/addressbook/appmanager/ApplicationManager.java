package ru.pdt.st.addressbook.appmanager;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.BrowserType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class ApplicationManager {

  private final Properties properties;
  WebDriver wd;

  private SessionHelper sessionHelper;
  private NavigationHelper navigationHelper;
  private GroupHelper groupHelper;
  private ContactHelper contactHelper;
  private String browser;
  private DbHelper dbHelper;

  public ApplicationManager(String browser) {
    this.browser = browser;
    properties = new Properties();
  }

  public SessionHelper getSessionHelper() {
    return sessionHelper;
  }

  public GroupHelper group() {
    return groupHelper;
  }

  public ContactHelper contact() {
    return contactHelper;
  }

  public NavigationHelper goTo() {
    return navigationHelper;
  }

  public DbHelper db() {
    return dbHelper;
  }

  public void init() throws IOException {
    // Загрузка конфигурационных параметров из файла .properties
    // Приоритет для файла target.properties, иначе конфигурация по-умолчанию из local.properties
    String target = System.getProperty("target", "local");
    properties.load(new FileReader(new File(String.format("src/test/resources/%s.properties", target))));

    dbHelper = new DbHelper();

    // Если selenium.server = "", то инициализируем драйвер браузера,
    // иначе инициализируем другой тип драйвера для удаленного Selenium-сервера
    if ("".equals(properties.getProperty("selenium.server"))) {
      if (browser.equals(BrowserType.FIREFOX)) {
        wd = new FirefoxDriver();
      } else if (browser.equals(BrowserType.CHROME)) {
        wd = new ChromeDriver();
      } else if (browser.equals(BrowserType.IE)) {
        wd = new InternetExplorerDriver();
      }
    } else {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        // Установка браузера. Selenium-сервер умеет определять, какой он драйвер должен использовать
        capabilities.setBrowserName(browser);
        // Установка платформы. Если не указано, использовать по-умолчанию win7
        capabilities.setPlatform(Platform.fromString(System.getProperty("platform", "")));
        wd = new RemoteWebDriver(new URL(properties.getProperty("selenium.server")), capabilities);
    }


    wd.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
    wd.get(properties.getProperty("web.baseUrl"));
    sessionHelper = new SessionHelper(wd);
    groupHelper = new GroupHelper(wd);
    contactHelper = new ContactHelper(wd);
    navigationHelper = new NavigationHelper(wd);
    sessionHelper.login(properties.getProperty("web.adminLogin"), properties.getProperty("web.adminPassword"));
  }

  public void stop() {
    sessionHelper.logout();
    wd.quit();
  }

}
