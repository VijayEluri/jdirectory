package jdirectory.core;

import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * A directory scanner for contents of ZIP archives.
 *
 * @author Alexander Yurinsky
 */
public class ZipArchiveScanner extends AbstractDirectoryScanner {
    private static final Logger LOGGER = Logger.getLogger(ZipArchiveScanner.class);

    private File archiveFile;
    /**
     * Creates an instance of directory scanner.
     *
     * @param rootDirectoryPath The path to the root filesystem directory.
     * @param archivePath The path to archive relative to the root directory.
     * @param localPath The local path relative to the root of archive contents.
     * @throws UnsupportedScanTargetException If target filesystem item is not supported.
     */
    public ZipArchiveScanner(String rootDirectoryPath, String archivePath,
                             String localPath) throws UnsupportedScanTargetException {
        super(rootDirectoryPath, localPath);
        if (archivePath.toLowerCase().endsWith(RAR_FILE_POSTFIX) || localPath.indexOf('!') >= 0) {
            throw new UnsupportedScanTargetException();
        }
        File archiveFile = new File(rootDirectory, archivePath);
        if (!archiveFile.exists() || !archiveFile.isFile()) {
            throw new IllegalArgumentException("Provided path to archive does not really " +
                    "point to a ZIP archive: " + archiveFile.getAbsolutePath());
        }
        this.archiveFile = archiveFile;
        if (this.localPath.length() > 0) {
            char lastChar = this.localPath.charAt(this.localPath.length() - 1);
            int endIdx = (lastChar == '/' || lastChar == '\\') ? this.localPath.length() - 1
                    : this.localPath.length();
            this.localPath = this.localPath.substring(1, endIdx);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FilesystemItem[] scan() throws DirectoryScanException {
        Set<FilesystemItem> result;
        ZipFile archive = null;
        try {
            archive = new ZipFile(archiveFile, ZipFile.OPEN_READ);
            result = new TreeSet<FilesystemItem>();
            Enumeration<? extends ZipEntry> entries = archive.entries();
            for (; entries.hasMoreElements();) {
                String entryName = entries.nextElement().getName();
                boolean isDirectory = isDirectory(entryName);
                if (isDirectory) {
                    entryName = entryName.substring(0, entryName.length() - 1);
                }
                if (entryName.startsWith(localPath) && !entryName.equals(localPath)
                        && entryName.indexOf('/', localPath.length() + 1) < 0) {
                    String name = localPath.length() > 0 ? entryName.substring(localPath.length() + 1,
                            entryName.length()) : entryName;
                    FilesystemItemType type = isDirectory ? FilesystemItemType.DIRECTORY
                        : FilesystemItemType.byName(name);
                    result.add(new FilesystemItem(name, type));
                }
            }
            return result.toArray(new FilesystemItem[result.size()]);
        } catch (IOException e) {
            throw new DirectoryScanException(
                    MessageFormat.format("An error encountered while processing ZIP archive {0}",
                        archiveFile.getAbsolutePath()), e);
        } finally {
            if (archive != null) {
                try {
                    archive.close();
                } catch (IOException e) {
                    LOGGER.error(MessageFormat.format("An attempt to close ZIP archive {0} failed",
                                archiveFile.getAbsolutePath()), e);
                }
            }
        }
    }

    private boolean isDirectory(String entryName) {
        return entryName.charAt(entryName.length() - 1) == '/';
    }
}
