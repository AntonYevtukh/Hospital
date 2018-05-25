package commands.hospitalization;

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


public class NewHospitalizationCommand implements ActionDbCommand {

    private static NewHospitalizationCommand instance;

    public static NewHospitalizationCommand getInstance() {
        if (instance == null) {
            synchronized (NewHospitalizationCommand.class) {
                if (instance == null)
                    instance = new NewHospitalizationCommand();
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
                List<User> nonHospitalizedPatients = UserService.getAllUsersByRoleAndHospitalizationStatus(1L, false);
                nonHospitalizedPatients.removeIf((User patient) -> patient.getId() == currentUser.getId());
                sessionRequestContent.addRequestAttribute("patients", nonHospitalizedPatients);
            } else {
                User patient = UserService.getUserById(patientId);
                sessionRequestContent.addRequestAttribute("hospitalize_show", true);
                sessionRequestContent.addRequestAttribute("hospitalize", true);
                sessionRequestContent.addRequestAttribute("patient", patient);
            }
            sessionRequestContent.addRequestAttribute("title", "title.new_hospitalization");
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
