package jdirectory.core;

import java.io.File;

/**
 * A directory scanner for contents of ZIP archives.
 *
 * @author Alexander Yurinsky
 */
public class ZipArchiveScanner extends AbstractDirectoryScanner {
    private File archiveFile;
    /**
     * Creates an instance of directory scanner.
     *
     * @param rootDirectoryPath The path to the root filesystem directory.
     * @param archivePath The path to archive relative to the root directory.
     * @param localPath The local path relative to the root of archive contents.
     */
    public ZipArchiveScanner(String rootDirectoryPath, String archivePath, String localPath) {
        super(rootDirectoryPath, localPath);
        File archiveFile = new File(archivePath);
        if (!archiveFile.exists() || !archiveFile.isFile()) {
            throw new IllegalArgumentException("Provided path to the archive does not" +
                    "points to the real ZIP archive");
        }
        this.archiveFile = archiveFile;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String[] scan() {
        return new String[0];
    }
}
