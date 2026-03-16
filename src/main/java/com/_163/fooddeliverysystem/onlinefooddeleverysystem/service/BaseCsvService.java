package com._163.fooddeliverysystem.onlinefooddeleverysystem.service;

import com._163.fooddeliverysystem.onlinefooddeleverysystem.util.FileManager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public abstract class BaseCsvService<T> {
    protected abstract String getFileName();
    protected abstract T fromDataString(String line);
    protected abstract String toDataString(T item);
    protected abstract String getId(T item);

    public List<T> getAll() {
        List<T> items = new ArrayList<>();
        for (String line : FileManager.readLines(getFileName())) {
            T item = fromDataString(line);
            if (item != null) {
                items.add(item);
            }
        }
        return items;
    }

    public T getById(String id) {
        if (id == null) return null;
        for (T item : getAll()) {
            if (Objects.equals(getId(item), id)) {
                return item;
            }
        }
        return null;
    }

    public boolean add(T item) {
        if (item == null || getId(item) == null) {
            return false;
        }
        if (getById(getId(item)) != null) {
            return false;
        }

        FileManager.appendLine(getFileName(), toDataString(item));
        return true;
    }

    public boolean update(T updatedItem) {
        if (updatedItem == null || getId(updatedItem) == null) {
            return false;
        }

        List<T> items = getAll();
        boolean found = false;
        for (int i = 0; i < items.size(); i++) {
            if (Objects.equals(getId(items.get(i)), getId(updatedItem))) {
                items.set(i, updatedItem);
                found = true;
                break;
            }
        }
        if (!found) {
            return false;
        }

        writeAll(items);
        return true;
    }

    public boolean delete(String id) {
        if (id == null) {
            return false;
        }

        List<T> items = getAll();
        Iterator<T> iterator = items.iterator();
        boolean removed = false;
        while (iterator.hasNext()) {
            if (Objects.equals(getId(iterator.next()), id)) {
                iterator.remove();
                removed = true;
                break;
            }
        }

        if (!removed) {
            return false;
        }

        writeAll(items);
        return true;
    }

    protected void writeAll(List<T> items) {
        List<String> lines = items.stream()
                .map(this::toDataString)
                .collect(Collectors.toList());
        FileManager.writeLines(getFileName(), lines);
    }
}
