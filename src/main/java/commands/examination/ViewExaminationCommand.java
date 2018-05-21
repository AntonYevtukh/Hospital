package commands.examination;

import commands.ActionDbCommand;
import exceptions.ErrorMessageKeysContainedException;
import exceptions.UnknownSqlException;
import model.entities.Examination;
import model.entities.User;
import resource_managers.PageManager;
import services.ExaminationService;
import utils.CommandResult;
import utils.SessionRequestContent;


public class ViewExaminationCommand implements ActionDbCommand {

    private static ViewExaminationCommand instance;

    public static ViewExaminationCommand getInstance() {
        if (instance == null) {
            synchronized (ViewExaminationCommand.class) {
                if (instance == null)
                    instance = new ViewExaminationCommand();
            }
        }
        return instance;
    }


    @Override
    public CommandResult execute(SessionRequestContent sessionRequestContent) {
        User currentUser = (User)sessionRequestContent.getSessionAttribute("current_user");
        long examinationId = Long.parseLong(sessionRequestContent.getSingleRequestParameter("id"));
        try {
            sessionRequestContent.addRequestAttribute("title", "title.view_examination");
            Examination examination = ExaminationService.getExaminationById(examinationId);
            sessionRequestContent.addRequestAttribute("examination", examination);
            return new CommandResult(PageManager.getProperty("page.view_examination"), false);
        } catch (ErrorMessageKeysContainedException e) {
            sessionRequestContent.addSessionAttribute("errors", e.getErrorMesageKeys());
            return new CommandResult(PageManager.getProperty("page.error"), false);
        } catch (UnknownSqlException e) {
            sessionRequestContent.addSessionAttribute("errors", e.getMessage());
            return new CommandResult(PageManager.getProperty("page.error"), false);
        }
    }


}
