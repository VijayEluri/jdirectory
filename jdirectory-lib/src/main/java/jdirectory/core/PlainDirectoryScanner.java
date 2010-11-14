package jdirectory.core;

import java.io.File;

/**
 * A directory scanner for plain directories.
 * 
 * @author Alexander Yurinsky
 */
public class PlainDirectoryScanner extends AbstractDirectoryScanner {
    private File currentDirectory;

    /**
     * Creates an instance of directory scanner.
     *
     * @param rootDirectoryPath The path to the root filesystem directory.
     * @param localPath The local path relative to the root filesystem directory.
     */
    public PlainDirectoryScanner(String rootDirectoryPath, String localPath) {
        super(rootDirectoryPath, localPath);
        File currentDirectory = new File(rootDirectory, localPath);
        if (!currentDirectory.exists() || !currentDirectory.isDirectory()) {
            throw new IllegalArgumentException("Provided local path does not point to the filesystem directory");
        }
        this.currentDirectory = currentDirectory;
        if (this.localPath.length() == 0) {
            this.localPath = "/" + this.localPath;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FilesystemItem[] scan() throws DirectoryScanException {
        File[] listFiles = currentDirectory.listFiles();
        FilesystemItem[] items = new FilesystemItem[listFiles.length];
        int i = 0;
        for (File file : listFiles) {
            FilesystemItemType type = file.isDirectory() ? FilesystemItemType.DIRECTORY
                    : getFileTypeByName(file.getName());
            items[i++] = new FilesystemItem(file.getName(), type);
        }
        return items;
    }
}
