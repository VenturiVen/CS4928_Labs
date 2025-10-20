package com.cafepos.smells;

import com.cafepos.common.Money;
import com.cafepos.factory.ProductFactory;
import com.cafepos.catalog.Product;

public class OrderManagerGod {
    public static int TAX_PERCENT = 10;
    public static String LAST_DISCOUNT_CODE = null;

    // God Class & Long Method: One method performs creation, pricing, discounting, tax, payment I/O, and printing.
    public static String process(String recipe, int qty, String paymentType, String discountCode, boolean printReceipt)
    {
        // God Class & Long Method: Creation
        ProductFactory factory = new ProductFactory();
        Product product = factory.create(recipe);

        // God Class & Long Method: Pricing
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
        
        // God Class & Long Method: Discounting
        if (discountCode != null) {
            if (discountCode.equalsIgnoreCase("LOYAL5")) {
                discount = Money.of(subtotal.asBigDecimal()
                        .multiply(java.math.BigDecimal.valueOf(5))
                        .divide(java.math.BigDecimal.valueOf(100)));
            } else if (discountCode.equalsIgnoreCase("COUPON1")) {
                discount = Money.of(1.00);
            } else if (discountCode.equalsIgnoreCase("NONE")) {
                discount = Money.zero();
            } else {
                discount = Money.zero();
            }
            LAST_DISCOUNT_CODE = discountCode;
        }

        // God Class & Long Method: Tax
        Money discounted = Money.of(subtotal.asBigDecimal().subtract(discount.asBigDecimal()));

        if (discounted.asBigDecimal().signum() < 0)
            discounted = Money.zero();
        var tax = Money.of(discounted.asBigDecimal()
                .multiply(java.math.BigDecimal.valueOf(TAX_PERCENT))
                .divide(java.math.BigDecimal.valueOf(100)));
        var total = discounted.add(tax);

        // God Class & Long Method: Payment I/O
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

        // God Class & Long Method: printing
        StringBuilder receipt = new StringBuilder();
        receipt.append("Order (").append(recipe).append(")x").append(qty).append("\n");
        receipt.append("Subtotal: ").append(subtotal).append("\n");
        if (discount.asBigDecimal().signum() > 0) {
            receipt.append("Discount: -").append(discount).append("\n");
        }
        receipt.append("Tax (").append(TAX_PERCENT).append("%):").append(tax).append("\n");
        receipt.append("Total: ").append(total);
        String out = receipt.toString();

        // God Class & Long Method: Printing
        if (printReceipt) {
            System.out.println(out);
        }
        return out;
    }
}