package commands;

import commands.assignment.*;
import commands.authentication.SignInCommand;
import commands.authentication.SignOutCommand;
import commands.authentication.SignUpCommand;
import commands.diagnose.AddDiagnoseCommand;
import commands.diagnose.DeleteDiagnoseCommand;
import commands.diagnose.UpdateDiagnoseCommand;
import commands.diagnose.ViewDiagnosesCommand;
import commands.examination.*;
import commands.hospitalization.*;
import commands.settings.*;
import commands.user.*;
import model.database.ConnectionProvider;
import utils.SessionRequestContent;

import java.util.HashMap;
import java.util.Map;

public class ActionCommandFactory {

    private static Map<String, ActionCommand> actionCommandMap = new HashMap<>();

    static {
        actionCommandMap.put("empty_command", EmptyCommand.getInstance());
        actionCommandMap.put("sign_up", SignUpCommand.getInstance());
        actionCommandMap.put("sign_in", SignInCommand.getInstance());
        actionCommandMap.put("sign_out", SignOutCommand.getInstance());
        actionCommandMap.put("new_user", NewUserCommand.getInstance());
        actionCommandMap.put("view_user", ViewUserCommand.getInstance());
        actionCommandMap.put("update_user", UpdateUserCommand.getInstance());
        actionCommandMap.put("edit_user", EditUserCommand.getInstance());
        actionCommandMap.put("view_diagnoses", ViewDiagnosesCommand.getInstance());
        actionCommandMap.put("update_diagnose", UpdateDiagnoseCommand.getInstance());
        actionCommandMap.put("add_diagnose", AddDiagnoseCommand.getInstance());
        actionCommandMap.put("delete_diagnose", DeleteDiagnoseCommand.getInstance());
        actionCommandMap.put("view_settings", ViewSettingsCommand.getInstance());
        actionCommandMap.put("update_role", UpdateRoleCommand.getInstance());
        actionCommandMap.put("add_role", AddRoleCommand.getInstance());
        actionCommandMap.put("delete_role", DeleteRoleCommand.getInstance());
        actionCommandMap.put("update_assignment_type", UpdateAssignmentTypeCommand.getInstance());
        actionCommandMap.put("add_assignment_type", AddAssignmentTypeCommand.getInstance());
        actionCommandMap.put("delete_assignment_type", DeleteAssignmentTypeCommand.getInstance());
        actionCommandMap.put("view_users", ViewUsersCommand.getInstance());
        actionCommandMap.put("view_patients", ViewPatientsCommand.getInstance());
        actionCommandMap.put("view_nurses", ViewNursesCommand.getInstance());
        actionCommandMap.put("view_doctors", ViewDoctorsCommand.getInstance());
        actionCommandMap.put("delete_user", DeleteUserCommand.getInstance());
        actionCommandMap.put("new_examination", NewExaminationCommand.getInstance());
        actionCommandMap.put("add_examination", AddExaminationCommand.getInstance());
        actionCommandMap.put("edit_examination", EditExaminationCommand.getInstance());
        actionCommandMap.put("view_examination", ViewExaminationCommand.getInstance());
        actionCommandMap.put("view_examinations", ViewExaminationsCommand.getInstance());
        actionCommandMap.put("view_doctor_examinations", ViewDoctorExaminationsCommand.getInstance());
        actionCommandMap.put("view_patient_examinations", ViewPatientExaminationsCommand.getInstance());
        actionCommandMap.put("load_drop_downs_data", LoadDropDownsDataCommand.getInstance());
        actionCommandMap.put("update_assignment", UpdateAssignmentCommand.getInstance());
        actionCommandMap.put("add_assignment", AddAssignmentCommand.getInstance());
        actionCommandMap.put("delete_assignment", DeleteAssignmentCommand.getInstance());
        actionCommandMap.put("view_assignments", ViewAssignmentsCommand.getInstance());
        actionCommandMap.put("view_doctor_assignments", ViewDoctorAssignmentsCommand.getInstance());
        actionCommandMap.put("view_patient_assignments", ViewPatientAssignmentsCommand.getInstance());
        actionCommandMap.put("view_executor_assignments", ViewExecutorAssignmentsCommand.getInstance());
        actionCommandMap.put("new_hospitalization", NewHospitalizationCommand.getInstance());
        actionCommandMap.put("new_discharge", NewDischargeCommand.getInstance());
        actionCommandMap.put("view_hospitalization", ViewHospitalizationCommand.getInstance());
        actionCommandMap.put("view_hospitalizations", ViewHospitalizationsCommand.getInstance());
        actionCommandMap.put("view_patient_hospitalizations", ViewPatientHospitalizationsCommand.getInstance());
        actionCommandMap.put("view_doctor_hospitalizations", ViewDoctorHospitalizationsCommand.getInstance());
        actionCommandMap.put("view_doctor_discharges", ViewDoctorDischargesCommand.getInstance());
    }

    public static ActionCommand defineCommand(SessionRequestContent sessionRequestContent) {

        try {
            ActionCommand actionCommand = actionCommandMap.getOrDefault(
                    sessionRequestContent.getSingleRequestParameter("action"),
                    actionCommandMap.get("empty_command"));
            if (actionCommand instanceof ActionDbCommand) {
                return (SessionRequestContent sessionRequestContentArg) -> {
                    try {
                        ConnectionProvider.bindConnection();
                        return actionCommand.execute(sessionRequestContentArg);
                    } finally {
                        ConnectionProvider.unbindConnection();
                    }
                };
            } else {
                return actionCommand;
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
            return actionCommandMap.get("empty_command");
        }
    }
}
