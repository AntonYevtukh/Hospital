package commands.assignment;

import commands.ActionDbCommand;
import exceptions.ErrorMessageKeysContainedException;
import model.entities.User;
import resource_managers.MessageManager;
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

    @Override
    public CommandResult execute(SessionRequestContent sessionRequestContent) {
        User currentUser = (User)sessionRequestContent.getSessionAttribute("current_user");
        long assignmentId = Long.parseLong(sessionRequestContent.getSingleRequestParameter("id"));
        List<String> validationFails = new ArrayList<>();
        String ajaxString = null;

        try {
            AssignmentService.deleteAssignment(assignmentId);
            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("success", MessageManager.getProperty("assignment.deleted", currentUser.getLanguage()));
            ajaxString = JsonSerializer.serialize(responseMap);
            return new CommandResult("", true, ajaxString, false);
        } catch (ErrorMessageKeysContainedException e) {
            String generalError = e.getErrorMesageKeys().get(0);
            ajaxString = ErrorResponseCreator.createResponseWithErrors(validationFails,
                    generalError, currentUser.getLanguage());
            return new CommandResult("", true, ajaxString, false);
        }
    }


}
