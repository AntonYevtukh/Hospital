package controllers;

import enums.Gender;
import model.database.ConnectionProvider;
import services.RoleService;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class ContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent event) {
        ConnectionProvider.bindConnection();
        event.getServletContext().setAttribute("genders_global", Gender.values());
        event.getServletContext().setAttribute("roles_global", RoleService.getRoles());
        ConnectionProvider.unbindConnection();
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        // NOOP
    }

}
