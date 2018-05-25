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


public class UpdateAssignmentCommand implements ActionDbCommand {

    private static final Logger LOGGER = LoggerFactory.getLogger(UpdateAssignmentCommand.class);
    private static UpdateAssignmentCommand instance;

    public static UpdateAssignmentCommand getInstance() {
        if (instance == null) {
            synchronized (UpdateAssignmentCommand.class) {
                if (instance == null)
                    instance = new UpdateAssignmentCommand();
            }
        }
        return instance;
    }

    private UpdateAssignmentCommand() {
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
            AssignmentService.updateAssignment(assignment);
            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("success", MessageManager.getProperty("assignment.updated", currentUser.getLanguage()));
            jsonString = JsonSerializer.serialize(responseMap);
            sessionRequestContent.addRequestAttribute("jsonString", jsonString);
            LOGGER.trace("Leaving the method");
            return new CommandResult(PageManager.getProperty("page.json"));
        } catch (ErrorMessageKeysContainedException e) {
            LOGGER.error("Error caught while executing the method:", e);
            validationFails.addAll(e.getErrorMesageKeys());
            jsonString = ErrorResponseCreator.createResponseWithErrors(validationFails, null, currentUser.getLanguage());
            sessionRequestContent.addRequestAttribute("jsonString", jsonString);
            return new CommandResult(PageManager.getProperty("page.json"));
        }
    }
}
