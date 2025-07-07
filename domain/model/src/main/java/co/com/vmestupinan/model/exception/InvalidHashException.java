package co.com.vmestupinan.model.exception;

public class InvalidHashException extends RuntimeException {
    public InvalidHashException(String message) {
        super(message);
    }
}
