package commands.examination;

import commands.ActionDbCommand;
import exceptions.ErrorMessageKeysContainedException;
import exceptions.UnknownSqlException;
import model.entities.Diagnose;
import model.entities.Examination;
import model.entities.User;
import resource_managers.PageManager;
import services.DiagnoseService;
import services.ExaminationService;
import utils.CommandResult;
import utils.SessionRequestContent;

import java.util.List;


public class EditExaminationCommand implements ActionDbCommand {

    private static EditExaminationCommand instance;

    public static EditExaminationCommand getInstance() {
        if (instance == null) {
            synchronized (EditExaminationCommand.class) {
                if (instance == null)
                    instance = new EditExaminationCommand();
            }
        }
        return instance;
    }


    @Override
    public CommandResult execute(SessionRequestContent sessionRequestContent) {
        User currentUser = (User)sessionRequestContent.getSessionAttribute("current_user");
        long examinationId = Long.parseLong(sessionRequestContent.getSingleRequestParameter("id"));
        try {
            List<Diagnose> diagnoses = DiagnoseService.getAllDiagnoses();
            sessionRequestContent.addRequestAttribute("title", "title.edit_examination");
            sessionRequestContent.addRequestAttribute("diagnoses", diagnoses);
            Examination examination = ExaminationService.getExaminationById(examinationId);
            sessionRequestContent.addRequestAttribute("examination", examination);
            return new CommandResult(PageManager.getProperty("page.edit_examination"), false);
        } catch (ErrorMessageKeysContainedException e) {
            sessionRequestContent.addSessionAttribute("errors", e.getErrorMesageKeys());
            return new CommandResult(PageManager.getProperty("page.error"), false);
        } catch (UnknownSqlException e) {
            sessionRequestContent.addSessionAttribute("errors", e.getMessage());
            return new CommandResult(PageManager.getProperty("page.error"), false);
        }
    }


}
