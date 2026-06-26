package com.codeberg.monsieurdoceo.filizer.shared.exceptions;

import com.codeberg.monsieurdoceo.filizer.shared.logging.AppLogger;
import java.nio.file.Path;
import java.util.Objects;

/**
 * Centralized factory for Filizer runtime exceptions.
 */
public final class FilizerExceptions {

    /*********************************************************/
    /*********************** FUNCTIONS ***********************/
    /*********************************************************/

    private final AppLogger logger;

    /*********************************************************/
    /********************** CONSTRUCTOR **********************/
    /*********************************************************/

    /**
     * Creates a new exception factory.
     *  
     * @param logger the application logger
     */
    public FilizerExceptions(AppLogger logger) { this.logger = Objects.requireNonNull(logger, "logger"); }

    /*********************************************************/
    /*********************** FUNCTIONS ***********************/
    /*********************************************************/

    /**
     * Creates an exception for a missing file.
     *
     * @param name the file name
     * @param cause the original cause
     * @return the exception to throw
     */
    public IllegalArgumentException fileNotFound(String name, Throwable cause) {
        return new FilizerFileNotFoundException(this.logger, name, cause);
    }

    /**
     * Creates an exception for an invalid file path or name.
     *
     * @param path the file path
     * @param name the file name
     * @param cause the original cause
     * @return the exception to throw
     */
    public IllegalArgumentException invalidFilePath(Path path, String name, Throwable cause) {
        return new FilizerInvalidFilePathException(this.logger, path, name, cause);
    }

    /**
     * Creates an exception for an invalid file name.
     *
     * @param name the file name
     * @param cause the original cause
     * @return the exception to throw
     */
    public IllegalArgumentException invalidFileName(String name, Throwable cause) {
        return new FilizerInvalidFileNameException(this.logger, name, cause);
    }

    /**
     * Creates an exception for a failed file creation.
     *
     * @param path the file path
     * @param name the file name
     * @param cause the original cause
     * @return the exception to throw
     */
    public IllegalStateException fileCreationFailed(Path path, String name, Throwable cause) {
        return new FilizerFileCreationException(this.logger, path, name, cause);
    }

    /**
     * Creates an exception for a failed file deletion.
     *
     * @param name the file name
     * @param cause the original cause
     * @return the exception to throw
     */
    public IllegalStateException fileDeletionFailed(String name, Throwable cause) {
        return new FilizerFileDeletionException(this.logger, name, cause);
    }

    /**
     * Creates an exception for a failed file save.
     *
     * @param name the file name
     * @param cause the original cause
     * @return the exception to throw
     */
    public IllegalStateException fileSaveFailed(String name, Throwable cause) {
        return new FilizerFileSaveException(this.logger, name, cause);
    }

    /**
     * Logs an ambiguous file lookup.
     *
     * @param name the file name
     */
    public void ambiguousFileName(String name) { this.logger.warn("Ambiguous file name lookup ignored for: " + name); }
}
