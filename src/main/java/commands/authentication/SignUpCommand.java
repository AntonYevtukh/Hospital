package commands.authentication;

import commands.ActionDbCommand;
import exceptions.ErrorMessageKeysContainedException;
import model.entities.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import resource_managers.PageManager;
import services.UserService;
import utils.CommandResult;
import utils.SessionRequestContent;
import utils.parsers.UserParser;
import validation.EntityValidatorFactory;

import java.util.List;


public class SignUpCommand implements ActionDbCommand {

    private static final Logger LOGGER = LoggerFactory.getLogger(SignUpCommand.class);
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

    private SignUpCommand() {
        LOGGER.info(getClass().getSimpleName() + " instance created!");
    }

    @Override
    public CommandResult execute(SessionRequestContent sessionRequestContent) {
        LOGGER.trace("Entering the method");
        User user = UserParser.parseUser(sessionRequestContent);
        List<String> validationFails = EntityValidatorFactory.getValidatorFor(User.class).validate(user);
        String retype = sessionRequestContent.getSingleRequestParameter("password_retype");
        if (!retype.equals(user.getPassword())) {
            validationFails.add("validation.user.password_retype");
        }
        if (user.getDateOfBirth() == null) {
            validationFails.add("validation.user.date_of_birth");
        }
        if (user.getItemsPerPage() == 0) {
            validationFails.add("validation.user.items");
        }
        if (user.getRoleMap().isEmpty()) {
            validationFails.add("validation.user.roles");
        }

        if (!validationFails.isEmpty()) {
            sessionRequestContent.addRequestAttribute("user", user);
            sessionRequestContent.addRequestAttribute("validationFails", validationFails);
            LOGGER.trace("Leaving the method");
            return new CommandResult(PageManager.getProperty("page.sign_up"));
        }

        try {
            sessionRequestContent.addRequestAttribute("user", user);
            long userId = UserService.registerUser(user);
            user.setId(userId);
            sessionRequestContent.addSessionAttribute("current_user", user);
            LOGGER.trace("Leaving the method");
            return new CommandResult("/serv?action=view_user&id=" + userId, true);
        } catch (ErrorMessageKeysContainedException e) {
            LOGGER.error("Error caught while executing the method:", e);
            validationFails.addAll(e.getErrorMesageKeys());
            sessionRequestContent.addRequestAttribute("validationFails", validationFails);
            LOGGER.trace("Leaving the method");
            return new CommandResult(PageManager.getProperty("page.sign_up"));
        }
    }
}
