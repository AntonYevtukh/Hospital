package validation;

import model.entities.Examination;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ExaminationValidator implements EntityValidator<Examination> {

    private static ExaminationValidator instance;

    public static ExaminationValidator getInstance() {
        if (instance == null) {
            synchronized (ExaminationValidator.class) {
                if (instance == null)
                    instance = new ExaminationValidator();
            }
        }
        return instance;
    }

    private ExaminationValidator() {

    }

    @Override
    public List<String> validate(Examination examination) {
        List<String> errorMessageKeys = new ArrayList<>(1);
        if (examination.getDate() == null || examination.getDate().compareTo(new Date()) > 0) {
            errorMessageKeys.add("examination.date");
        }
        return errorMessageKeys;
    }
}
