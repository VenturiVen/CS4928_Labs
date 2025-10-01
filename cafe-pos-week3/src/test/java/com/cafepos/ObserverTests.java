package com.cafepos;

import com.cafepos.domain.OrderObserver;
import com.cafepos.catalog.SimpleProduct;
import com.cafepos.common.Money;
import com.cafepos.domain.LineItem;
import com.cafepos.domain.Order;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

public class ObserverTests {

    @Test
    void observers_notified_on_item_add() {
        var p = new SimpleProduct("A", "A", Money.of(2));
        var o = new Order(1);
        o.addItem(new LineItem(p, 1)); // baseline
        List<String> events = new ArrayList<>();
        o.register((order, evt) -> events.add(evt));
        o.addItem(new LineItem(p, 1));
        assertTrue(events.contains("itemAdded"));
    }

    @Test
    void multiple_observers_ready() {
        var o = new Order(1);

        List<String> events1 = new ArrayList<>();
        List<String> events2 = new ArrayList<>();

        OrderObserver observer1 = (order, event) -> events1.add(event);
        OrderObserver observer2 = (order, event) -> events2.add(event);

        o.register(observer1);
        o.register(observer2);

        o.markReady();

        assertTrue(events1.contains("ready"));
        assertTrue(events2.contains("ready"));
    }

}
