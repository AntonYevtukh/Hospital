package exceptions;

public class EntitySQLParseException extends RuntimeException {

    public EntitySQLParseException() {
    }

    public EntitySQLParseException(String message) {
        super(message);
    }
}
