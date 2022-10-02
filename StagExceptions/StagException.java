package StagExceptions;

public class StagException extends Exception {
    String error = "[STAG Error]: ";
    String errorMessage;

    public StagException(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public String toString() {
        return error + errorMessage;
    }
}
