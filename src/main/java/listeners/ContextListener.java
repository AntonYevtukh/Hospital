package listeners;

import enums.Gender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class ContextListener implements ServletContextListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(ContextListener.class);


    @Override
    public void contextInitialized(ServletContextEvent event) {
        LOGGER.info("Start initializing the Servlet Context");
        event.getServletContext().setAttribute("genders_global", Gender.values());
        LOGGER.info("Finishing initializing the Servlet Context");
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        LOGGER.info("Servlet Context destroyed");
    }

}
