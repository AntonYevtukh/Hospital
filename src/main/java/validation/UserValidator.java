package validation;

import model.entities.User;
import resource_managers.RegexManager;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UserValidator implements EntityValidator<User> {

    private static UserValidator instance;

    public static UserValidator getInstance() {
        if (instance == null) {
            synchronized (UserValidator.class) {
                if (instance == null)
                    instance = new UserValidator();
            }
        }
        return instance;
    }

    private UserValidator() {

    }

    @Override
    public List<String> validate(User user) {
        List<String> errorMessageKeys = new ArrayList<>(7);
        if (user.getLogin() == null || !user.getLogin().matches(RegexManager.getProperty("regex.user.login"))) {
            errorMessageKeys.add("validation.user.login");
        }
        if (user.getPassword() == null || !user.getPassword().matches(RegexManager.getProperty("regex.user.password"))) {
            errorMessageKeys.add("validation.user.password");
        }
        if (user.getFirstName() == null || !user.getFirstName().matches(RegexManager.getProperty("regex.user.first_name"))) {
            errorMessageKeys.add("validation.user.first_name");
        }
        if (user.getLastName() == null || !user.getLastName().matches(RegexManager.getProperty("regex.user.last_name"))) {
            errorMessageKeys.add("validation.user.last_name");
        }
        if (user.getMiddleName() != null && !user.getMiddleName().isEmpty() &&
                !user.getMiddleName().matches(RegexManager.getProperty("regex.user.middle_name"))) {
            errorMessageKeys.add("validation.user.middle_name");
        }
        if (user.getDateOfBirth() == null || user.getDateOfBirth().compareTo(new Date()) > 0) {
            errorMessageKeys.add("validation.user.date_of_birth");
        }
        if (user.getPassportNumber() == null ||
                !user.getPassportNumber().matches(RegexManager.getProperty("regex.user.passport_number"))) {
            errorMessageKeys.add("validation.user.passport_number");
        }
        if (user.getEmail() != null && !user.getEmail().isEmpty() &&
                !user.getEmail().matches(RegexManager.getProperty("regex.user.email"))) {
            errorMessageKeys.add("validation.user.email");
        }
        if (user.getPhone() != null && !user.getPhone().isEmpty() &&
                !user.getPhone().matches(RegexManager.getProperty("regex.user.phone"))) {
            errorMessageKeys.add("validation.user.phone");
        }
        if (user.getRoleMap() == null || user.getRoleMap().isEmpty()) {
            errorMessageKeys.add("validation.user.roles");
        }
        return errorMessageKeys;
    }
}
