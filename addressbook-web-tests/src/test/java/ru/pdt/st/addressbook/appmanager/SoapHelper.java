package ru.pdt.st.addressbook.appmanager;

import biz.futureware.mantis.rpc.soap.client.IssueData;
import biz.futureware.mantis.rpc.soap.client.MantisConnectLocator;
import biz.futureware.mantis.rpc.soap.client.MantisConnectPortType;

import javax.xml.rpc.ServiceException;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.Arrays;

public class SoapHelper {

  private final ApplicationManager app;

  // Конструктор для SoapHelper
  public SoapHelper(ApplicationManager app) {
    this.app = app;
  }

  private MantisConnectPortType getMantisConnect() throws ServiceException, MalformedURLException {
    // Установка соединения с Mantis SOAP
    //MantisConnectPortType mc = new MantisConnectLocator()
    //        .getMantisConnectPort(new URL("http://localhost/mantisbt-2.24.3/api/soap/mantisconnect.php"));
    MantisConnectPortType mc = new MantisConnectLocator()
            .getMantisConnectPort(new URL(app.getProperty("mantis.soap.url")));
    return mc;
  }

  public BigInteger issueId() throws MalformedURLException, ServiceException, RemoteException {
    // Открытие соединения
    MantisConnectPortType mc = getMantisConnect();
    // Получение ИД проекта addressbook
    BigInteger projectId = mc.mc_project_get_id_from_name
            (app.getProperty("mantis.soap.adminLogin"), app.getProperty("mantis.soap.adminPassword"),
                    "addressbook");
    // Получение списка баг-репортов по проекту addressbook
    IssueData[] issues = mc.mc_project_get_issues
            (app.getProperty("mantis.soap.adminLogin"), app.getProperty("mantis.soap.adminPassword"),
                    projectId, BigInteger.valueOf(1), BigInteger.valueOf(0));
    // Получение ИД произвольного баг-репорта
    return Arrays.asList(issues).iterator().next().getId();
  }

  public int issueStatus(BigInteger issueId) throws MalformedURLException, ServiceException, RemoteException {
    // Открытие соединения
    MantisConnectPortType mc = getMantisConnect();
    // Получение статуса баг-репорта
    return (mc.mc_issue_get(app.getProperty("mantis.soap.adminLogin"), app.getProperty("mantis.soap.adminPassword"),
            issueId).getStatus().getId().intValue());
  }

}
