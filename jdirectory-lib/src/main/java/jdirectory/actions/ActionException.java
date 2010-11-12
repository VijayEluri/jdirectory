package jdirectory.actions;

/**
 * Indicates that an error has encountered while performing JDirectory action.
 *
 * @author Alexander Yurinsky
 */
public class ActionException extends Exception {
    private static final long serialVersionUID = 1021015406454542000L;

    /**
     * Constructs a new exception with the specified detail message and
     * cause.
     *
     * @param  message The detail message.
     * @param  cause The cause.
     */
    public ActionException(String message, Throwable cause) {
        super(message, cause);
    }
}
