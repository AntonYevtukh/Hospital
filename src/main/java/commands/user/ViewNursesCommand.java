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

public class ViewNursesCommand implements ActionDbCommand {
    private static ViewNursesCommand instance;
    private static final Set<Long> PERMITTED_FULL_VIEW_ROLE_IDS = Set.of(2L, 3L, 4L);
    private static final Set<Long> PERMITTED_EDIT_ROLE_IDS = Set.of(4L);

    public static ViewNursesCommand getInstance() {
        if (instance == null) {
            synchronized (ViewNursesCommand.class) {
                if (instance == null)
                    instance = new ViewNursesCommand();
            }
        }
        return instance;
    }

    private ViewNursesCommand() {
    }

    @Override
    public CommandResult execute(SessionRequestContent sessionRequestContent) {
        int page = Integer.parseInt(sessionRequestContent.getSingleRequestParameter("page"));
        try {
            User currentUser = (User)sessionRequestContent.getSessionAttribute("current_user");
            PageContent<User> pageContent = UserService.getUsersForPageByRole(2L, page, currentUser.getItemsPerPage());
            sessionRequestContent.addRequestAttribute("page_content", pageContent);
            if (!SetIntersector.intersectSets(currentUser.getRoleMap().keySet(), PERMITTED_FULL_VIEW_ROLE_IDS).isEmpty())
                sessionRequestContent.addRequestAttribute("can_view_all", true);
            if (!SetIntersector.intersectSets(currentUser.getRoleMap().keySet(), PERMITTED_EDIT_ROLE_IDS).isEmpty())
                sessionRequestContent.addRequestAttribute("can_edit", true);
            sessionRequestContent.addRequestAttribute("url_pattern", "/serv?action=view_nurses&page=");
            sessionRequestContent.addRequestAttribute("title", "title.view_nurses");
            return new CommandResult(PageManager.getProperty("page.view_users"));
        } catch (RuntimeException e) {
            e.getMessage();
            e.printStackTrace();
            return new CommandResult(PageManager.getProperty("page.error"));
        }
    }
}
