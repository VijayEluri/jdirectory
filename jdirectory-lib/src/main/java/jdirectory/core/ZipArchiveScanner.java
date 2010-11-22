package jdirectory.core;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.Set;
import java.util.TreeSet;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

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
     * @throws UnsupportedScanTargetException If target filesystem item is not supported.
     */
    public ZipArchiveScanner(String rootDirectoryPath, String archivePath,
                             String localPath) throws UnsupportedScanTargetException {
        super(rootDirectoryPath, localPath);
        if (archivePath.toLowerCase().endsWith(RAR_FILE_POSTFIX)) {
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
        try {
            return scanArchive(new BufferedInputStream(new FileInputStream(archiveFile)), localPath);
        } catch (Exception e) {
            throw new DirectoryScanException(
                    MessageFormat.format("An error encountered while processing ZIP archive {0}",
                        archiveFile.getAbsolutePath()), e);
        }
    }

    /*
     * Scans a particular instance of archive represented by its input stream.
     * All streams supposed to be closed inside this method.
     */
    private FilesystemItem[] scanArchive(InputStream input, String currentPath) throws Exception {
        ZipInputStream zipStream = null;
        try {
            zipStream = new ZipInputStream(input);
            int subArchiveIdx = currentPath.indexOf('!');
            ZipEntry entry;
            if (subArchiveIdx >= 0) {
                String archiveName = currentPath.substring(0, subArchiveIdx);
                currentPath = currentPath.length() > subArchiveIdx + 1
                        ? currentPath.substring(subArchiveIdx + 2, currentPath.length()) : "";
                while ((entry = zipStream.getNextEntry()) != null) {
                    if (entry.getName().equals(archiveName)) {
                        return scanArchive(zipStream, currentPath);
                    } else {
                        zipStream.closeEntry();
                    }
                }
            } else {
                Set<FilesystemItem> result = new TreeSet<FilesystemItem>();
                while ((entry = zipStream.getNextEntry()) != null) {
                    String entryName = entry.getName();
                    boolean isDirectory = entry.isDirectory();
                    if (isDirectory) {
                        entryName = entryName.substring(0, entryName.length() - 1);
                    }
                    if (entryName.startsWith(currentPath) && entryName.length() != currentPath.length()) {
                        int startIdx = currentPath.length() > 0 ? currentPath.length() + 1 : currentPath.length();
                        int endIdx = entryName.indexOf('/', currentPath.length() + 1);
                        if (endIdx >= 0) {
                            isDirectory = true;
                        } else {
                            endIdx = entryName.length();
                        }
                        String name = entryName.substring(startIdx, endIdx);
                        FilesystemItemType type = isDirectory ? FilesystemItemType.DIRECTORY
                                : FilesystemItemType.byName(name);
                        result.add(new FilesystemItem(name, type));
                    }
                }
                return result.toArray(new FilesystemItem[result.size()]);
            }
        } finally {
            if (zipStream != null) {
                zipStream.close();
            }
        }
        return new FilesystemItem[0];
    }
}
