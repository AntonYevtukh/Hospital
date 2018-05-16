package commands.user;

import commands.ActionDbCommand;
import model.entities.User;
import resource_managers.PageManager;
import services.UserService;
import utils.SessionRequestContent;
import utils.CommandResult;

public class ViewUserCommand implements ActionDbCommand {
    private static ViewUserCommand instance;

    public static ViewUserCommand getInstance() {
        if (instance == null) {
            synchronized (ViewUserCommand.class) {
                if (instance == null)
                    instance = new ViewUserCommand();
            }
        }
        return instance;
    }

    private ViewUserCommand() {
    }

    @Override
    public CommandResult execute(SessionRequestContent sessionRequestContent) {
        long userId = Integer.parseInt(sessionRequestContent.getSingleRequestParameter("id"));
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
