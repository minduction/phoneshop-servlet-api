package com.es.phoneshop.security;

import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultDosProtectionService implements DosProtectionService {
    private static final int THRESHOLD = 20;
    private static final long TIME_LIMIT = 60000;

    private final Map<String, Queue<Long>> countMap = new ConcurrentHashMap<String, Queue<Long>>();

    private static final class DefaultProtectionServiceHolder {
        private static final DefaultDosProtectionService instance = new DefaultDosProtectionService();
    }

    public static DefaultDosProtectionService getInstance() {
        return DefaultDosProtectionService.DefaultProtectionServiceHolder.instance;
    }

    @Override
    public boolean isAllowed(String ip){
        long currentTime = System.currentTimeMillis();
        Queue<Long> timestamps = countMap.computeIfAbsent(ip, key -> new LinkedList<>());

        synchronized (timestamps) {
            timestamps.removeIf(timestamp -> currentTime - timestamp > TIME_LIMIT);
            if (timestamps.size() >= THRESHOLD) {
                return false;
            }
            timestamps.add(currentTime);
            return true;
        }
    }
}
