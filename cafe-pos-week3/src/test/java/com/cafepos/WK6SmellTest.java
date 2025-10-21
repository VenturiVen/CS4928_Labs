package com.cafepos;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.cafepos.smells.OrderManagerGod;

public class WK6SmellTest {

    @Test
    void testBasicEspressoOrder() {
        String receipt = OrderManagerGod.process("ESP", 1, "CASH", null, false);
        assertTrue(receipt.contains("ESP"));
        assertTrue(receipt.contains("Subtotal"));
        assertTrue(receipt.contains("Tax"));
        assertTrue(receipt.contains("Total"));
    }

    @Test
    void testDiscountAppliedCorrectly() {
        String receipt = OrderManagerGod.process("LAT", 2, "CARD", "LOYAL5", false);
        assertTrue(receipt.contains("Discount"));
    }

    @Test
    void testCouponDiscount() {
        String receipt = OrderManagerGod.process("CAP", 1, "WALLET", "COUPON1", false);
        assertTrue(receipt.contains("Discount: -"));
    }

    @Test
    void testUnknownPaymentType() {
        String receipt = OrderManagerGod.process("ESP+SHOT", 1, "CRYPTO", null, false);
        assertTrue(receipt.contains("Total"));
    }

    @Test
    void testReceiptPrinting() {
        String receipt = OrderManagerGod.process("LAT+OAT", 1, "CASH", "NONE", true);
        assertTrue(receipt.contains("LAT+OAT"));
    }  
}
