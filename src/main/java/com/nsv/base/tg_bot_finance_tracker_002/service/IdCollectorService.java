package com.nsv.base.tg_bot_finance_tracker_002.service;


import org.springframework.stereotype.Service;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class IdCollectorService {

    private final Map<Object, List<String>> storage = new ConcurrentHashMap<>();

    public void addId(Object key, String id) {
        storage.computeIfAbsent(key, k -> new ArrayList<>()).add(id);
    }

    public List<String> claimIds(Object key) {
        List<String> ids = storage.remove(key);
        return ids != null ? ids : Collections.emptyList();
    }

    public boolean hasIds(Object key) {
        return storage.containsKey(key) && !storage.get(key).isEmpty();
    }

    public void clear() {
        storage.clear();
    }
}