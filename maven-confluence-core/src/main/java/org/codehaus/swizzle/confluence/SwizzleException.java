package org.codehaus.swizzle.confluence;

/**
 * This is the exception thrown by the Swizzle to signal errors. Errors that occurred on the server are indicated by throwing a {@link org.codehaus.swizzle.confluence.ConfluenceException}, which is a
 * subclass of SwizzleException.
 */
public class SwizzleException extends Exception {
    private static final long serialVersionUID = 4697548022557085636L;

    public SwizzleException() {
        super();
    }

    public SwizzleException(String message) {
        super(message);
    }

    public SwizzleException(Throwable cause) {
        super(cause);
    }

    public SwizzleException(String message, Throwable cause) {
        super(message, cause);
    }
}