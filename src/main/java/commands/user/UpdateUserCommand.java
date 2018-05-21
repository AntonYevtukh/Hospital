package commands.user;

import commands.ActionDbCommand;
import exceptions.ErrorMessageKeysContainedException;
import model.entities.User;
import resource_managers.PageManager;
import services.UserService;
import utils.CommandResult;
import utils.SessionRequestContent;
import utils.parsers.UserParser;
import validation.EntityValidatorFactory;

import java.util.List;


public class UpdateUserCommand implements ActionDbCommand {

    private static UpdateUserCommand instance;

    public static UpdateUserCommand getInstance() {
        if (instance == null) {
            synchronized (UpdateUserCommand.class) {
                if (instance == null)
                    instance = new UpdateUserCommand();
            }
        }
        return instance;
    }


    @Override
    public CommandResult execute(SessionRequestContent sessionRequestContent) {
        User currentUser = (User)sessionRequestContent.getSessionAttribute("current_user");
        User user = UserParser.parseUser(sessionRequestContent);
        List<String> validationFails = EntityValidatorFactory.getValidatorFor(User.class).validate(user);
        String retype = sessionRequestContent.getSingleRequestParameter("password_retype");
        if (!user.getPassword().isEmpty() && !retype.equals(user.getPassword()))
            validationFails.add("validation.user.password_retype");
        if (user.getPassword().isEmpty())
            validationFails.remove("validation.user.password");
        if (user.getDateOfBirth() == null)
            validationFails.add("validation.user.date_of_birth");
        if (user.getItemsPerPage() == 0)
            validationFails.add("validation.user.items");
        if (user.getRoleMap().isEmpty())
            validationFails.add("validation.user.roles");

        if (!validationFails.isEmpty()) {
            sessionRequestContent.addRequestAttribute("user", user);
            sessionRequestContent.addRequestAttribute("validationFails", validationFails);
            return new CommandResult(PageManager.getProperty("page.edit_user"));
        }

        try {
            sessionRequestContent.addRequestAttribute("user", user);
            UserService.updateUser(user);
            if (currentUser.getId() == user.getId())
                sessionRequestContent.addSessionAttribute("current_user", user);
            return new CommandResult("/serv?action=view_user&id=" + user.getId(), true);
        } catch (ErrorMessageKeysContainedException e) {
            validationFails.addAll(e.getErrorMesageKeys());
            sessionRequestContent.addRequestAttribute("validationFails", validationFails);
            return new CommandResult(PageManager.getProperty("page.edit_user"));
        }
    }
}
