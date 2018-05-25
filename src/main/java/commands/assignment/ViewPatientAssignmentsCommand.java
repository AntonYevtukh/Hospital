package commands.assignment;

import commands.ActionDbCommand;
import model.entities.Assignment;
import model.entities.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import resource_managers.PageManager;
import services.AssignmentService;
import services.UserService;
import utils.CommandResult;
import utils.PageContent;
import utils.SessionRequestContent;

public class ViewPatientAssignmentsCommand implements ActionDbCommand {

    private static final Logger LOGGER = LoggerFactory.getLogger(ViewPatientAssignmentsCommand.class);
    private static ViewPatientAssignmentsCommand instance;

    public static ViewPatientAssignmentsCommand getInstance() {
        if (instance == null) {
            synchronized (ViewPatientAssignmentsCommand.class) {
                if (instance == null)
                    instance = new ViewPatientAssignmentsCommand();
            }
        }
        return instance;
    }

    private ViewPatientAssignmentsCommand() {
        LOGGER.info(getClass().getSimpleName() + " instance created!");
    }

    @Override
    public CommandResult execute(SessionRequestContent sessionRequestContent) {
        LOGGER.trace("Entering the method");
        int page = Integer.parseInt(sessionRequestContent.getSingleRequestParameter("page"));
        int patientId = Integer.parseInt(sessionRequestContent.getSingleRequestParameter("patient_id"));
        User patient = UserService.getUserById(patientId);
        try {
            User currentUser = (User)sessionRequestContent.getSessionAttribute("current_user");
            if (!currentUser.getRoleMap().containsKey(3L)) {
                sessionRequestContent.addRequestAttribute("error_message", "error.access");
                LOGGER.trace("Leaving the method");
                return new CommandResult(PageManager.getProperty("page.error"));
            }
            PageContent<Assignment> pageContent =
                    AssignmentService.getAssignmentsForPageByPatientId(patientId, page, currentUser.getItemsPerPage());
            sessionRequestContent.addRequestAttribute("page_content", pageContent);
            sessionRequestContent.addRequestAttribute("title", "assignments.assignments_patient");
            sessionRequestContent.addRequestAttribute("url_pattern",
                    "/serv?action=view_patient_assignments&doctor_id=" + patientId + "&page=");
            sessionRequestContent.addRequestAttribute("user", patient);
            LOGGER.trace("Leaving the method");
            return new CommandResult(PageManager.getProperty("page.view_assignments"));
        } catch (RuntimeException e) {
            LOGGER.error("Error caught while executing the method:", e);
            LOGGER.trace("Leaving the method");
            return new CommandResult(PageManager.getProperty("page.error"));
        }
    }
}
