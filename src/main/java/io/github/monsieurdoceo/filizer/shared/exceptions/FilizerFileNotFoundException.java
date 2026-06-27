package io.github.monsieurdoceo.filizer.shared.exceptions;

import io.github.monsieurdoceo.filizer.shared.logging.AppLogger;
import java.util.Objects;

/**
 * Thrown when a requested file cannot be found.
 */
public final class FilizerFileNotFoundException
    extends IllegalArgumentException
{

    /**
     * Creates a new file-not-found exception.
     *
     * @param logger the application logger
     * @param name the missing file name
     * @param cause the original cause
     */
    public FilizerFileNotFoundException(
        AppLogger logger,
        String name,
        Throwable cause
    ) {
        super(
            message(Objects.requireNonNull(logger, "logger"), name, cause),
            cause
        );
    }

    /**
     * Creates a message for the exception.
     *
     * @param logger the application logger
     * @param name the missing file name
     * @param cause the original cause
     * @return the exception message
     */
    private static String message(
        AppLogger logger,
        String name,
        Throwable cause
    ) {
        logger.error("File not found: " + name, cause);
        return logger.getCurrentlyLoggedMessage();
    }
}
