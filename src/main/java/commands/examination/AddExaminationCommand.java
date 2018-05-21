package commands.examination;

import commands.ActionDbCommand;
import exceptions.ErrorMessageKeysContainedException;
import model.entities.Examination;
import model.entities.User;
import resource_managers.PageManager;
import services.ExaminationService;
import utils.CommandResult;
import utils.SessionRequestContent;
import utils.parsers.ExaminationParser;
import validation.EntityValidatorFactory;

import java.util.List;


public class AddExaminationCommand implements ActionDbCommand {

    private static AddExaminationCommand instance;

    public static AddExaminationCommand getInstance() {
        if (instance == null) {
            synchronized (AddExaminationCommand.class) {
                if (instance == null)
                    instance = new AddExaminationCommand();
            }
        }
        return instance;
    }


    @Override
    public CommandResult execute(SessionRequestContent sessionRequestContent) {
        User currentUser = (User)sessionRequestContent.getSessionAttribute("current_user");
        Examination examination = ExaminationParser.parseExamination(sessionRequestContent);
        List<String> validationFails = EntityValidatorFactory.getValidatorFor(Examination.class).validate(examination);
        if (!validationFails.isEmpty()) {
            sessionRequestContent.addRequestAttribute("examination", examination);
            sessionRequestContent.addRequestAttribute("validationFails", validationFails);
            return new CommandResult(PageManager.getProperty("page.edit_examination"));
        }

        try {
            long examinationId = ExaminationService.addExamination(examination);
            examination.setId(examinationId);
            return new CommandResult("/serv?action=edit_examination&id=" + examination.getId(), true);
        } catch (ErrorMessageKeysContainedException e) {
            validationFails.addAll(e.getErrorMesageKeys());
            sessionRequestContent.addRequestAttribute("validationFails", validationFails);
            return new CommandResult(PageManager.getProperty("page.create_examination"));
        }
    }
}
