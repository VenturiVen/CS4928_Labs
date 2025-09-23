package com.cafepos.payment;

import com.cafepos.domain.Order;

public final class CardPayment implements PaymentStrategy {

    private final String cardNumber;
    private final String cardConcat;

    public CardPayment(String cardNumber) {
        this.cardNumber = cardNumber;
        this.cardConcat = cardNumber.substring(cardNumber.length()-4, cardNumber.length());
    }

    public String getCardNumber() {
        return cardNumber;
    }
    
    @Override
    public void pay(Order order) {
    
    System.out.println("[Card] Customer paid " + order.totalWithTax(10) + " EUR with card ****" + cardConcat);
    }
}
