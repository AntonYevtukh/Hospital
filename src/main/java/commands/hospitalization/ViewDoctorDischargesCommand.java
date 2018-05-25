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

public class ViewDoctorDischargesCommand implements ActionDbCommand {
    private static ViewDoctorDischargesCommand instance;

    public static ViewDoctorDischargesCommand getInstance() {
        if (instance == null) {
            synchronized (ViewDoctorDischargesCommand.class) {
                if (instance == null)
                    instance = new ViewDoctorDischargesCommand();
            }
        }
        return instance;
    }

    private ViewDoctorDischargesCommand() {
    }

    @Override
    public CommandResult execute(SessionRequestContent sessionRequestContent) {
        int page = Integer.parseInt(sessionRequestContent.getSingleRequestParameter("page"));
        int dischargedDoctorId = Integer.parseInt(sessionRequestContent.getSingleRequestParameter("discharged_doctor_id"));
        User dischargedDoctor = UserService.getUserById(dischargedDoctorId);
        try {
            User currentUser = (User)sessionRequestContent.getSessionAttribute("current_user");
            if (!currentUser.getRoleMap().containsKey(3L)) {
                sessionRequestContent.addRequestAttribute("error_message", "error.access");
                return new CommandResult(PageManager.getProperty("page.error"));
            }
            PageContent<Hospitalization> pageContent =
                    HospitalizationService.getHospitalizationsForPageByDischargedDoctorId(dischargedDoctorId, page, currentUser.getItemsPerPage());
            sessionRequestContent.addRequestAttribute("title", "hospitalization.hospitalizations_discharged");
            sessionRequestContent.addRequestAttribute("page_content", pageContent);
            sessionRequestContent.addRequestAttribute("url_pattern",
                    "/serv?action=view_doctor_discharges&discharged_doctor_id=" + dischargedDoctorId + "&page=");
            sessionRequestContent.addRequestAttribute("user", dischargedDoctor);
            return new CommandResult(PageManager.getProperty("page.view_hospitalizations"));
        } catch (RuntimeException e) {
            e.getMessage();
            e.printStackTrace();
            return new CommandResult(PageManager.getProperty("page.error"));
        }
    }
}
