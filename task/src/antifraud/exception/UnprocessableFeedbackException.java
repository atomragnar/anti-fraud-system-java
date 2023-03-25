package antifraud.exception;

public class UnprocessableFeedbackException extends RuntimeException {

    private String message;

    public UnprocessableFeedbackException() {
        super();
    }

    public UnprocessableFeedbackException(String message) {
        super(message);
    }


}
