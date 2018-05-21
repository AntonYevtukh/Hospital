package commands.user;

import commands.ActionDbCommand;
import model.entities.User;
import resource_managers.PageManager;
import services.UserService;
import utils.CommandResult;
import utils.PageContent;
import utils.SessionRequestContent;
import utils.SetIntersector;

import java.util.Set;

public class ViewPatientsCommand implements ActionDbCommand {
    private static ViewPatientsCommand instance;
    private static final Set<Long> PERMITTED_VIEW_ROLE_IDS = Set.of(2L, 3L, 4L);
    private static final Set<Long> PERMITTED_EDIT_ROLE_IDS = Set.of(4L);

    public static ViewPatientsCommand getInstance() {
        if (instance == null) {
            synchronized (ViewPatientsCommand.class) {
                if (instance == null)
                    instance = new ViewPatientsCommand();
            }
        }
        return instance;
    }

    private ViewPatientsCommand() {
    }

    @Override
    public CommandResult execute(SessionRequestContent sessionRequestContent) {
        int page = Integer.parseInt(sessionRequestContent.getSingleRequestParameter("page"));
        try {
            User currentUser = (User)sessionRequestContent.getSessionAttribute("current_user");
            if (SetIntersector.intersectSets(currentUser.getRoleMap().keySet(), PERMITTED_VIEW_ROLE_IDS).isEmpty()) {
                sessionRequestContent.addRequestAttribute("error_message", "error.access");
                return new CommandResult(PageManager.getProperty("page.error"));
            }
            PageContent<User> pageContent = UserService.getUsersForPageByRole(1L, page, currentUser.getItemsPerPage());
            sessionRequestContent.addRequestAttribute("page_content", pageContent);
            if (!SetIntersector.intersectSets(currentUser.getRoleMap().keySet(), PERMITTED_EDIT_ROLE_IDS).isEmpty()) {
                sessionRequestContent.addRequestAttribute("can_edit", true);
            }
            sessionRequestContent.addRequestAttribute("can_view_all", true);
            sessionRequestContent.addRequestAttribute("url_pattern", "/serv?action=view_patients&page=");
            sessionRequestContent.addRequestAttribute("title", "title.view_patients");
            return new CommandResult(PageManager.getProperty("page.view_users"));
        } catch (RuntimeException e) {
            e.getMessage();
            e.printStackTrace();
            return new CommandResult(PageManager.getProperty("page.error"));
        }
    }
}
