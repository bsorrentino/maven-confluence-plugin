package org.codehaus.swizzle.confluence;

/**
 * This exception is thrown to signal an error on the server. Sometimes the original cause of the error is also transmitted and can be obtained by calling the getCause method (otherwise getCause will
 * return null).
 */
public class ConfluenceException extends SwizzleException {
    private static final long serialVersionUID = 4697548022557085636L;

    public ConfluenceException() {
        super();
    }

    public ConfluenceException(String message) {
        super(message);
    }

    public ConfluenceException(Throwable cause) {
        super(cause);
    }

    public ConfluenceException(String message, Throwable cause) {
        super(message, cause);
    }
}
