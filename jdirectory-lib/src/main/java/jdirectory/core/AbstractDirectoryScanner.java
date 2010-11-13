package jdirectory.core;

import java.io.File;

/**
 * An abstract directory scanner.
 * Encapsulates a set of basic parameters.
 *
 * @author Alexander Yurinsky
 */
public abstract class AbstractDirectoryScanner implements DirectoryScanner {
    private static final String ZIP_FILE_POSTFIX = ".zip";
    /**
     * Path to the root directory.
     */
    protected File rootDirectory;
    /**
     * Local path to the current directory.
     */
    protected String localPath;

    /**
     * Creates an instance of directory scanner.
     *
     * @param rootDirectoryPath The path to the root filesystem directory.
     * @param localPath The local path relative to the root filesystem directory.
     */
    public AbstractDirectoryScanner(String rootDirectoryPath, String localPath) {
        File rootDirectory = new File(rootDirectoryPath);
        if (!rootDirectory.exists()) {
            throw new IllegalArgumentException("Provided directory does not exist");
        }
        this.rootDirectory = rootDirectory;
        this.localPath = localPath;
    }

    /**
     * Determines whether the file is ZIP archive or not.
     *
     * @param fileName The name of the file.
     * @return <code>true</code> If the file is ZIP archive.
     */
    protected boolean isZipArchive(String fileName) {
        return fileName.toLowerCase().endsWith(ZIP_FILE_POSTFIX);
    }
}
