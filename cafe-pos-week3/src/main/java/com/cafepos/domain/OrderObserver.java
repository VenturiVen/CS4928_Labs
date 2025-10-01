package com.cafepos.domain;

@FunctionalInterface
public interface OrderObserver {
    void updated(Order order, String eventType);
}
