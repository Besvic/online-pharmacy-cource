package com.pharmacy.traning.model.pool;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class HibernateConfig {

    private static HibernateConfig instance;
    private Session session;
    private SessionFactory sessionFactory;

    public void openSession(){
        Configuration configuration = new Configuration();
        configuration.configure();
        sessionFactory = configuration.buildSessionFactory();
        session = sessionFactory.openSession();
    }

    public void beginTransaction(){
        session.getTransaction().begin();
    }

    public void commitTransaction(){
        session.getTransaction().commit();
    }

    public void closeSession(){
        sessionFactory.close();
        session.close();
    }
    public static HibernateConfig getInstance(){
        if (instance == null){
            instance = new HibernateConfig();
        }
        return instance;
    }
    public Session getSession(){
       return session;
    }
}
