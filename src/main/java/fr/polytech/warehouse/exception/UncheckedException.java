package fr.polytech.warehouse.exception;

public class UncheckedException extends RuntimeException {

    private static final long serialVersionUID = 8826452555089726422L;

    public UncheckedException(Exception e) {
        super(e);
    }

    public UncheckedException(String msg, Exception e) {
        super(msg, e);
    }

}
