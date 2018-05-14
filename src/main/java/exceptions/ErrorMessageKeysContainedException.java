package exceptions;

import java.util.List;

public class ErrorMessageKeysContainedException extends RuntimeException {

    private List<String> errorMesageKeys;

    public ErrorMessageKeysContainedException(List<String> errorMesageKeys) {
        this.errorMesageKeys = errorMesageKeys;
    }

    public List<String> getErrorMesageKeys() {
        return errorMesageKeys;
    }
}
