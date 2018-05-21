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

public class ViewDoctorAssignmentsCommand implements ActionDbCommand {
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
    }

    @Override
    public CommandResult execute(SessionRequestContent sessionRequestContent) {
        int page = Integer.parseInt(sessionRequestContent.getSingleRequestParameter("page"));
        int doctorId = Integer.parseInt(sessionRequestContent.getSingleRequestParameter("doctor_id"));
        User doctor = UserService.getUserById(doctorId);
        try {
            User currentUser = (User)sessionRequestContent.getSessionAttribute("current_user");
            if (!currentUser.getRoleMap().containsKey(3L)) {
                sessionRequestContent.addRequestAttribute("error_message", "error.access");
                return new CommandResult(PageManager.getProperty("page.error"));
            }
            PageContent<Assignment> pageContent =
                    AssignmentService.getAssignmentsForPageByDoctorId(doctorId, page, currentUser.getItemsPerPage());
            sessionRequestContent.addRequestAttribute("title", "assignments.assignments_doctor");
            sessionRequestContent.addRequestAttribute("page_content", pageContent);
            sessionRequestContent.addRequestAttribute("url_pattern",
                    "/serv?action=view_doctor_assignments&doctor_id=" + doctorId + "&page=");
            sessionRequestContent.addRequestAttribute("user", doctor);
            return new CommandResult(PageManager.getProperty("page.view_assignments"));
        } catch (RuntimeException e) {
            e.getMessage();
            e.printStackTrace();
            return new CommandResult(PageManager.getProperty("page.error"));
        }
    }
}
