package commands.user;

import commands.ActionDbCommand;
import model.entities.Role;
import model.entities.User;
import resource_managers.PageManager;
import services.RoleService;
import services.UserService;
import utils.CommandResult;
import utils.SessionRequestContent;

import java.util.List;

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
            List<Role> allRoles = RoleService.getRoles();
            sessionRequestContent.addRequestAttribute("user", user);
            sessionRequestContent.addRequestAttribute("all_roles", allRoles);
            return new CommandResult(PageManager.getProperty("page.edit_user"));
        } catch (RuntimeException e) {
            e.printStackTrace();
            return new CommandResult(PageManager.getProperty("page.error"));
        }
    }
}
