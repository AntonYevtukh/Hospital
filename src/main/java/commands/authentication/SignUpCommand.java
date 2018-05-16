package commands.authentication;

import commands.ActionDbCommand;
import exceptions.ErrorMessageKeysContainedException;
import services.UserService;
import utils.SessionRequestContent;
import model.entities.User;
import resource_managers.PageManager;
import utils.CommandResult;
import utils.parsers.UserParser;
import validation.EntityValidatorFactory;

import java.util.*;


public class SignUpCommand implements ActionDbCommand {

    private static SignUpCommand instance;

    public static SignUpCommand getInstance() {
        if (instance == null) {
            synchronized (SignUpCommand.class) {
                if (instance == null)
                    instance = new SignUpCommand();
            }
        }
        return instance;
    }


    @Override
    public CommandResult execute(SessionRequestContent sessionRequestContent) {
        User user = UserParser.parseUser(sessionRequestContent);
        List<String> validationFails = EntityValidatorFactory.getValidatorFor(User.class).validate(user);
        String retype = sessionRequestContent.getSingleRequestParameter("password_retype");
        if (!retype.equals(user.getPassword()))
            validationFails.add("validation.user.password_retype");
        if (user.getDateOfBirth() == null)
            validationFails.add("validation.user.date_of_birth");
        if (user.getItemsPerPage() == 0)
            validationFails.add("validation.user.items");
        if (user.getRoleMap().isEmpty())
            validationFails.add("validation.user.roles");

        if (!validationFails.isEmpty()) {
            sessionRequestContent.addRequestAttribute("user", user);
            sessionRequestContent.addRequestAttribute("validationFails", validationFails);
            return new CommandResult(PageManager.getProperty("page.sign_up"));
        }

        try {
            sessionRequestContent.addRequestAttribute("user", user);
            UserService.registerUser(user);
            sessionRequestContent.addSessionAttribute("current_user", user);
            return new CommandResult("/serv?action=view_current_user", true);
        } catch (ErrorMessageKeysContainedException e) {
            validationFails.addAll(e.getErrorMesageKeys());
            sessionRequestContent.addRequestAttribute("validationFails", validationFails);
            return new CommandResult(PageManager.getProperty("page.sign_up"));
        }
    }
}
