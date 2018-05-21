package utils.parsers;

import model.entities.Assignment;
import model.entities.AssignmentType;
import model.entities.Examination;
import model.entities.User;
import utils.DateParser;
import utils.SessionRequestContent;
import utils.json.JsonSerializer;

import java.util.Locale;

public class AssignmentParser {

    public static Assignment parseAssignment(SessionRequestContent sessionRequestContent) {
        User currentUser = (User)sessionRequestContent.getSessionAttribute("current_user");
        Assignment assignment = new Assignment();
        if ("application/json".equals(sessionRequestContent.getRequestContentType())) {
            assignment = JsonSerializer.deserialize(Assignment.class,
                    sessionRequestContent.getRequestBodyString());
        } else {
            try {
                assignment.setId(Long.parseLong(sessionRequestContent.getSingleRequestParameter("id")));
            } catch (NullPointerException | NumberFormatException e) {

            }
            assignment.setExamination(new Examination(
                    Long.parseLong(sessionRequestContent.getSingleRequestParameter("examination"))));
            assignment.setAssignmentType(new AssignmentType(
                    Long.parseLong(sessionRequestContent.getSingleRequestParameter("assignment_type"))));
            assignment.setDoctor(new User(
                    Long.parseLong(sessionRequestContent.getSingleRequestParameter("doctor"))));
            assignment.setPatient(new User(
                    Long.parseLong(sessionRequestContent.getSingleRequestParameter("patient"))));
            assignment.setExecutor(new User(
                    Long.parseLong(sessionRequestContent.getSingleRequestParameter("executor"))));
            Locale userLocale = new Locale(currentUser.getLanguage());
            String startDateString = sessionRequestContent.getSingleRequestParameter("start_date");
            String endDateString = sessionRequestContent.getSingleRequestParameter("end_date");
            assignment.setStartDate(DateParser.parseDate(startDateString, userLocale, "date.regular"));
            assignment.setEndDate(DateParser.parseDate(endDateString, userLocale, "date.regular"));
            assignment.setDescription(sessionRequestContent.getSingleRequestParameter("description"));
        }
        return assignment;
    }
}
