package utils.json;

import resource_managers.MessageManager;
import resource_managers.ValidationManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ErrorResponseCreator {

    private ErrorResponseCreator() {
    }

    public static String createResponseWithErrors(List<String> errors, String generalError, String language) {
        Map<String, Object> responseMap = new HashMap<>();
        String ajaxString = null;
        Map<String, String> localizedErrorsMap = errors.stream().
                collect(Collectors.toMap((String errorMessageKey) -> errorMessageKey.replace(".", "_"),
                        (String errorMessageKey) -> ValidationManager.getProperty(errorMessageKey, language)));
        responseMap.put("errors", localizedErrorsMap);
        if (generalError != null && !generalError.isEmpty()) {
            responseMap.put("error", MessageManager.getProperty(generalError, language));
        }
        ajaxString = JsonSerializer.serialize(responseMap);
        return ajaxString;
    }
}
