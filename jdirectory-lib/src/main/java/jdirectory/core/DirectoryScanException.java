package jdirectory.core;

/**
 * Indicates that an error has been encountered while scanning a directory for items.
 * 
 * @author Alexander Yurinsky
 */
public class DirectoryScanException extends Exception {
    private static final long serialVersionUID = 4654515646465431313L;

    /**
     * Constructs a new exception with the specified detail message and
     * cause.
     *
     * @param  message The detail message.
     * @param  cause The cause.
     */
    public DirectoryScanException(String message, Throwable cause) {
        super(message, cause);
    }
}
