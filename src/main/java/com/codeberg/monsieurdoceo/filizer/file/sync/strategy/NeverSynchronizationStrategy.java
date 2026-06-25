package com.codeberg.monsieurdoceo.filizer.file.sync.strategy;
import com.codeberg.monsieurdoceo.filizer.file.domain.CustomFile;
import com.codeberg.monsieurdoceo.filizer.file.sync.FileSynchronizationStrategy;

/**
 * Never synchronizes files.
 *
 * <p>This strategy assumes that files cannot be modified
 * externally.
 */
public final class NeverSynchronizationStrategy  implements FileSynchronizationStrategy {

    /*********************************************************/
    /*********************** FUNCTIONS ***********************/
    /*********************************************************/

    /**
     * Performs no synchronization.
     *
     * @param file ignored
     */
    @Override
    public void synchronize(CustomFile file) {/* Intentionally left blank. */}
}