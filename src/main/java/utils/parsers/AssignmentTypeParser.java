package utils.parsers;

import model.entities.AssignmentType;
import model.entities.Role;
import utils.SessionRequestContent;
import utils.json.JsonSerializer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class AssignmentTypeParser {

    public static AssignmentType parseAssignmentType(SessionRequestContent sessionRequestContent) {
        AssignmentType assignmentType = new AssignmentType();
        if ("application/json".equals(sessionRequestContent.getRequestContentType())) {
            assignmentType = JsonSerializer.deserialize(AssignmentType.class,
                    sessionRequestContent.getRequestBodyString());
        } else {
            try {
                assignmentType.setId(Long.parseLong(sessionRequestContent.getSingleRequestParameter("id")));
            } catch (NullPointerException | NumberFormatException e) {

            }
            assignmentType.setName(sessionRequestContent.getSingleRequestParameter("name"));
            if (sessionRequestContent.getRequestParameter("hospitalization_required") != null)
                assignmentType.setHospitalizationRequired(true);
        }
        return assignmentType;
    }
}
