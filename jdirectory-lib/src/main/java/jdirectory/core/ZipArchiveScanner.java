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
     */
    public ZipArchiveScanner(String rootDirectoryPath, String archivePath, String localPath) {
        super(rootDirectoryPath, localPath);
        File archiveFile = new File(archivePath);
        if (!archiveFile.exists() || !archiveFile.isFile()) {
            throw new IllegalArgumentException("Provided path to archive does not really " +
                    "point to a ZIP archive");
        }
        this.archiveFile = archiveFile;
        char lastChar = this.localPath.charAt(this.localPath.length() - 1);
        if (lastChar == '/' || lastChar == '\\') {
            this.localPath = this.localPath.substring(0, this.localPath.length() - 1);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String[] scan() throws DirectoryScanException {
        Set<String> result;
        ZipFile archive = null;
        try {
            archive = new ZipFile(archiveFile, ZipFile.OPEN_READ);
            result = new TreeSet<String>();
            Enumeration<? extends ZipEntry> entries = archive.entries();
            for (; entries.hasMoreElements();) {
                String entryName = entries.nextElement().getName();
                if (entryName.charAt(entryName.length() - 1) == '/') {
                    entryName = entryName.substring(0, entryName.length() - 1);
                }
                if (entryName.startsWith(localPath) && !entryName.equals(localPath)
                        && entryName.indexOf('/', localPath.length() + 1) < 0) {
                    result.add(entryName.substring(localPath.length() + 1, entryName.length()));
                }
            }
            return result.toArray(new String[result.size()]);
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
}
