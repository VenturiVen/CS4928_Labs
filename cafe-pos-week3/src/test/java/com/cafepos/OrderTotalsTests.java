package com.cafepos;

import com.cafepos.catalog.*;
import com.cafepos.common.*;
import com.cafepos.domain.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class OrderTotalsTests {

    @Test
    void order_totals() {
        var p1 = new SimpleProduct("A", "A", Money.of(2.50));
        var p2 = new SimpleProduct("B", "B", Money.of(3.50));
        var o = new Order(1);
        o.addItem(new LineItem(p1, 2));
        o.addItem(new LineItem(p2, 1));
        assertEquals(Money.of(8.50), o.subtotal());
        assertEquals(Money.of(0.85), o.taxAtPercent(10));
        assertEquals(Money.of(9.35), o.totalWithTax(10));
    }

    @Test
    void total_with_tax_for_single_item() {
        var p = new SimpleProduct("C", "C", Money.of(10.00));
        var o = new Order(2);
        o.addItem(new LineItem(p, 1));
        assertEquals(Money.of(10.00), o.subtotal(), "Subtotal should match item price");
        assertEquals(Money.of(1.00), o.taxAtPercent(10), "Tax at 10% should be 1.00");
        assertEquals(Money.of(11.00), o.totalWithTax(10), "Total with 10% tax should be 11.00");
    }

    @Test
    void total_with_tax_for_empty_order() {
        var o = new Order(3);
        assertEquals(Money.of(0.00), o.subtotal(), "Empty order subtotal should be 0.00");
        assertEquals(Money.of(0.00), o.taxAtPercent(10), "Tax for empty order should be 0.00");
        assertEquals(Money.of(0.00), o.totalWithTax(10), "Total with tax should be 0.00");
    }

    @Test
    void total_with_zero_tax() {
        var p1 = new SimpleProduct("D", "D", Money.of(5.00));
        var p2 = new SimpleProduct("E", "E", Money.of(7.00));
        var o = new Order(4);
        o.addItem(new LineItem(p1, 1));
        o.addItem(new LineItem(p2, 2));
        assertEquals(Money.of(19.00), o.subtotal(), "Subtotal should be 19.00");
        assertEquals(Money.of(0.00), o.taxAtPercent(0), "Tax at 0% should be 0.00");
        assertEquals(Money.of(19.00), o.totalWithTax(0), "Total with 0% tax should equal subtotal");
        
    }
}
