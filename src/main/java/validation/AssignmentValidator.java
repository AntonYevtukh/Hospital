package validation;

import model.entities.Assignment;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class AssignmentValidator implements EntityValidator<Assignment> {

    private static AssignmentValidator instance;

    public static AssignmentValidator getInstance() {
        if (instance == null) {
            synchronized (AssignmentValidator.class) {
                if (instance == null)
                    instance = new AssignmentValidator();
            }
        }
        return instance;
    }

    private AssignmentValidator() {

    }

    @Override
    public List<String> validate(Assignment assignment) {
        List<String> errorMessageKeys = new ArrayList<>(3);
        Date startDate = assignment.getStartDate();
        Date endDate = assignment.getEndDate();
        if (startDate == null) {
            errorMessageKeys.add("assignment.start_date");
        }
        if (endDate == null) {
            errorMessageKeys.add("assignment.end_date");
        }
        if ((startDate != null && endDate != null && startDate.compareTo(endDate) > 0)) {
            errorMessageKeys.add("assignment.chrono_dates");
        }
        return errorMessageKeys;
    }
}
