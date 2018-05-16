package utils.parsers;

import model.entities.AssignmentType;
import model.entities.Role;
import utils.SessionRequestContent;
import utils.json.JsonSerializer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class RoleParser {

    public static Role parseRole(SessionRequestContent sessionRequestContent) {
        Role role = new Role();
        if ("application/json".equals(sessionRequestContent.getRequestContentType())) {
            role = JsonSerializer.deserialize(Role.class,
                    sessionRequestContent.getRequestBodyString());
        } else {
            try {
                role.setId(Long.parseLong(sessionRequestContent.getSingleRequestParameter("id")));
            } catch (NullPointerException | NumberFormatException e) {

            }
            role.setName(sessionRequestContent.getSingleRequestParameter("name"));
            String[] assignmentTypeIds = sessionRequestContent.getRequestParameter("assignment_type_id");
            List<AssignmentType> assignmentTypes = new ArrayList<>();
            if (assignmentTypeIds != null && assignmentTypeIds.length != 0) {
                assignmentTypes =
                        Arrays.stream(sessionRequestContent.getRequestParameter("assignment_type_id")).map(
                                (String assignmentTypeIdString) -> new AssignmentType(Long.parseLong(assignmentTypeIdString))
                        ).collect(Collectors.toList());

            }
            role.setAllowedAssignmentTypes(assignmentTypes);
        }
        return role;
    }
}
