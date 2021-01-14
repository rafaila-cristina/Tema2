package ro.mta.se.lab.Exception;

public class HandleException extends Exception {
    private String _mMessage;

    public HandleException(String msg) {
        this._mMessage=msg;
    }

    public String getMessage(){
        return this._mMessage;
    }
}
