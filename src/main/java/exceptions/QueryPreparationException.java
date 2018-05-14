package exceptions;

public class QueryPreparationException extends RuntimeException {

    public QueryPreparationException() {
    }

    public QueryPreparationException(String message) {
        super(message);
    }
}
