package org.example.filesystem;

import org.example.filesystem.exception.DirectoryNotFoundException;
import org.example.filesystem.exception.FileNotFoundException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileSystem {
    private final DirectoryNode root;

    private static class DirectoryNode {
        Map<String, DirectoryNode> directories;
        Map<String, String> files;

        DirectoryNode() {
            this.directories = new HashMap<>();
            this.files = new HashMap<>();
        }
    }

    public FileSystem() {
        this.root = new DirectoryNode();
    }

    public boolean createDirectory(String path) {
        if (path.equals("/")) return false;

        String[] parts = path.substring(1).split("/");
        DirectoryNode current = root;

        for (int i = 0; i < parts.length; i++) {
            String name = parts[i];
            boolean isLast = i == parts.length - 1;
            if (current.files.containsKey(name)) return false;

            if (current.directories.containsKey(name)) {
                if (isLast) return false;

                current = current.directories.get(name);
            } else {
                if (!isLast) throw new DirectoryNotFoundException("Directory does not exist: " + name);

                current.directories.put(name, new DirectoryNode());
            }
        }

        return true;
    }

    public boolean createFile(String path, String content) {
        if (path.equals("/")) return false;
        if (content == null) return false;

        String[] parts = path.substring(1).split("/");
        String fileName = parts[parts.length - 1];

        DirectoryNode current = findDirectory(parts, parts.length - 1);

        if (current.files.containsKey(fileName)) return false;
        if (current.directories.containsKey(fileName)) return false;

        current.files.put(fileName, content);
        return true;
    }

    public String readFile(String path) {
        if (path.equals("/")) throw new FileNotFoundException("Path is not a file: /");

        String[] parts = path.substring(1).split("/");
        String fileName = parts[parts.length - 1];

        DirectoryNode current = findDirectory(parts, parts.length - 1);

        if (!current.files.containsKey(fileName))
            throw new FileNotFoundException("File does not exist: " + path);

        return current.files.get(fileName);
    }

    public boolean delete(String path) {
        if (path.equals("/")) return false;

        String[] parts = path.substring(1).split("/");
        String name = parts[parts.length - 1];

        DirectoryNode current = findDirectory(parts, parts.length - 1);

        if (current.files.containsKey(name)) {
            current.files.remove(name);
            return true;
        }

        DirectoryNode directory = current.directories.get(name);

        if (directory != null) {
            if (!directory.directories.isEmpty() || !directory.files.isEmpty()) return false;

            current.directories.remove(name);
            return true;
        }

        return false;
    }

    public List<String> list(String path) {
        DirectoryNode currentRoot;

        if (path.equals("/")) {
            currentRoot = root;
        } else {
            String[] parts = path.substring(1).split("/");
            currentRoot = findDirectory(parts, parts.length);
        }

        List<String> list = new ArrayList<>();
        list.addAll(currentRoot.files.keySet());
        list.addAll(currentRoot.directories.keySet());

        return list;
    }

    private DirectoryNode findDirectory(String[] parts, int endExclusive) {
        DirectoryNode current = root;

        for (int i = 0; i < endExclusive; i++) {
            if (!current.directories.containsKey(parts[i]))
                throw new DirectoryNotFoundException("Directory does not exist: " + parts[i]);

            current = current.directories.get(parts[i]);
        }

        return current;
    }
}
