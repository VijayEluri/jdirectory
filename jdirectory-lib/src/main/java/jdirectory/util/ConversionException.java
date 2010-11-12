package jdirectory.util;

/**
 * Indicates that an error has encountered while converting data.
 *
 * @author Alexander Yurinsky
 */
public class ConversionException extends Exception {
    private static final long serialVersionUID = -1521848031302165464L;

    /**
     * Constructs a new exception with the specified detail message and
     * cause.
     *
     * @param  message The detail message.
     * @param  cause The cause.
     */
    public ConversionException(String message, Throwable cause) {
        super(message, cause);
    }
}
