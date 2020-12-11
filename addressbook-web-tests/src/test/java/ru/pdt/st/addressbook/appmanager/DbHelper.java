package ru.pdt.st.addressbook.appmanager;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import ru.pdt.st.addressbook.model.ContactData;
import ru.pdt.st.addressbook.model.Contacts;
import ru.pdt.st.addressbook.model.GroupData;
import ru.pdt.st.addressbook.model.Groups;

import java.util.List;
import java.util.stream.Collectors;

public class DbHelper {

  private final SessionFactory sessionFactory;

  public DbHelper() {
    // A SessionFactory is set up once for an application!
    final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
            .configure() // configures settings from hibernate.cfg.xml
            .build();
    sessionFactory = new MetadataSources( registry ).buildMetadata().buildSessionFactory();
  }

  public Groups groups() {
    Session session = sessionFactory.openSession();
    session.beginTransaction();
    List<GroupData> result = session.createQuery("from GroupData").list();
    List<GroupData> replaceResult = result.stream().map((g) -> g
            .withFooter(g.getFooter().replaceAll("\r", ""))
            .withHeader(g.getHeader().replaceAll("\r", "")))
            .collect(Collectors.toList());
    session.getTransaction().commit();
    session.close();
    return new Groups(replaceResult);
  }

  public GroupData group(int id) {
    Session session = sessionFactory.openSession();
    session.beginTransaction();
    List<GroupData> result = session.createQuery(String.format("from GroupData where group_id=%s", id) ).list();
    session.getTransaction().commit();
    session.close();
    return new Groups(result).iterator().next();
  }

  public Contacts contacts() {
    Session session = sessionFactory.openSession();
    session.beginTransaction();
    List<ContactData> result = session.createQuery("from ContactData").list();
    session.getTransaction().commit();
    session.close();
    return new Contacts(result);
  }

  public ContactData contact(int id) {
    Session session = sessionFactory.openSession();
    session.beginTransaction();
    List<ContactData> result = session.createQuery(String.format("from ContactData where id=%s", id) ).list();
    session.getTransaction().commit();
    session.close();
    return new Contacts(result).iterator().next();
  }

}
