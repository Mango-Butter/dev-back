package com.mangoboss.app.domain.service.cache;

import java.util.List;

public interface CacheService {
    <T> void cacheObject(String key, List<T> data, Integer expireMinutes);

    <T> List<T> getCachedObject(String key, Class<T> clazz);
}
