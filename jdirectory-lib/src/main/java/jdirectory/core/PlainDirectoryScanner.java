package jdirectory.core;

/**
 * A directory scanner for plain directories.
 * 
 * @author Alexander Yurinsky
 */
public class PlainDirectoryScanner extends AbstractDirectoryScanner {
    /**
     * Creates an instance of directory scanner.
     *
     * @param rootDirectoryPath The path to the root filesystem directory.
     * @param localPath The local path relative to the root filesystem directory.
     */
    public PlainDirectoryScanner(String rootDirectoryPath, String localPath) {
        super(rootDirectoryPath, localPath);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String[] scan() {
        return new String[0];
    }
}
