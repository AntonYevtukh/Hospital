package validation;

import model.entities.AssignmentType;

import java.util.ArrayList;
import java.util.List;

public class AssignmentTypeValidator implements EntityValidator<AssignmentType> {

    private static AssignmentTypeValidator instance;

    public static AssignmentTypeValidator getInstance() {
        if (instance == null) {
            synchronized (AssignmentTypeValidator.class) {
                if (instance == null)
                    instance = new AssignmentTypeValidator();
            }
        }
        return instance;
    }

    private AssignmentTypeValidator() {

    }

    @Override
    public List<String> validate(AssignmentType assignmentType) {
        List<String> errorMessageKeys = new ArrayList<>(1);
        if (assignmentType.getName() == null || assignmentType.getName().isEmpty() ||
                assignmentType.getName().length() > 30) {
            errorMessageKeys.add("validation.assignment_type.name");
        }
        return errorMessageKeys;
    }
}
