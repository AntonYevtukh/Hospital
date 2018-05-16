package commands.diagnose;

import commands.ActionDbCommand;
import exceptions.ErrorMessageKeysContainedException;
import model.entities.User;
import resource_managers.MessageManager;
import services.DiagnoseService;
import utils.CommandResult;
import utils.SessionRequestContent;
import utils.json.ErrorResponseCreator;
import utils.json.JsonSerializer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class DeleteDiagnoseCommand implements ActionDbCommand {

    private static DeleteDiagnoseCommand instance;

    public static DeleteDiagnoseCommand getInstance() {
        if (instance == null) {
            synchronized (DeleteDiagnoseCommand.class) {
                if (instance == null)
                    instance = new DeleteDiagnoseCommand();
            }
        }
        return instance;
    }


    @Override
    public CommandResult execute(SessionRequestContent sessionRequestContent) {
        User currentUser = (User)sessionRequestContent.getSessionAttribute("current_user");
        long diagnoseId = Long.parseLong(sessionRequestContent.getSingleRequestParameter("id"));
        List<String> validationFails = new ArrayList<>();
        String ajaxString = null;

        try {
            DiagnoseService.deleteDiagnose(diagnoseId);
            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("success", MessageManager.getProperty("diagnose.deleted", currentUser.getLanguage()));
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
