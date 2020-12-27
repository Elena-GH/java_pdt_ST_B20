package ru.pdt.st.addressbook.rf;

import org.openqa.selenium.remote.BrowserType;
import ru.pdt.st.addressbook.appmanager.ApplicationManager;
import ru.pdt.st.addressbook.model.GroupData;

import java.io.IOException;

public class AddressbookKeywords {

  // Инициализация библиотеки AddressbookKeywords
  // Библиотека инициализируется один раз для всех тестовых сценариев Robot Framework
  public static final String ROBOT_LIBRARY_SCOPE = "GLOBAL";

  private ApplicationManager app;

  public void initApplicationManager() throws IOException {
    app = new ApplicationManager(System.getProperty("browser", BrowserType.FIREFOX));
    app.init();
  }

  public void stopApplicationManager() {
    app.stop();
    // Окончательное уничтожение объекта
    app = null;
  }

  public int getGroupCount() {
    app.goTo().groupPage();
    return app.group().count();
  }

  public void createGroup(String name, String header, String footer) {
    app.goTo().groupPage();
    app.group().create(new GroupData().withName(name).withHeader(header).withFooter(footer));
  }
}
