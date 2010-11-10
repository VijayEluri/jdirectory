package jdirectory.core;

/**
 * Represents a scanner which intended
 * to collect the items of directory or archive.
 * 
 * @author Alexander Yurinsky
 */
public interface DirectoryScanner {
    /**
     * Scans current directory or archive for items.
     *
     * @return An array of directory's items.
     */
    String[] scan();
}
