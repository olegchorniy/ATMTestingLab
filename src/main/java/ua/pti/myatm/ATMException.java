package ua.pti.myatm;

/**
 *
 * @author Олег
 */
public class ATMException extends Exception {

    /**
     * Creates a new instance of <code>ATMException</code> without detail
     * message.
     */
    public ATMException() {
    }

    /**
     * Constructs an instance of <code>ATMException</code> with the specified
     * detail message.
     *
     * @param msg the detail message.
     */
    public ATMException(String msg) {
        super(msg);
    }
}
