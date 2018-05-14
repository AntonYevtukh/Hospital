package commands;

import model.entities.User;
import resource_managers.PageManager;
import services.UserService;
import utils.CommandResult;
import utils.PageContent;
import utils.SessionRequestContent;

public class ViewDoctorsCommand implements ActionDbCommand {
    private static ViewDoctorsCommand instance;

    public static ViewDoctorsCommand getInstance() {
        if (instance == null) {
            synchronized (ViewDoctorsCommand.class) {
                if (instance == null)
                    instance = new ViewDoctorsCommand();
            }
        }
        return instance;
    }

    private ViewDoctorsCommand() {
    }

    @Override
    public CommandResult execute(SessionRequestContent sessionRequestContent) {
        int page = Integer.parseInt(sessionRequestContent.getSingleRequestParameter("page"));
        try {
            User currentUser = (User)sessionRequestContent.getSessionAttribute("current_user");
            PageContent<User> pageContent = UserService.getUsersForPageByRole(4, page, currentUser.getItemsPerPage());
            sessionRequestContent.addRequestAttribute("page_content", pageContent);
            return new CommandResult(PageManager.getProperty("page.view_users"));
        } catch (RuntimeException e) {
            e.printStackTrace();
            return new CommandResult(PageManager.getProperty("page.error"));
        }
    }
}
