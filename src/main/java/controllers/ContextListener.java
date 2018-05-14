package controllers;

import enums.Gender;
import services.RoleService;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class ContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent event) {
        event.getServletContext().setAttribute("genders_global", Gender.values());
        event.getServletContext().setAttribute("roles_global", RoleService.getRolesFromDatabase());
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        // NOOP
    }

}
