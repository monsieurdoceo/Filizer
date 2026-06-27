package io.github.monsieurdoceo.filizer.shared.logging;

import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Bukkit-backed application logger.
 */
public final class BukkitAppLogger implements AppLogger {

    /**
     * The Bukkit logger.
     */
    private final Logger logger;

    /**
     * The prefix to use for all log messages.
     */
    private static final String PREFIX = "[Filizer] ";

    /**
     * The currently logged message.
     */
    private String loggedMessage = "";

    /**
     * Creates a new logger adapter.
     *
     * @param logger the Bukkit logger
     */
    public BukkitAppLogger(Logger logger) {
        this.logger = Objects.requireNonNull(logger, "logger");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getCurrentlyLoggedMessage() {
        return this.loggedMessage;
    }

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
