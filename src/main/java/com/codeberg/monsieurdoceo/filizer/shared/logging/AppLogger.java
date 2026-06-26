package com.codeberg.monsieurdoceo.filizer.shared.logging;

/**
 * Application-level logger abstraction.
 */
public interface AppLogger {

    /*********************************************************/
    /*********************** FUNCTIONS ***********************/
    /*********************************************************/

    /**
     * Gets the currently logged message.
     * @return the currently logged message
     */
    String getCurrentlyLoggedMessage();

    // ############################################### //

    /**
     * Logs an informational message.
     *
     * @param message the message to log
     */
    void info(String message);

    /**
     * Logs a warning message.
     *
     * @param message the message to log
     */
    void warn(String message);

    /**
     * Logs an error message.
     *
     * @param message the message to log
     */
    void error(String message);

    /**
     * Logs an error message with a cause.
     *
     * @param message the message to log
     * @param throwable the cause
     */
    void error(String message, Throwable throwable);
}
