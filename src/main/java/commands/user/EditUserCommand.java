package commands.user;

import commands.ActionDbCommand;
import model.entities.User;
import resource_managers.PageManager;
import services.UserService;
import utils.CommandResult;
import utils.SessionRequestContent;

public class EditUserCommand implements ActionDbCommand {
    private static EditUserCommand instance;

    public static EditUserCommand getInstance() {
        if (instance == null) {
            synchronized (EditUserCommand.class) {
                if (instance == null)
                    instance = new EditUserCommand();
            }
        }
        return instance;
    }

    private EditUserCommand() {
    }

    @Override
    public CommandResult execute(SessionRequestContent sessionRequestContent) {
        long userId = Integer.parseInt(sessionRequestContent.getSingleRequestParameter("id"));
        try {
            User user = UserService.getUserById(userId);
            sessionRequestContent.addRequestAttribute("user", user);
            return new CommandResult(PageManager.getProperty("page.edit_user"));
        } catch (RuntimeException e) {
            e.printStackTrace();
            return new CommandResult(PageManager.getProperty("page.error"));
        }
    }
}
