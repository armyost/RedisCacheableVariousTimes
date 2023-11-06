package com.example.demo;

import java.util.*;

import lombok.Getter;

@Getter
public enum CacheType {
    
    DEMO_1H("demo1H", 60 * 60, 10000),
    DEMO_1D("demo1D", 24 * 60 * 60, 10000);

    CacheType(String cacheName, int expiredAfterWrite, int maximumSize) {
        this.cacheName = cacheName;
        this.expiredAfterWrite = expiredAfterWrite;
        this.maximumSize = maximumSize;
    }

    private String cacheName;
    private int expiredAfterWrite;
    private int maximumSize;
}
