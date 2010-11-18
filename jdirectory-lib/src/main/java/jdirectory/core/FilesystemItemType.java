package jdirectory.core;

import java.util.Arrays;

/**
 * Represents a set of filesystem item types.
 *
 * @author Alexander Yurinsky
 */
public enum FilesystemItemType {
    FILE(null, "file-symbol"),
    PICTURE(new String[] {".jpg", ".png", ".gif"}, "picture-symbol"),
    DIRECTORY(null, "dir-symbol"),
    ARCHIVE(new String[] {".zip", ".rar"}, "archive-symbol"),
    JAR(new String[] {".jar"}, "jar-symbol");

    private String[] extensions;
    private String cssClass;

    private FilesystemItemType(String[] extensions, String cssClass) {
        if (extensions != null) {
            Arrays.sort(extensions);
        }
        this.extensions = extensions;
        this.cssClass = cssClass;
    }

    /**
     * Determines the type of the specified file by its extension.
     *
     * @param fileName The name of the file.
     * @return The type of the file according to its extension, default is FILE.
     */
    public static FilesystemItemType byName(String fileName) {
        int pointIdx = fileName.lastIndexOf('.');
        if (pointIdx >= 0 && pointIdx + 1 < fileName.length()) {
            String extension = fileName.substring(pointIdx).toLowerCase();
            for (FilesystemItemType value : values()) {
                if (value.extensions != null && Arrays.binarySearch(value.extensions, extension) >= 0) {
                    return value;
                }
            }
        }
        return FilesystemItemType.FILE;
    }

    public String getCssClass() {
        return cssClass;
    }
}
