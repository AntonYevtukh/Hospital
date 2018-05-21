package commands.user;

import commands.ActionDbCommand;
import model.entities.User;
import resource_managers.PageManager;
import services.UserService;
import utils.CommandResult;
import utils.PageContent;
import utils.SessionRequestContent;

public class ViewUsersCommand implements ActionDbCommand {
    private static ViewUsersCommand instance;

    public static ViewUsersCommand getInstance() {
        if (instance == null) {
            synchronized (ViewUsersCommand.class) {
                if (instance == null)
                    instance = new ViewUsersCommand();
            }
        }
        return instance;
    }

    private ViewUsersCommand() {
    }

    @Override
    public CommandResult execute(SessionRequestContent sessionRequestContent) {
        int page = Integer.parseInt(sessionRequestContent.getSingleRequestParameter("page"));
        try {
            User currentUser = (User)sessionRequestContent.getSessionAttribute("current_user");
            if (!currentUser.getRoleMap().containsKey(4L)) {
                sessionRequestContent.addRequestAttribute("error_message", "error.access");
                return new CommandResult(PageManager.getProperty("page.error"));
            }
            PageContent<User> pageContent = UserService.getUsersForPage(page, currentUser.getItemsPerPage());
            sessionRequestContent.addRequestAttribute("page_content", pageContent);
            sessionRequestContent.addRequestAttribute("can_view_all", true);
            sessionRequestContent.addRequestAttribute("can_edit", true);
            sessionRequestContent.addRequestAttribute("title", "title.view_users");
            sessionRequestContent.addRequestAttribute("url_pattern", "/serv?action=view_users&page=");
            return new CommandResult(PageManager.getProperty("page.view_users"));
        } catch (RuntimeException e) {
            e.getMessage();
            e.printStackTrace();
            return new CommandResult(PageManager.getProperty("page.error"));
        }
    }
}
