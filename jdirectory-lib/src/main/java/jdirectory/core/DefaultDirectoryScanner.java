package jdirectory.core;

/**
 * Default directory scanner, works as a stub.
 * 
 * @author Alexander Yurinsky
 */
public class DefaultDirectoryScanner extends AbstractDirectoryScanner {
    /**
     * Creates an instance of directory scanner.
     *
     * @param rootDirectoryPath The path to the root filesystem directory.
     * @param localPath The local path relative to the root filesystem directory.
     */
    public DefaultDirectoryScanner(String rootDirectoryPath, String localPath) {
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
