package ru.pdt.st.addressbook.tests;

import biz.futureware.mantis.rpc.soap.client.IssueData;
import org.hamcrest.MatcherAssert;
import org.openqa.selenium.remote.BrowserType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.*;
import ru.pdt.st.addressbook.appmanager.ApplicationManager;
import ru.pdt.st.addressbook.model.ContactData;
import ru.pdt.st.addressbook.model.GroupData;
import ru.pdt.st.addressbook.model.Groups;

import javax.xml.rpc.ServiceException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.equalTo;

public class TestBase {

  Logger logger = LoggerFactory.getLogger(TestBase.class);

  protected static final ApplicationManager app
          = new ApplicationManager(System.getProperty("browser", BrowserType.FIREFOX));

  @BeforeSuite(alwaysRun = true)
  public void setUp() throws Exception {
    app.init();
  }

  @AfterSuite(alwaysRun = true)
  public void tearDown() throws Exception {
    app.stop();
  }

  @BeforeMethod
  public void logTestStart(Method m, Object[] p) {
    logger.info("Start test " + m.getName() + " with parameters " + Arrays.asList(p));
  }

  @AfterMethod(alwaysRun = true)
  public void logTestStop(Method m) {
    logger.info("Stop test " + m.getName());
  }

  public void verifyGroupListInUI() {
    if (Boolean.getBoolean("verifyUI")) {
      Groups dbGroups = app.db().groups();
      Groups uiGroups = app.group().all();
      MatcherAssert.assertThat(uiGroups, equalTo(dbGroups.stream()
              .map((g) -> new GroupData().withId(g.getId()).withName(g.getName()))
              .collect(Collectors.toSet())));
    }
  }

  public void verifyContactListInUI() {
    if (Boolean.getBoolean("verifyUI")) {
      Set<ContactData> dbContacts = app.db().contacts().stream()
              .map((c) -> new ContactData().withId(c.getId()).withFirstName(c.getFirstName())
              .withLastName(c.getLastName()).withAddress(c.getAddress()))
              .collect(Collectors.toSet());
      Set<ContactData> uiContacts = app.contact().all().stream()
              .map((c) -> new ContactData().withId(c.getId()).withFirstName(c.getFirstName())
                      .withLastName(c.getLastName()).withAddress(c.getAddress()))
              .collect(Collectors.toSet());
      MatcherAssert.assertThat(uiContacts, equalTo(dbContacts));
    }
  }

  @Test
  public void issues() throws RemoteException, ServiceException, MalformedURLException {
    System.out.println(app.soap().getIssues());
  }

  /*
  public boolean isIssueOpen(int issueId) {

    return false;
  }

  public void skipIfNotFixed(int issueId) {
    if (isIssueOpen(issueId)) {
      throw new SkipException("Ignored because of issue " + issueId);
    }
  }
  */
}
