package commands.user;

import commands.ActionDbCommand;
import model.entities.User;
import resource_managers.PageManager;
import services.UserService;
import utils.SessionRequestContent;
import utils.CommandResult;

public class ViewCurrentUserCommand implements ActionDbCommand {
    private static ViewCurrentUserCommand instance;

    public static ViewCurrentUserCommand getInstance() {
        if (instance == null) {
            synchronized (ViewCurrentUserCommand.class) {
                if (instance == null)
                    instance = new ViewCurrentUserCommand();
            }
        }
        return instance;
    }

    private ViewCurrentUserCommand() {
    }

    @Override
    public CommandResult execute(SessionRequestContent sessionRequestContent) {
        User currentUser = (User)(sessionRequestContent.getSessionAttribute("current_user"));
        if (currentUser == null) {
            sessionRequestContent.addRequestAttribute("error_message", "error.not_signed_in");
            return new CommandResult(PageManager.getProperty("page.error"), true);
        }
        long userId = currentUser.getId();
        try {
            User user = UserService.getUserById(userId);
            sessionRequestContent.addRequestAttribute("user", user);
            return new CommandResult(PageManager.getProperty("page.view_user"));
        } catch (RuntimeException e) {
            e.printStackTrace();
            return new CommandResult(PageManager.getProperty("page.error"));
        }
    }
}
