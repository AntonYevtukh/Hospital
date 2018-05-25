package commands.examination;

import commands.ActionDbCommand;
import exceptions.ErrorMessageKeysContainedException;
import exceptions.UnknownSqlException;
import model.entities.Diagnose;
import model.entities.User;
import resource_managers.PageManager;
import services.DiagnoseService;
import services.UserService;
import utils.CommandResult;
import utils.SessionRequestContent;

import java.util.List;



public class NewExaminationCommand implements ActionDbCommand {

    private static NewExaminationCommand instance;

    public static NewExaminationCommand getInstance() {
        if (instance == null) {
            synchronized (NewExaminationCommand.class) {
                if (instance == null)
                    instance = new NewExaminationCommand();
            }
        }
        return instance;
    }


    @Override
    public CommandResult execute(SessionRequestContent sessionRequestContent) {
        User currentUser = (User)sessionRequestContent.getSessionAttribute("current_user");

        try {
            long patientId = 0;
            List<Diagnose> diagnoses = DiagnoseService.getAllDiagnoses();
            try {
                patientId = Long.parseLong(sessionRequestContent.getSingleRequestParameter("patient_id"));
            } catch (NumberFormatException | NullPointerException e) {
                e.printStackTrace();
            }
            if (patientId == 0) {
                List<User> patients = UserService.getAllUsersByRole(1L);
                patients.removeIf((User patient) -> patient.getId() == currentUser.getId());
                sessionRequestContent.addRequestAttribute("patients", patients);
            } else {
                User patient = UserService.getUserById(patientId);
                if (!patient.isHospitalized()) {
                    sessionRequestContent.addRequestAttribute("hospitalize_show", true);
                } else {
                    sessionRequestContent.addRequestAttribute("discharge_show", true);
                }
                sessionRequestContent.addRequestAttribute("patient", patient);
            }
            sessionRequestContent.addRequestAttribute("title", "title.new_examination");
            sessionRequestContent.addRequestAttribute("diagnoses", diagnoses);
            return new CommandResult(PageManager.getProperty("page.create_examination"), false);
        } catch (ErrorMessageKeysContainedException e) {
            sessionRequestContent.addSessionAttribute("errors", e.getErrorMesageKeys());
            return new CommandResult(PageManager.getProperty("page.error"), false);
        } catch (UnknownSqlException e) {
            sessionRequestContent.addSessionAttribute("errors", e.getMessage());
            return new CommandResult(PageManager.getProperty("page.error"), false);
        }
    }


}
