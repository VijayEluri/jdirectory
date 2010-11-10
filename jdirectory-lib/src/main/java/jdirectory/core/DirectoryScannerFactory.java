package jdirectory.core;

/**
 * Represents a factory to create instances of {@link DirectoryScanner}
 * @author Alexander Yurinsky
 */
public class DirectoryScannerFactory {
    private static final DirectoryScannerFactory INSTANCE = new DirectoryScannerFactory();

    /**
     * Gets an instance of the factory.
     *
     * @return An instance of the factory.
     */
    public static DirectoryScannerFactory getInstance() {
        return INSTANCE;
    }
    private DirectoryScannerFactory() {}

    /**
     * Gets a particular instance of scanner.
     *
     * @param rootDirectoryPath The path to the root filesystem directory.
     * @param localPath The local path relative to the root filesystem directory.
     * @return An instance of {@link DirectoryScanner}.
     */
    public DirectoryScanner getScanner(String rootDirectoryPath, String localPath) {
        if (localPath.length() > 0) {
            int wowIdx =  localPath.indexOf('!');
            return wowIdx >= 0
                    ? new ZipArchiveScanner(rootDirectoryPath, localPath.substring(0, wowIdx),
                        localPath.substring(wowIdx + 1, localPath.length()))
                    : new PlainDirectoryScanner(rootDirectoryPath, localPath);
        }
        return new DefaultDirectoryScanner(rootDirectoryPath, localPath);
    }
}
