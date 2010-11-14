package jdirectory.core;

import java.io.File;
import java.util.Arrays;

/**
 * An abstract directory scanner.
 * Encapsulates a set of basic parameters.
 *
 * @author Alexander Yurinsky
 */
public abstract class AbstractDirectoryScanner implements DirectoryScanner {
    private static final String ZIP_FILE_POSTFIX = ".zip";
    private static final String[] PICTURE_POSTFIXES ={".jpg", ".png", ".gif"};

    static {
        Arrays.sort(PICTURE_POSTFIXES);
    }
    
    /**
     * RAR file extension.
     */
    protected static final String RAR_FILE_POSTFIX = ".rar";
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
     * Determines the type of the specified file by its extension.
     *
     * @param fileName The name of the file.
     * @return The type of the file according to its extension, default is FILE.
     */
    protected FilesystemItemType getFileTypeByName(String fileName) {
        int pointIdx = fileName.lastIndexOf('.');
        if (pointIdx >= 0 && pointIdx + 1 < fileName.length()) {
            String extension = fileName.substring(pointIdx).toLowerCase();
            if (extension.equals(ZIP_FILE_POSTFIX) || extension.equals(RAR_FILE_POSTFIX)) {
                return FilesystemItemType.ARCHIVE;
            }
            if (Arrays.binarySearch(PICTURE_POSTFIXES, extension) >= 0) {
                return FilesystemItemType.PICTURE;
            }
        }
        return FilesystemItemType.FILE;
    }
}
