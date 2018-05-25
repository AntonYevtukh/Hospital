package validation;

import model.entities.Hospitalization;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class HospitalizationValidator implements EntityValidator<Hospitalization> {

    private static HospitalizationValidator instance;

    public static HospitalizationValidator getInstance() {
        if (instance == null) {
            synchronized (HospitalizationValidator.class) {
                if (instance == null)
                    instance = new HospitalizationValidator();
            }
        }
        return instance;
    }

    private HospitalizationValidator() {

    }

    @Override
    public List<String> validate(Hospitalization hospitalization) {
        List<String> errorMessageKeys = new ArrayList<>(2);
        Date startDate = hospitalization.getStartDate();
        Date endDate = hospitalization.getEndDate();
        if (startDate == null) {
            errorMessageKeys.add("hospitalization.start_date");
        }
        if ((startDate != null && endDate != null && startDate.compareTo(endDate) > 0)) {
            errorMessageKeys.add("hospitalization.chrono_dates");
        }
        return errorMessageKeys;
    }
}
