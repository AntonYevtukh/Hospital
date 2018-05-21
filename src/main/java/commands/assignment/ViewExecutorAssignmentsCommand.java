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

public class ViewExecutorAssignmentsCommand implements ActionDbCommand {
    private static ViewExecutorAssignmentsCommand instance;

    public static ViewExecutorAssignmentsCommand getInstance() {
        if (instance == null) {
            synchronized (ViewExecutorAssignmentsCommand.class) {
                if (instance == null)
                    instance = new ViewExecutorAssignmentsCommand();
            }
        }
        return instance;
    }

    private ViewExecutorAssignmentsCommand() {
    }

    @Override
    public CommandResult execute(SessionRequestContent sessionRequestContent) {
        int page = Integer.parseInt(sessionRequestContent.getSingleRequestParameter("page"));
        int executorId = Integer.parseInt(sessionRequestContent.getSingleRequestParameter("executor_id"));
        User executor = UserService.getUserById(executorId);
        try {
            User currentUser = (User)sessionRequestContent.getSessionAttribute("current_user");
            if (!currentUser.getRoleMap().containsKey(3L)) {
                sessionRequestContent.addRequestAttribute("error_message", "error.access");
                return new CommandResult(PageManager.getProperty("page.error"));
            }
            PageContent<Assignment> pageContent =
                    AssignmentService.getAssignmentsForPageByExecutorId(executorId, page, currentUser.getItemsPerPage());
            sessionRequestContent.addRequestAttribute("title", "assignments.assignments_executor");
            sessionRequestContent.addRequestAttribute("page_content", pageContent);
            sessionRequestContent.addRequestAttribute("url_pattern",
                    "/serv?action=view_doctor_assignments&executor_id=" + executorId + "&page=");
            sessionRequestContent.addRequestAttribute("user", executor);
            return new CommandResult(PageManager.getProperty("page.view_assignments"));
        } catch (RuntimeException e) {
            e.getMessage();
            e.printStackTrace();
            return new CommandResult(PageManager.getProperty("page.error"));
        }
    }
}
