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

public class ViewDoctorAssignmentsCommand implements ActionDbCommand {

    private static final Logger LOGGER = LoggerFactory.getLogger(ViewDoctorAssignmentsCommand.class);
    private static ViewDoctorAssignmentsCommand instance;

    public static ViewDoctorAssignmentsCommand getInstance() {
        if (instance == null) {
            synchronized (ViewDoctorAssignmentsCommand.class) {
                if (instance == null)
                    instance = new ViewDoctorAssignmentsCommand();
            }
        }
        return instance;
    }

    private ViewDoctorAssignmentsCommand() {
        LOGGER.info(getClass().getSimpleName() + " instance created!");
    }

    @Override
    public CommandResult execute(SessionRequestContent sessionRequestContent) {
        LOGGER.trace("Entering the method");
        int page = Integer.parseInt(sessionRequestContent.getSingleRequestParameter("page"));
        int doctorId = Integer.parseInt(sessionRequestContent.getSingleRequestParameter("doctor_id"));
        User doctor = UserService.getUserById(doctorId);
        try {
            User currentUser = (User)sessionRequestContent.getSessionAttribute("current_user");
            if (!currentUser.getRoleMap().containsKey(3L)) {
                sessionRequestContent.addRequestAttribute("error_message", "error.access");
                LOGGER.trace("Leaving the method");
                return new CommandResult(PageManager.getProperty("page.error"));
            }
            PageContent<Assignment> pageContent =
                    AssignmentService.getAssignmentsForPageByDoctorId(doctorId, page, currentUser.getItemsPerPage());
            sessionRequestContent.addRequestAttribute("title", "assignments.assignments_doctor");
            sessionRequestContent.addRequestAttribute("page_content", pageContent);
            sessionRequestContent.addRequestAttribute("url_pattern",
                    "/serv?action=view_doctor_assignments&doctor_id=" + doctorId + "&page=");
            sessionRequestContent.addRequestAttribute("user", doctor);
            LOGGER.trace("Leaving the method");
            return new CommandResult(PageManager.getProperty("page.view_assignments"));
        } catch (RuntimeException e) {
            LOGGER.error("Error caught while executing the method:", e);
            LOGGER.trace("Leaving the method");
            return new CommandResult(PageManager.getProperty("page.error"));
        }
    }
}
