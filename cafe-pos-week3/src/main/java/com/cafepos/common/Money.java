package com.cafepos.common;

import java.math.BigDecimal;
import java.math.RoundingMode;

public final class Money implements Comparable<Money> {

    private final BigDecimal amount;
   

// Factory methods

    public static Money of(double value) {
        if(value<0){
            throw new IllegalArgumentException("Money amount cannot be negative");
        }   
        return new Money(BigDecimal.valueOf(value));
    }

    public static Money zero() {
        return new Money(BigDecimal.ZERO);
    }


// private constructor
    private Money(BigDecimal a) {
    if (a == null){
        throw new IllegalArgumentException("amount required");
    }
    this.amount = a.setScale(2, RoundingMode.HALF_UP);
    }

// add multiply divide
    public Money add(Money other) {
        return new Money(this.amount.add(other.amount).setScale(2, RoundingMode.HALF_UP));
    }
    
    public Money multiply(int qty) {
        return new Money(this.amount.multiply(BigDecimal.valueOf(qty)).setScale(2, RoundingMode.HALF_UP));
    }

    public Money divide(int qty) {
        return new Money(this.amount.divide(BigDecimal.valueOf(qty), 2, RoundingMode.HALF_UP));
    }

    // getter method
    public BigDecimal getAmount(){
        return this.amount;
    }


    //overrides for big decimal
    @Override
    public int compareTo(Money other){
        return this.amount.compareTo(other.amount);
    }

    @Override
    public boolean equals(Object o){
        if (this == o) return true;
        if(!(o instanceof Money)) return false;
        Money money = (Money)o;
        return amount.compareTo(money.amount) == 0;
    }

    @Override
    public int hashCode() {
    return amount.stripTrailingZeros().hashCode();
    }

    @Override
    public String toString(){
        return amount.toString();
    }
}
