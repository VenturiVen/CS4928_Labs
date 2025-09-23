package com.cafepos;

import com.cafepos.payment.CardPayment;
import com.cafepos.payment.CashPayment;
import com.cafepos.payment.PaymentStrategy;
import com.cafepos.payment.WalletPayment;
import com.cafepos.catalog.*;
import com.cafepos.common.*;
import com.cafepos.domain.*;
import com.cafepos.payment.*;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.Test;


public class OrderPaymentTests {

    @Test 
    void payment_strategy_called() {
    var p = new SimpleProduct("A", "A", Money.of(5));
    var order = new Order(42);
    order.addItem(new LineItem(p, 1));
    final boolean[] called = {false};
    PaymentStrategy fake = o -> called[0] = true;
    order.pay(fake);
    assertTrue(called[0], "Payment strategy should be called");
    }


    private Order sampleOrder() {
        var p = new SimpleProduct("Coffee", "Small coffee", Money.of(3));
        var order = new Order(99);
        order.addItem(new LineItem(p, 2)); // total = 6
        return order;
    }

    private String captureOutput(Runnable action) {
        var originalOut = System.out;
        var baos = new ByteArrayOutputStream();
        System.setOut(new PrintStream(baos));
        try {
            action.run();
        } finally {
            System.setOut(originalOut);
        }
        return baos.toString();
    }

    @Test
    void cashPayment_prints_message() {
        var order = sampleOrder();
        var cash = new CashPayment();

        String output = captureOutput(() -> order.pay(cash));

        assertTrue(output.contains("[Cash]"), "CashPayment should print with [Cash]");
    }

    @Test
    void cardPayment_prints_message() {
        var order = sampleOrder();
        var card = new CardPayment("12345678910112");

        String output = captureOutput(() -> order.pay(card));

        assertTrue(output.contains("[Card]"), "CardPayment should print with [Card]");
        assertTrue(output.contains("****0112"),"CardPayment should include last 4 digits of card number");
    }

    @Test
    void walletPayment_prints_message() {
        var order = sampleOrder();
        var wallet = new WalletPayment("wallet-abc-123");
        

        String output = captureOutput(() -> order.pay(wallet));

        assertTrue(output.contains("[Wallet]"), "WalletPayment should print with [Wallet]");
        assertTrue(output.contains("wallet-abc-123"), "WalletPayment should include wallet id");
    }


}
