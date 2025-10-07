package com.cafepos;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.cafepos.catalog.Product;
import com.cafepos.catalog.SimpleProduct;
import com.cafepos.common.Money;
import com.cafepos.decorator.ExtraShot;
import com.cafepos.decorator.OatMilk;
import com.cafepos.decorator.Priced;
import com.cafepos.decorator.SizeLarge;
import com.cafepos.factory.ProductFactory;
import com.cafepos.domain.Order;

public class WK5ActivityTest {

    @Test
    void Drink_Factory_vs_Manual() {
        // Create using factory
        ProductFactory factory = new ProductFactory();
        Product f = factory.create("ESP+SHOT+OAT+L");

        // Create manually
        Product m = new SizeLarge(new OatMilk(new ExtraShot(new SimpleProduct("P-ESP","Espresso", Money.of(2.50)))));

        assertEquals(m.name(), f.name());

        Priced pricedF = (Priced) f;
        Priced pricedM = (Priced) m;
        assertEquals(pricedM.price(), pricedF.price());

        Order orderF = new Order(1);
        Order orderM = new Order(2);

        orderF.addItem(new com.cafepos.domain.LineItem(f, 1));
        orderM.addItem(new com.cafepos.domain.LineItem(m, 1));

        assertEquals(orderF.subtotal(), orderM.subtotal());

        assertEquals(orderF.totalWithTax(10), orderM.totalWithTax(10));

    }
}
