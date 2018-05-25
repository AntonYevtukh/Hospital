package commands.hospitalization;

import commands.ActionDbCommand;
import model.entities.Hospitalization;
import model.entities.User;
import resource_managers.PageManager;
import services.HospitalizationService;
import services.UserService;
import utils.CommandResult;
import utils.PageContent;
import utils.SessionRequestContent;

public class ViewPatientHospitalizationsCommand implements ActionDbCommand {
    private static ViewPatientHospitalizationsCommand instance;

    public static ViewPatientHospitalizationsCommand getInstance() {
        if (instance == null) {
            synchronized (ViewPatientHospitalizationsCommand.class) {
                if (instance == null)
                    instance = new ViewPatientHospitalizationsCommand();
            }
        }
        return instance;
    }

    private ViewPatientHospitalizationsCommand() {
    }

    @Override
    public CommandResult execute(SessionRequestContent sessionRequestContent) {
        int page = Integer.parseInt(sessionRequestContent.getSingleRequestParameter("page"));
        int patientId = Integer.parseInt(sessionRequestContent.getSingleRequestParameter("patient_id"));
        User patient = UserService.getUserById(patientId);
        try {
            User currentUser = (User)sessionRequestContent.getSessionAttribute("current_user");
            if (!currentUser.getRoleMap().containsKey(3L) || currentUser.getId() != patientId) {
                sessionRequestContent.addRequestAttribute("error_message", "error.access");
                return new CommandResult(PageManager.getProperty("page.error"));
            }
            PageContent<Hospitalization> pageContent =
                    HospitalizationService.getHospitalizationsForPageByPatientId(patientId, page, currentUser.getItemsPerPage());
            sessionRequestContent.addRequestAttribute("title", "hospitalization.hospitalizations_patient");
            sessionRequestContent.addRequestAttribute("page_content", pageContent);
            sessionRequestContent.addRequestAttribute("url_pattern",
                    "/serv?action=view_patient_hospitalizations&patient_id=" + patientId + "&page=");
            sessionRequestContent.addRequestAttribute("user", patient);
            return new CommandResult(PageManager.getProperty("page.view_hospitalizations"));
        } catch (RuntimeException e) {
            e.getMessage();
            e.printStackTrace();
            return new CommandResult(PageManager.getProperty("page.error"));
        }
    }
}
