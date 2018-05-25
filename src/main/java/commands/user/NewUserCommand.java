package commands.user;

import commands.ActionDbCommand;
import model.entities.Role;
import resource_managers.PageManager;
import services.RoleService;
import utils.CommandResult;
import utils.SessionRequestContent;

import java.util.List;

public class NewUserCommand implements ActionDbCommand {
    private static NewUserCommand instance;

    public static NewUserCommand getInstance() {
        if (instance == null) {
            synchronized (NewUserCommand.class) {
                if (instance == null)
                    instance = new NewUserCommand();
            }
        }
        return instance;
    }

    private NewUserCommand() {
    }

    @Override
    public CommandResult execute(SessionRequestContent sessionRequestContent) {
        try {
            List<Role> allRoles = RoleService.getRoles();
            sessionRequestContent.addRequestAttribute("all_roles", allRoles);
            return new CommandResult(PageManager.getProperty("page.sign_up"));
        } catch (RuntimeException e) {
            e.printStackTrace();
            return new CommandResult(PageManager.getProperty("page.error"));
        }
    }
}
