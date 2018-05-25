package commands.assignment;

import commands.ActionDbCommand;
import exceptions.ErrorMessageKeysContainedException;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class DeleteAssignmentCommand implements ActionDbCommand {

    private static final Logger LOGGER = LoggerFactory.getLogger(DeleteAssignmentCommand.class);
    private static DeleteAssignmentCommand instance;

    public static DeleteAssignmentCommand getInstance() {
        if (instance == null) {
            synchronized (DeleteAssignmentCommand.class) {
                if (instance == null)
                    instance = new DeleteAssignmentCommand();
            }
        }
        return instance;
    }

    private DeleteAssignmentCommand() {
        LOGGER.info(getClass().getSimpleName() + " instance created!");
    }

    @Override
    public CommandResult execute(SessionRequestContent sessionRequestContent) {
        LOGGER.trace("Entering the method");
        User currentUser = (User)sessionRequestContent.getSessionAttribute("current_user");
        long assignmentId = Long.parseLong(sessionRequestContent.getSingleRequestParameter("id"));
        List<String> validationFails = new ArrayList<>();
        String jsonString = null;

        try {
            AssignmentService.deleteAssignment(assignmentId);
            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("success", MessageManager.getProperty("assignment.deleted", currentUser.getLanguage()));
            jsonString = JsonSerializer.serialize(responseMap);
            sessionRequestContent.addRequestAttribute("jsonString", jsonString);
            LOGGER.trace("Leaving the method");
            return new CommandResult(PageManager.getProperty("page.json"));
        } catch (ErrorMessageKeysContainedException e) {
            LOGGER.error("Error caught while executing the method:", e);
            String generalError = e.getErrorMesageKeys().get(0);
            jsonString = ErrorResponseCreator.createResponseWithErrors(validationFails,
                    generalError, currentUser.getLanguage());
            sessionRequestContent.addRequestAttribute("jsonString", jsonString);
            LOGGER.trace("Entering the method");
            return new CommandResult(PageManager.getProperty("page.json"));
        }
    }


}
