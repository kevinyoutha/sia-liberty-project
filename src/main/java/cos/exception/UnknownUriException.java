package cos.exception;

public class UnknownUriException extends Exception {

    private static final long serialVersionUID = 1L;

    public UnknownUriException() {
        super();
    }

    public UnknownUriException(String message) {
        super(message);
    }
}