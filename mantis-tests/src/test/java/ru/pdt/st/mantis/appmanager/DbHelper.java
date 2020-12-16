package ru.pdt.st.mantis.appmanager;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import ru.pdt.st.mantis.model.UserData;


import java.util.HashSet;
import java.util.List;

public class DbHelper {

  private final SessionFactory sessionFactory;
  private HashSet<UserData> delegate ;

  public DbHelper(ApplicationManager app) {
    // A SessionFactory is set up once for an application!
    final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
            .configure() // configures settings from hibernate.cfg.xml
            .build();
    sessionFactory = new MetadataSources( registry ).buildMetadata().buildSessionFactory();
  }

  public UserData user() {
    Session session = sessionFactory.openSession();
    session.beginTransaction();
    List<UserData> resultList = session.createQuery("from UserData where access_level <> 90").list();
    UserData resultUser = resultList.iterator().next();
    session.getTransaction().commit();
    session.close();
    return new UserData().withId(resultUser.getId())
            .withUsername(resultUser.getUsername())
            .withEmail(resultUser.getEmail());
  }

}
