package com.codeberg.monsieurdoceo.filizer.shared.logging;

import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Bukkit-backed application logger.
 */
public final class BukkitAppLogger implements AppLogger {

    /**********************************************************/
    /*********************** PROPERTIES ***********************/
    /**********************************************************/

    private final Logger logger;
    private static final String PREFIX = "[Filizer] ";
    private String loggedMessage = "";

    /*********************************************************/
    /********************** CONSTRUCTOR **********************/
    /*********************************************************/

    /**
     * Creates a new logger adapter.
     *
     * @param logger the Bukkit logger
     */
    public BukkitAppLogger(Logger logger) { this.logger = Objects.requireNonNull(logger, "logger"); }

    /*********************************************************/
    /*********************** FUNCTIONS ***********************/
    /*********************************************************/

    /**
     * {@inheritDoc}
     */
    @Override
    public String getCurrentlyLoggedMessage() { return this.loggedMessage; }

    // ############################################### //

    /**
     * {@inheritDoc}
     */
    @Override
    public void info(String message) {
        this.loggedMessage = PREFIX + message;
        this.logger.info(this.loggedMessage);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void warn(String message) {
        this.loggedMessage = PREFIX + message;
        this.logger.warning(this.loggedMessage);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void error(String message) {
        this.loggedMessage = PREFIX + message;
        this.logger.severe(this.loggedMessage);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void error(String message, Throwable throwable) {
        this.loggedMessage = PREFIX + message;
        this.logger.log(Level.SEVERE, this.loggedMessage, throwable);
    }
}
