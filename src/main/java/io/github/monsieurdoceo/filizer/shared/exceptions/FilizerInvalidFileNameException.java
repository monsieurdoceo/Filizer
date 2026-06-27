package io.github.monsieurdoceo.filizer.shared.exceptions;

import io.github.monsieurdoceo.filizer.shared.logging.AppLogger;
import java.util.Objects;

/**
 * Thrown when a file name is invalid.
 */
public final class FilizerInvalidFileNameException
    extends IllegalArgumentException
{

    /**
     * Creates a new invalid-file-name exception.
     *
     * @param logger the application logger
     * @param name the file name
     * @param cause the original cause
     */
    public FilizerInvalidFileNameException(
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
     * @param name the file name
     * @param cause the original cause
     * @return the exception message
     */
    private static String message(
        AppLogger logger,
        String name,
        Throwable cause
    ) {
        logger.error("Invalid file name: " + name, cause);
        return logger.getCurrentlyLoggedMessage();
    }
}
