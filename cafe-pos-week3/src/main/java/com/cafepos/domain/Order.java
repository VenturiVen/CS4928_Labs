package com.cafepos.domain;

import java.util.List;
import java.util.ArrayList;
import com.cafepos.common.Money;
import com.cafepos.payment.PaymentStrategy;

public final class Order {

    private final long id;
    private final List<LineItem> items = new ArrayList<>();
    private final List<OrderObserver> observers = new ArrayList<>();
    
    public Order(long id) {
        this.id = id;
    }
    
    public void addItem(LineItem li) {
        if (li.quantity() <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero");
        }
        items.add(li);
        notifyObservers("itemAdded");
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

    public void pay(PaymentStrategy strategy) {
        if (strategy == null) throw new
        IllegalArgumentException("strategy required");
        strategy.pay(this);
        notifyObservers("paid");
    }

    public void register(OrderObserver o) {
        if (o == null) throw new IllegalArgumentException("Observer cannot be null");
        if (observers.contains(o)) throw new IllegalArgumentException("Observer already registered");
        observers.add(o);
    }
    public void unregister(OrderObserver o) {
        if (!observers.contains(o)) throw new IllegalArgumentException("Observer not registered");
        observers.remove(o);
    }
    
    private void notifyObservers(String eventType) {
        for (OrderObserver o : observers) {
            o.updated(this, eventType);
        }
    }
    
    public void markReady() {
        notifyObservers("ready");
    }


}

