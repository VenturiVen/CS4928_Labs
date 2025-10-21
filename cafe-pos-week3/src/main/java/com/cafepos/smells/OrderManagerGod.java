package com.cafepos.smells;

import com.cafepos.common.Money;
import com.cafepos.domain.Order;
import com.cafepos.domain.OrderIds;
import com.cafepos.factory.ProductFactory;
import com.cafepos.payment.CardPayment;
import com.cafepos.payment.CashPayment;
import com.cafepos.payment.WalletPayment;
import com.cafepos.pricing.FixedCouponDiscount;
import com.cafepos.pricing.FixedRateTaxPolicy;
import com.cafepos.pricing.LoyaltyPercentDiscount;
import com.cafepos.pricing.NoDiscount;
import com.cafepos.pricing.ReceiptPrinter;
import com.cafepos.pricing.PricingService;
import com.cafepos.catalog.Product;

public class OrderManagerGod {

    public static int TAX_PERCENT = 10;

    public static String LAST_DISCOUNT_CODE = null;

    public static String process(String recipe, int qty, String paymentType, String discountCode, boolean printReceipt)
    {
        ProductFactory factory = new ProductFactory();
        Product product = factory.create(recipe);

        Money unitPrice;
        try {
            var priced = product instanceof com.cafepos.decorator.Priced p ? p.price() : product.basePrice();
            unitPrice = priced;
        } catch (Exception e) {
            unitPrice = product.basePrice();
        }
        if (qty <= 0)
            qty = 1;
        Money subtotal = unitPrice.multiply(qty);
        Money discount = Money.zero();

        if (discountCode != null) {
            if (discountCode.equalsIgnoreCase("LOYAL5")) {
                // 5% loyalty discount
                discount = new LoyaltyPercentDiscount(5).discountOf(subtotal);
            } else if (discountCode.equalsIgnoreCase("COUPON1")) {
                // Fixed discount
                discount = new FixedCouponDiscount(Money.of(1.00)).discountOf(subtotal);
            } else if (discountCode.equalsIgnoreCase("NONE")) {
                // No discount
                discount = new NoDiscount().discountOf(subtotal);
            } else {
                // No discount
                discount = new NoDiscount().discountOf(subtotal);
            }

            LAST_DISCOUNT_CODE = discountCode;
        }

        Money discounted = Money.of(subtotal.asBigDecimal().subtract(discount.asBigDecimal()));

        if (discounted.asBigDecimal().signum() < 0)
            discounted = Money.zero();

        var tax = new FixedRateTaxPolicy(TAX_PERCENT).taxOn(discounted);
        var total = discounted.add(tax);

        if (paymentType != null) {
            if (paymentType.equalsIgnoreCase("CASH")) {
                new CashPayment().pay(new Order(OrderIds.next()));
            } else if (paymentType.equalsIgnoreCase("CARD")) {
                new CardPayment("1234").pay((new Order(OrderIds.next())));
            } else if (paymentType.equalsIgnoreCase("WALLET")) {
                new WalletPayment("user-wallet-789").pay(new Order(OrderIds.next()));
            } else {
                System.out.println("[UnknownPayment] " + total);
            }
        }

        String receipt = new ReceiptPrinter()
                .format(recipe, qty, new PricingService.PricingResult(subtotal, discount, tax, total), TAX_PERCENT);

        System.out.println(receipt);

        return receipt;
    }
}