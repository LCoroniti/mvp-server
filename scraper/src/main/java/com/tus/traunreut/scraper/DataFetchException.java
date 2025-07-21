package com.tus.traunreut.scraper;

/**
 * Thrown to indicate an error occurred during a data scraping or fetch request.
 */
public class DataFetchException extends Exception {

    /**
     * Creates a new DataFetchException with no detail message.
     */
    public DataFetchException() {
        super();
    }

    /**
     * Creates a new DataFetchException with the specified detail message.
     *
     * @param message the detail message
     */
    public DataFetchException(String message) {
        super(message);
    }

    /**
     * Creates a new DataFetchException with the specified detail message and cause.
     *
     * @param message the detail message
     * @param cause the cause
     */
    public DataFetchException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Creates a new DataFetchException with the specified cause.
     *
     * @param cause the cause
     */
    public DataFetchException(Throwable cause) {
        super(cause);
    }
}
