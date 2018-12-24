package web.restful;

public class RestfulException extends Exception {

    public int error;
    public String reason;

    public RestfulException() {

    }

    public RestfulException(int error, String reason)
    {
        this.error = error;
        this.reason = reason;
    }

    @Override
    public String getMessage() {
        return reason + "(" + error + ")";
    }
}
