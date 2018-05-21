package commands.assignment;

import commands.ActionDbCommand;
import model.entities.Assignment;
import model.entities.User;
import resource_managers.PageManager;
import services.AssignmentService;
import utils.CommandResult;
import utils.PageContent;
import utils.SessionRequestContent;

public class ViewAssignmentsCommand implements ActionDbCommand {
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
    }

    @Override
    public CommandResult execute(SessionRequestContent sessionRequestContent) {
        int page = Integer.parseInt(sessionRequestContent.getSingleRequestParameter("page"));
        try {
            User currentUser = (User)sessionRequestContent.getSessionAttribute("current_user");
            if (!currentUser.getRoleMap().containsKey(3L)) {
                sessionRequestContent.addRequestAttribute("error_message", "error.access");
                return new CommandResult(PageManager.getProperty("page.error"));
            }
            PageContent<Assignment> pageContent = AssignmentService.getAssignmentsForPage(page, currentUser.getItemsPerPage());
            sessionRequestContent.addRequestAttribute("title", "assignments.assignments");
            sessionRequestContent.addRequestAttribute("page_content", pageContent);
            sessionRequestContent.addRequestAttribute("url_pattern", "/serv?action=view_assignments&page=");
            return new CommandResult(PageManager.getProperty("page.view_assignments"));
        } catch (RuntimeException e) {
            e.getMessage();
            e.printStackTrace();
            return new CommandResult(PageManager.getProperty("page.error"));
        }
    }
}
