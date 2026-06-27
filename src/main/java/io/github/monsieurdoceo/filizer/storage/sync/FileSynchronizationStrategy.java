package com.codeberg.monsieurdoceo.filizer.storage.sync;
import com.codeberg.monsieurdoceo.filizer.storage.domain.CustomFile;

/**
 * Defines how a {@link CustomFile} should be synchronized
 * with its underlying filesystem representation.
 */
public interface FileSynchronizationStrategy {

    /**
     * Synchronizes the given file if required.
     *
     * @param file the file to synchronize
     */
    void synchronize(CustomFile file);
}