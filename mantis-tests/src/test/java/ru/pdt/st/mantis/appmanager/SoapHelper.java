package ru.pdt.st.mantis.appmanager;

import biz.futureware.mantis.rpc.soap.client.*;
import ru.pdt.st.mantis.model.Issue;
import ru.pdt.st.mantis.model.Project;

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

  private MantisConnectPortType getMantisConnect() throws ServiceException, MalformedURLException {
    // Установка соединения с Mantis SOAP
    MantisConnectPortType mc = new MantisConnectLocator()
            .getMantisConnectPort(new URL("http://localhost/mantisbt-2.24.3/api/soap/mantisconnect.php"));
    return mc;
  }

  public Set<Project> getProjects() throws MalformedURLException, ServiceException, RemoteException {
    // Открытие соединения
    MantisConnectPortType mc = getMantisConnect();
    // Получить список проектов, к которым пользователь имеет доступ
    ProjectData[] projects = mc.mc_projects_get_user_accessible("administrator", "root");
    // Преобразование полученных данных в модельные объекты
    return Arrays.asList(projects).stream().map((p) -> new Project()
            .withId(p.getId().intValue()).withName(p.getName()))
            .collect(Collectors.toSet());
  }

  public Issue addIssue(Issue issue) throws MalformedURLException, ServiceException, RemoteException {
    // Открытие соединения
    MantisConnectPortType mc = getMantisConnect();
    // Получение списка категорий для баг-репортов
    String[] categories = mc.mc_project_get_categories("administrator", "root",
            BigInteger.valueOf(issue.getProject().getId()));
    // Создание из нашего модельного объекта структуры объекта Mantis SOAP
    IssueData issueData = new IssueData();
    issueData.setSummary(issue.getSummary());
    issueData.setDescription(issue.getDescription());
    // setProject ожидает в параметре ObjectRef project, т.е. ссылку на проект
    // ObjectRef, в свою очередь, ожидает два параметра, причем один из них с типом BigInteger
    issueData.setProject(new ObjectRef(BigInteger.valueOf(issue.getProject().getId()), issue.getProject().getName()));
    // Категория - обязательное поле, список выбора. Выбираем первую попавшуюся из списка categories
    issueData.setCategory(categories[0]);
    // Создание баг-репорта
    BigInteger issueId = mc.mc_issue_add("administrator", "root", issueData);
    // Обратное преобразование объекта Mantis SOAP в новый модельный объект. Получение объекта
    IssueData createdIssueData = mc.mc_issue_get("administrator", "root", issueId);
    // Обратное преобразование объекта Mantis SOAP в новый модельный объект. Заполнение модели
    return new Issue().withId(createdIssueData.getId().intValue())
                      .withSummary(createdIssueData.getSummary())
                      .withDescription(createdIssueData.getDescription())
                      .withProject(new Project().withId(createdIssueData.getId().intValue())
                                                .withName(createdIssueData.getProject().getName()));
  }
}
