package ro.mta.se.lab.Exception;

public class HandleException extends Exception {
    private String _mMessage;

    public HandleException(String message) {
        this._mMessage=message;
    }

    public String getMessage(){
        return this._mMessage;
    }
}
