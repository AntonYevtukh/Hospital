package commands.assignment;

import commands.ActionDbCommand;
import exceptions.ErrorMessageKeysContainedException;
import model.entities.Assignment;
import model.entities.User;
import resource_managers.MessageManager;
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


    @Override
    public CommandResult execute(SessionRequestContent sessionRequestContent) {
        User currentUser = (User)sessionRequestContent.getSessionAttribute("current_user");
        Assignment assignment = AssignmentParser.parseAssignment(sessionRequestContent);
        List<String> validationFails = EntityValidatorFactory.getValidatorFor(Assignment.class).validate(assignment);
        String ajaxString = null;

        if (!validationFails.isEmpty()) {
            ajaxString = ErrorResponseCreator.createResponseWithErrors(validationFails, null, currentUser.getLanguage());
            return new CommandResult("", true, ajaxString, false);
        }

        try {
            AssignmentService.addAssignment(assignment);
            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("success", MessageManager.getProperty("assignment.added", currentUser.getLanguage()));
            ajaxString = JsonSerializer.serialize(responseMap);
            System.out.println(responseMap);
            return new CommandResult("", true, ajaxString, false);
        } catch (ErrorMessageKeysContainedException e) {
            validationFails.addAll(e.getErrorMesageKeys());
            ajaxString = ErrorResponseCreator.createResponseWithErrors(validationFails, null, currentUser.getLanguage());
            return new CommandResult("", true, ajaxString, false);
        }
    }
}
