import org.example.filesystem.FileSystem;
import org.example.filesystem.exception.DirectoryNotFoundException;
import org.example.filesystem.exception.FileNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FileSystemTest {

    FileSystem fileSystem;

    @BeforeEach
    void setUp() {
        fileSystem = new FileSystem();
    }

    // ---------------- CREATE DIRECTORY ----------------

    @Test
    void createDirectory() {
        assertTrue(fileSystem.createDirectory("/documents"));
        assertTrue(fileSystem.createDirectory("/documents/work"));
    }

    @Test
    void createDirectoryReturnsFalseWhenDirectoryAlreadyExists() {
        fileSystem.createDirectory("/documents");

        assertFalse(fileSystem.createDirectory("/documents"));
    }

    @Test
    void createDirectoryThrowsExceptionWhenParentDoesNotExist() {
        assertThrows(
                DirectoryNotFoundException.class,
                () -> fileSystem.createDirectory("/documents/work")
        );
    }

    @Test
    void createDirectoryReturnsFalseWhenFileWithSameNameExists() {
        fileSystem.createFile("/documents", "Hello");

        assertFalse(fileSystem.createDirectory("/documents"));
    }

    @Test
    void createRootDirectoryReturnsFalse() {
        assertFalse(fileSystem.createDirectory("/"));
    }


    // ---------------- CREATE FILE ----------------

    @Test
    void createFile() {
        fileSystem.createDirectory("/documents");

        assertTrue(fileSystem.createFile("/documents/report.txt", "Hello"));
    }

    @Test
    void createFileWithEmptyContent() {
        fileSystem.createDirectory("/documents");

        assertTrue(fileSystem.createFile("/documents/empty.txt", ""));

        assertEquals("", fileSystem.readFile("/documents/empty.txt"));
    }

    @Test
    void createFileWithNullContentReturnsFalse() {
        fileSystem.createDirectory("/documents");

        assertFalse(fileSystem.createFile("/documents/report.txt", null));
    }

    @Test
    void createFileReturnsFalseWhenFileAlreadyExists() {
        fileSystem.createDirectory("/documents");
        fileSystem.createFile("/documents/report.txt", "A");

        assertFalse(fileSystem.createFile("/documents/report.txt", "B"));

        assertEquals("A", fileSystem.readFile("/documents/report.txt"));
    }

    @Test
    void createFileReturnsFalseWhenDirectoryWithSameNameExists() {
        fileSystem.createDirectory("/documents");
        fileSystem.createDirectory("/documents/report.txt");

        assertFalse(fileSystem.createFile("/documents/report.txt", "Hello"));
    }

    @Test
    void createFileThrowsExceptionWhenParentDoesNotExist() {
        assertThrows(DirectoryNotFoundException.class, () -> fileSystem.createFile("/documents/report.txt", "Hello"));
    }

    @Test
    void createFileInRoot() {
        assertTrue(fileSystem.createFile("/report.txt", "Hello"));

        assertEquals("Hello", fileSystem.readFile("/report.txt"));
    }

    @Test
    void createFileForRootPathReturnsFalse() {
        assertFalse(fileSystem.createFile("/", "Hello"));
    }


    // ---------------- READ FILE ----------------

    @Test
    void readExistingFile() {
        fileSystem.createDirectory("/documents");
        fileSystem.createFile("/documents/report.txt", "Hello");

        String result = fileSystem.readFile("/documents/report.txt");

        assertEquals("Hello", result);
    }

    @Test
    void readFileThrowsExceptionWhenFileDoesNotExist() {
        fileSystem.createDirectory("/documents");

        assertThrows(FileNotFoundException.class, () -> fileSystem.readFile("/documents/report.txt"));
    }

    @Test
    void readFileThrowsExceptionWhenDirectoryDoesNotExist() {
        assertThrows(DirectoryNotFoundException.class, () -> fileSystem.readFile("/documents/report.txt"));
    }

    @Test
    void readRootAsFileThrowsException() {
        assertThrows(FileNotFoundException.class, () -> fileSystem.readFile("/"));
    }


    // ---------------- DELETE ----------------

    @Test
    void deleteFile() {
        fileSystem.createDirectory("/documents");
        fileSystem.createFile("/documents/report.txt", "Hello");

        assertTrue(fileSystem.delete("/documents/report.txt"));

        assertThrows(FileNotFoundException.class, () -> fileSystem.readFile("/documents/report.txt"));
    }

    @Test
    void deleteEmptyDirectory() {
        fileSystem.createDirectory("/documents");
        fileSystem.createDirectory("/documents/work");

        assertTrue(fileSystem.delete("/documents/work"));

        List<String> result = fileSystem.list("/documents");

        assertFalse(result.contains("work"));
    }

    @Test
    void deleteDirectoryContainingFileReturnsFalse() {
        fileSystem.createDirectory("/documents");
        fileSystem.createDirectory("/documents/work");
        fileSystem.createFile("/documents/work/report.txt", "Hello");

        assertFalse(fileSystem.delete("/documents/work"));
    }

    @Test
    void deleteDirectoryContainingDirectoryReturnsFalse() {
        fileSystem.createDirectory("/documents");
        fileSystem.createDirectory("/documents/work");
        fileSystem.createDirectory("/documents/work/backend");

        assertFalse(fileSystem.delete("/documents/work"));
    }

    @Test
    void deleteUnknownObjectReturnsFalse() {
        fileSystem.createDirectory("/documents");

        assertFalse(fileSystem.delete("/documents/unknown"));
    }

    @Test
    void deleteThrowsExceptionWhenParentDirectoryDoesNotExist() {
        assertThrows(DirectoryNotFoundException.class, () -> fileSystem.delete("/documents/report.txt"));
    }

    @Test
    void deleteRootReturnsFalse() {
        assertFalse(fileSystem.delete("/"));
    }


    // ---------------- LIST ----------------

    @Test
    void listDirectoryContainsFilesAndDirectories() {
        fileSystem.createDirectory("/documents");
        fileSystem.createDirectory("/documents/work");
        fileSystem.createDirectory("/documents/photos");

        fileSystem.createFile("/documents/report.txt", "Hello");

        List<String> result = fileSystem.list("/documents");

        assertEquals(3, result.size());
        assertTrue(result.contains("work"));
        assertTrue(result.contains("photos"));
        assertTrue(result.contains("report.txt"));
    }

    @Test
    void listEmptyDirectoryReturnsEmptyList() {
        fileSystem.createDirectory("/documents");

        List<String> result = fileSystem.list("/documents");

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void listRootDirectory() {
        fileSystem.createDirectory("/documents");
        fileSystem.createDirectory("/photos");
        fileSystem.createFile("/README", "Hello");

        List<String> result = fileSystem.list("/");

        assertEquals(3, result.size());
        assertTrue(result.contains("documents"));
        assertTrue(result.contains("photos"));
        assertTrue(result.contains("README"));
    }

    @Test
    void listThrowsExceptionWhenDirectoryDoesNotExist() {
        assertThrows(DirectoryNotFoundException.class, () -> fileSystem.list("/unknown"));
    }
}