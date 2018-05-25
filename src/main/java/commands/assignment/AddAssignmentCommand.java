package commands.assignment;

import commands.ActionDbCommand;
import exceptions.ErrorMessageKeysContainedException;
import model.entities.Assignment;
import model.entities.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import resource_managers.MessageManager;
import resource_managers.PageManager;
import services.AssignmentService;
import utils.CommandResult;
import utils.SessionRequestContent;
import utils.json.ErrorResponseCreator;
import utils.json.JsonSerializer;
import utils.parsers.AssignmentParser;
import validation.EntityValidatorFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class AddAssignmentCommand implements ActionDbCommand {

    private static final Logger LOGGER = LoggerFactory.getLogger(AddAssignmentCommand.class);
    private static AddAssignmentCommand instance;

    public static AddAssignmentCommand getInstance() {
        if (instance == null) {
            synchronized (AddAssignmentCommand.class) {
                if (instance == null)
                    instance = new AddAssignmentCommand();
            }
        }
        return instance;
    }

    private AddAssignmentCommand() {
        LOGGER.info(getClass().getSimpleName() + " instance created!");
    }

    @Override
    public CommandResult execute(SessionRequestContent sessionRequestContent) {
        LOGGER.trace("Entering the method");
        User currentUser = (User)sessionRequestContent.getSessionAttribute("current_user");
        Assignment assignment = AssignmentParser.parseAssignment(sessionRequestContent);
        List<String> validationFails = EntityValidatorFactory.getValidatorFor(Assignment.class).validate(assignment);
        String jsonString = null;

        if (!validationFails.isEmpty()) {
            jsonString = ErrorResponseCreator.createResponseWithErrors(validationFails, null, currentUser.getLanguage());
            sessionRequestContent.addRequestAttribute("jsonString", jsonString);
            LOGGER.trace("Leaving the method");
            return new CommandResult(PageManager.getProperty("page.json"));
        }

        try {
            AssignmentService.addAssignment(assignment);
            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("success", MessageManager.getProperty("assignment.added", currentUser.getLanguage()));
            jsonString = JsonSerializer.serialize(responseMap);
            sessionRequestContent.addRequestAttribute("jsonString", jsonString);
            LOGGER.trace("Leaving the method");
            return new CommandResult(PageManager.getProperty("page.json"));
        } catch (ErrorMessageKeysContainedException e) {
            LOGGER.error("Error caught while executing the method:", e);
            validationFails.addAll(e.getErrorMesageKeys());
            jsonString = ErrorResponseCreator.createResponseWithErrors(validationFails, null, currentUser.getLanguage());
            sessionRequestContent.addRequestAttribute("jsonString", jsonString);
            LOGGER.trace("Leaving the method");
            return new CommandResult(PageManager.getProperty("page.json"));
        }
    }
}
