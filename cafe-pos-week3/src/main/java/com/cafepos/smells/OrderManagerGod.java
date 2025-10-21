package com.cafepos.smells;

import com.cafepos.common.Money;
import com.cafepos.factory.ProductFactory;
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
                System.out.println("[Cash] Customer paid " + total + "EUR");
            } else if (paymentType.equalsIgnoreCase("CARD")) {
                System.out.println("[Card] Customer paid " + total + "EUR with card ****1234");
            } else if (paymentType.equalsIgnoreCase("WALLET")) {
                System.out.println("[Wallet] Customer paid " + total + "EUR via wallet user-wallet-789");
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