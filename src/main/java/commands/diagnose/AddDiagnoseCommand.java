package commands.diagnose;

import commands.ActionDbCommand;
import exceptions.ErrorMessageKeysContainedException;
import model.entities.Diagnose;
import model.entities.User;
import resource_managers.MessageManager;
import services.DiagnoseService;
import utils.CommandResult;
import utils.SessionRequestContent;
import utils.json.ErrorResponseCreator;
import utils.json.JsonSerializer;
import utils.parsers.DiagnoseParser;
import validation.EntityValidatorFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class AddDiagnoseCommand implements ActionDbCommand {

    private static AddDiagnoseCommand instance;

    public static AddDiagnoseCommand getInstance() {
        if (instance == null) {
            synchronized (AddDiagnoseCommand.class) {
                if (instance == null)
                    instance = new AddDiagnoseCommand();
            }
        }
        return instance;
    }


    @Override
    public CommandResult execute(SessionRequestContent sessionRequestContent) {
        User currentUser = (User)sessionRequestContent.getSessionAttribute("current_user");
        Diagnose diagnose = DiagnoseParser.parseDiagnose(sessionRequestContent);
        List<String> validationFails = EntityValidatorFactory.getValidatorFor(Diagnose.class).validate(diagnose);
        String ajaxString = null;

        if (!validationFails.isEmpty()) {
            ajaxString = ErrorResponseCreator.createResponseWithErrors(validationFails, null, currentUser.getLanguage());
            return new CommandResult("", true, ajaxString, false);
        }

        try {
            DiagnoseService.addDiagnose(diagnose);
            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("success", MessageManager.getProperty("diagnose.added", currentUser.getLanguage()));
            ajaxString = JsonSerializer.serialize(responseMap);
            return new CommandResult("", true, ajaxString, false);
        } catch (ErrorMessageKeysContainedException e) {
            validationFails.addAll(e.getErrorMesageKeys());
            ajaxString = ErrorResponseCreator.createResponseWithErrors(validationFails, null, currentUser.getLanguage());
            return new CommandResult("", true, ajaxString, false);
        }
    }


}
