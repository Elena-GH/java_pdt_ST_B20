package ru.pdt.st.addressbook.appmanager;

import biz.futureware.mantis.rpc.soap.client.IssueData;
import biz.futureware.mantis.rpc.soap.client.MantisConnectLocator;
import biz.futureware.mantis.rpc.soap.client.MantisConnectPortType;
import biz.futureware.mantis.rpc.soap.client.ProjectData;
import ru.pdt.st.addressbook.model.Issue;

import javax.xml.rpc.ServiceException;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class SoapHelper {

  private final ApplicationManager app;

  // Конструктор для SoapHelper
  public SoapHelper(ApplicationManager app) {
    this.app = app;
  }

  public Set<Issue> getIssues() throws RemoteException, MalformedURLException, ServiceException {
    // Установка соединения с Mantis SOAP
    MantisConnectPortType mc = new MantisConnectLocator()
            .getMantisConnectPort(new URL("http://localhost/mantisbt-2.24.3/api/soap/mantisconnect.php"));
    // Получение ИД проекта addressbook
    BigInteger projectId = mc.mc_project_get_id_from_name("administrator", "root", "addressbook");
    // Получение списка баг-репортов по проекту addressbook
    IssueData[] issues = mc.mc_project_get_issues("administrator", "root", projectId,
            BigInteger.valueOf(1), BigInteger.valueOf(0));
    return Arrays.asList(issues).stream().map((i) -> new Issue()
            .withId(i.getId().intValue()).withStatus(String.valueOf(i.getStatus())))
            .collect(Collectors.toSet());
  }


}
