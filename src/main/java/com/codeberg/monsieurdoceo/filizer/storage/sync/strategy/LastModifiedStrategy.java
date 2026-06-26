package com.codeberg.monsieurdoceo.filizer.storage.sync.strategy;
import com.codeberg.monsieurdoceo.filizer.storage.domain.CustomFile;
import com.codeberg.monsieurdoceo.filizer.storage.sync.FileSynchronizationStrategy;

/**
 * Synchronizes files using the filesystem last modified timestamp.
 */
public final class LastModifiedStrategy implements FileSynchronizationStrategy {

    /*********************************************************/
    /*********************** FUNCTIONS ***********************/
    /*********************************************************/

    /**
     * Synchronizes the given file if the last modified timestamp is newer.
     *
     * @param file the file to synchronize
     */
    @Override
    public void synchronize(CustomFile file) { if(file.getFile().lastModified() > file.getLastModified()) file.reload(); }
}