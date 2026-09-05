package org.example.database;

import java.util.*;

public class Database {
    private final Map<String, String> data;
    private final Map<String, Integer> valueCount;
    private final Deque<Map<String, String>> transactions;

    public Database() {
        this.data = new HashMap<>();
        this.valueCount = new HashMap<>();
        this.transactions = new ArrayDeque<>();
    }

    public void set(String key, String value) {
        String oldValue = this.data.get(key);
        if (Objects.equals(value, oldValue)) return;

        addToTransactionIfNeeded(key, oldValue);
        decrementValueCountIfNeeded(oldValue);
        valueCount.put(value, valueCount.getOrDefault(value, 0) + 1);
        data.put(key, value);
    }

    public String get(String key) {
        return this.data.get(key);
    }

    public void delete(String key) {
        String value = this.data.get(key);
        if (value == null) return;

        addToTransactionIfNeeded(key, value);

        decrementValueCountIfNeeded(value);
        this.data.remove(key);

    }

    public int getCount(String value) {
        return this.valueCount.getOrDefault(value, 0);
    }

    private void decrementValueCountIfNeeded(String value) {
        if (value == null) return;

        if (valueCount.get(value) > 1) {
            valueCount.put(value, valueCount.get(value) - 1);
        } else {
            valueCount.remove(value);
        }
    }

    public void begin() {
        this.transactions.push(new HashMap<>());
    }

    private void addToTransactionIfNeeded(String key, String oldValue) {
        if (!transactions.isEmpty()) {
            Map<String, String> transaction = transactions.peek();

            if (!transaction.containsKey(key)) {
                transaction.put(key, oldValue);
            }
        }
    }

    public boolean rollback() {
        if (transactions.isEmpty()) return false;

        Map<String, String> transaction = transactions.pop();
        for (String key : transaction.keySet()) {
            String oldValue = transaction.get(key);
            String currentValue = data.get(key);

            decrementValueCountIfNeeded(currentValue);

            if (oldValue != null) {
                data.put(key, oldValue);
                valueCount.put(oldValue, valueCount.getOrDefault(oldValue, 0) + 1);
            } else {
                data.remove(key);
            }
        }
        return true;
    }
    public boolean commit() {
        if (transactions.isEmpty()) return false;

        transactions.clear();
        return true;
    }
}
