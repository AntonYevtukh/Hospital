package commands.assignment;

import commands.ActionDbCommand;
import model.entities.Assignment;
import model.entities.User;
import resource_managers.PageManager;
import services.AssignmentService;
import services.UserService;
import utils.CommandResult;
import utils.PageContent;
import utils.SessionRequestContent;

public class ViewPatientAssignmentsCommand implements ActionDbCommand {
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
    }

    @Override
    public CommandResult execute(SessionRequestContent sessionRequestContent) {
        int page = Integer.parseInt(sessionRequestContent.getSingleRequestParameter("page"));
        int patientId = Integer.parseInt(sessionRequestContent.getSingleRequestParameter("patient_id"));
        User patient = UserService.getUserById(patientId);
        try {
            User currentUser = (User)sessionRequestContent.getSessionAttribute("current_user");
            if (!currentUser.getRoleMap().containsKey(3L)) {
                sessionRequestContent.addRequestAttribute("error_message", "error.access");
                return new CommandResult(PageManager.getProperty("page.error"));
            }
            PageContent<Assignment> pageContent =
                    AssignmentService.getAssignmentsForPageByPatientId(patientId, page, currentUser.getItemsPerPage());
            sessionRequestContent.addRequestAttribute("page_content", pageContent);
            sessionRequestContent.addRequestAttribute("title", "assignments.assignments_patient");
            sessionRequestContent.addRequestAttribute("url_pattern",
                    "/serv?action=view_patient_assignments&doctor_id=" + patientId + "&page=");
            sessionRequestContent.addRequestAttribute("user", patient);
            return new CommandResult(PageManager.getProperty("page.view_assignments"));
        } catch (RuntimeException e) {
            e.getMessage();
            e.printStackTrace();
            return new CommandResult(PageManager.getProperty("page.error"));
        }
    }
}
