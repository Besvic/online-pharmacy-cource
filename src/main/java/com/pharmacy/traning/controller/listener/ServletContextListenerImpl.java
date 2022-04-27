package com.pharmacy.traning.controller.listener;

//import com.pharmacy.traning.model.pool.ConnectionPool;
import com.pharmacy.traning.model.pool.HibernateConfig;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

/**
 * @author Besarab Victor
 * The type Servlet context listener.
 */
@WebListener
public class ServletContextListenerImpl implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        HibernateConfig.getInstance().openSession();
//        ConnectionPool.getInstance();
    }

    // TODO: 19.04.2022 comment
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        HibernateConfig.getInstance().closeSession();
//        ConnectionPool.getInstance().destroyPool();
    }
}
