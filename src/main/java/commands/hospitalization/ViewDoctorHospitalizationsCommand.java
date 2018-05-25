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

public class ViewDoctorHospitalizationsCommand implements ActionDbCommand {
    private static ViewDoctorHospitalizationsCommand instance;

    public static ViewDoctorHospitalizationsCommand getInstance() {
        if (instance == null) {
            synchronized (ViewDoctorHospitalizationsCommand.class) {
                if (instance == null)
                    instance = new ViewDoctorHospitalizationsCommand();
            }
        }
        return instance;
    }

    private ViewDoctorHospitalizationsCommand() {
    }

    @Override
    public CommandResult execute(SessionRequestContent sessionRequestContent) {
        int page = Integer.parseInt(sessionRequestContent.getSingleRequestParameter("page"));
        int acceptedDoctorId = Integer.parseInt(sessionRequestContent.getSingleRequestParameter("accepted_doctor_id"));
        User acceptedDoctor = UserService.getUserById(acceptedDoctorId);
        try {
            User currentUser = (User)sessionRequestContent.getSessionAttribute("current_user");
            if (!currentUser.getRoleMap().containsKey(3L)) {
                sessionRequestContent.addRequestAttribute("error_message", "error.access");
                return new CommandResult(PageManager.getProperty("page.error"));
            }
            PageContent<Hospitalization> pageContent =
                    HospitalizationService.getHospitalizationsForPageByAcceptedDoctorId(acceptedDoctorId, page, currentUser.getItemsPerPage());
            sessionRequestContent.addRequestAttribute("title", "hospitalization.hospitalizations_performed");
            sessionRequestContent.addRequestAttribute("page_content", pageContent);
            sessionRequestContent.addRequestAttribute("url_pattern",
                    "/serv?action=view_doctor_hospitalizations&accepted_doctor_id=" + acceptedDoctorId + "&page=");
            sessionRequestContent.addRequestAttribute("user", acceptedDoctor);
            return new CommandResult(PageManager.getProperty("page.view_hospitalizations"));
        } catch (RuntimeException e) {
            e.getMessage();
            e.printStackTrace();
            return new CommandResult(PageManager.getProperty("page.error"));
        }
    }
}
