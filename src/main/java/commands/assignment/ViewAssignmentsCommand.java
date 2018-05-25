package commands.assignment;

import commands.ActionDbCommand;
import model.entities.Assignment;
import model.entities.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import resource_managers.PageManager;
import services.AssignmentService;
import utils.CommandResult;
import utils.PageContent;
import utils.SessionRequestContent;

public class ViewAssignmentsCommand implements ActionDbCommand {

    private static final Logger LOGGER = LoggerFactory.getLogger(ViewAssignmentsCommand.class);
    private static ViewAssignmentsCommand instance;

    public static ViewAssignmentsCommand getInstance() {
        if (instance == null) {
            synchronized (ViewAssignmentsCommand.class) {
                if (instance == null)
                    instance = new ViewAssignmentsCommand();
            }
        }
        return instance;
    }

    private ViewAssignmentsCommand() {
        LOGGER.info(getClass().getSimpleName() + " instance created!");
    }

    @Override
    public CommandResult execute(SessionRequestContent sessionRequestContent) {
        LOGGER.trace("Entering the method");
        int page = Integer.parseInt(sessionRequestContent.getSingleRequestParameter("page"));
        try {
            User currentUser = (User)sessionRequestContent.getSessionAttribute("current_user");
            if (!currentUser.getRoleMap().containsKey(3L)) {
                sessionRequestContent.addRequestAttribute("error_message", "error.access");
                LOGGER.trace("Leaving the method");
                return new CommandResult(PageManager.getProperty("page.error"));
            }
            PageContent<Assignment> pageContent = AssignmentService.getAssignmentsForPage(page, currentUser.getItemsPerPage());
            sessionRequestContent.addRequestAttribute("title", "assignments.assignments");
            sessionRequestContent.addRequestAttribute("page_content", pageContent);
            sessionRequestContent.addRequestAttribute("url_pattern", "/serv?action=view_assignments&page=");
            LOGGER.trace("Leaving the method");
            return new CommandResult(PageManager.getProperty("page.view_assignments"));
        } catch (RuntimeException e) {
            LOGGER.error("Error caught while executing the method:", e);
            LOGGER.trace("Leaving the method");
            return new CommandResult(PageManager.getProperty("page.error"));
        }
    }
}
