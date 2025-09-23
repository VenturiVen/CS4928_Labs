package com.cafepos;

import com.cafepos.common.Money;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class MoneyTests {

    @Test
    void addition_of_money() {
        var m1 = Money.of(2.50);
        var m2 = Money.of(3.75);
        var result = m1.add(m2);
        assertEquals(Money.of(6.25), result, "2.50 + 3.75 should equal 6.25");
    }

    @Test
    void multiplication_of_money() {
        var m = Money.of(4.00);
        var result = m.multiply(3);
        assertEquals(Money.of(12.00), result, "4.00 * 3 should equal 12.00");
    }

    @Test
    void zero_money_addition() {
        var m = Money.of(5.00);
        var zero = Money.of(0.00);
        assertEquals(m, m.add(zero), "Adding zero should return the same value");
    }
}

