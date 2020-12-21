package ru.pdt.st.mantis.tests;

import org.openqa.selenium.remote.BrowserType;
import org.testng.SkipException;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import ru.lanwen.verbalregex.VerbalExpression;
import ru.pdt.st.mantis.appmanager.ApplicationManager;
import ru.pdt.st.mantis.model.MailMessage;

import javax.xml.rpc.ServiceException;
import java.io.File;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.util.List;

public class TestBase {

  protected static final ApplicationManager app
          = new ApplicationManager(System.getProperty("browser", BrowserType.FIREFOX));

  @BeforeSuite(alwaysRun = true)
  public void setUp() throws Exception {
    app.init();
    app.ftp().upload(new File("src/test/resources/config_inc.php"),
                                 "config_inc.php",
                                "config_inc.php.bak");
  }

  public boolean isIssueOpen(BigInteger issueId) throws RemoteException, ServiceException, MalformedURLException {
    // Если статус баг-репорта отличается от Resolved (код 80), возвращаем true
    if (app.soap().issueStatus(issueId) != 80) {
      return true;
    }
    return false;
  }

  public void skipIfNotFixed(BigInteger issueId) throws RemoteException, ServiceException, MalformedURLException {
    // Если есть баг-репорт в статусе, отличном от Resolved (код 80), тест выполнять не нужно
    if (isIssueOpen(issueId)) {
      throw new SkipException("Ignored because of issue " + issueId);
    }
  }

  @AfterSuite(alwaysRun = true)
  public void tearDown() throws Exception {
    app.ftp().restore("config_inc.php.bak", "config_inc.php");
    app.stop();
  }

}
