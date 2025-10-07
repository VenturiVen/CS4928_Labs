package com.cafepos.decorator;

import com.cafepos.common.Money;

public interface Priced {
    Money price();
}
// Make SimpleProduct implement Priced (price() == basePrice())
// Make all decorators implement Priced (price() == basePrice() + surcharges)
