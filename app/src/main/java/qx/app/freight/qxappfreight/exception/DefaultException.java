package qx.app.freight.qxappfreight.exception;

/**
 * Auther: RyanLi
 * Data: 2018-05-28 17:04
        * Description:
        */
public class DefaultException extends Exception {

    public static final String DATA_NULL_EXCEPTION = "Response [data] null Exception";

    public DefaultException(String message) {
        super(message);
    }
}