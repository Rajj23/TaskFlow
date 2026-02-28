package com.taskflow.Custom;

import com.taskflow.Entity.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TaskTrie {

    static class TrieNode {
        Map<Character, TrieNode> children = new HashMap<>();
        List<Task> tasks = new ArrayList<>();
        boolean isEndOfWord = false;
    }

    private TrieNode root = new TrieNode();

    public void insert(String word, Task task) {
        if (word == null || word.trim().isEmpty())
            return;

        TrieNode node = root;
        for (char c : word.toLowerCase().toCharArray()) {
            if (Character.isLetterOrDigit(c)) {
                node = node.children.computeIfAbsent(c, k -> new TrieNode());
                node.tasks.add(task);
            }
        }
        node.isEndOfWord = true;
    }

    public void insertTask(Task task) {
        if (task.getTitle() != null) {
            insert(task.getTitle(), task);
            String[] titleWords = task.getTitle().toLowerCase().split("\\s+");
            for (String word : titleWords) {
                if (word.length() > 0) {
                    insert(word, task);
                }
            }
        }

        if (task.getDescription() != null) {
            insert(task.getDescription(), task);
            String[] descWords = task.getDescription().toLowerCase().split("\\s+");
            for (String word : descWords) {
                if (word.length() > 0) {
                    insert(word, task);
                }
            }
        }
    }

    public List<Task> searchByPrefix(String prefix) {
        if (prefix == null || prefix.trim().isEmpty())
            return new ArrayList<>();

        TrieNode node = root;
        String cleanPrefix = prefix.toLowerCase().replaceAll("[^a-zA-Z0-9]", "");

        for (char c : cleanPrefix.toCharArray()) {
            node = node.children.get(c);
            if (node == null)
                return new ArrayList<>();
        }

        return node.tasks.stream().distinct().collect(Collectors.toList());
    }

    public void clear() {
        root = new TrieNode();
    }

    public boolean isEmpty() {
        return root.children.isEmpty();
    }
}
