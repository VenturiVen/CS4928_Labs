package com.cafepos.domain;

import java.util.List;
import java.util.ArrayList;
import com.cafepos.common.Money;


public final class Order {
    private final long id;
    private final List<LineItem> items = new ArrayList<>();
    
    
    public Order(long id) { 
        this.id = id; 
    }
    
    public void addItem(LineItem li) {
        if (li.quantity() <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero");
        }
        items.add(li);
    }
    
    public Money subtotal() {
    return items.stream().map(LineItem::lineTotal).reduce(Money.zero(), Money::add);
    }

    public Money taxAtPercent(int percent) {
        Money sub = subtotal();
        return sub.multiply(percent).divide(100);
    }

    public Money totalWithTax(int percent) {
        return subtotal().add(taxAtPercent(percent));
    }

    public long id() {
        return id;
    }

    public List<LineItem> items(){
        return items;
    }

}

