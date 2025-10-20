package com.cafepos.smells;

import com.cafepos.common.Money;
import com.cafepos.factory.ProductFactory;
import com.cafepos.catalog.Product;

public class OrderManagerGod {

    // Primitive Obsession: using int instead of a tax class
    public static int TAX_PERCENT = 10;

    // Primitive Obsession: using String instead of a DiscountCode enum or type
    public static String LAST_DISCOUNT_CODE = null;

    // Primitive Obsession: using String instead of enums or classes for recipe, paymentType, discountCode
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

        // Primitive Obsession: using String comparisons instead of a DiscountCode enum
        if (discountCode != null) {
            if (discountCode.equalsIgnoreCase("LOYAL5")) {
                discount = Money.of(subtotal.asBigDecimal() 
                        // Primitive Obsession: magic number should instead be a constant or part of a Discount class
                        .multiply(java.math.BigDecimal.valueOf(5))
                        .divide(java.math.BigDecimal.valueOf(100)));
            } else if (discountCode.equalsIgnoreCase("COUPON1")) {
                // Primitive Obsession: magic number should instead be a constant or part of a Discount class
                discount = Money.of(1.00);
            } else if (discountCode.equalsIgnoreCase("NONE")) {
                discount = Money.zero();
            } else {
                discount = Money.zero();
            }

            LAST_DISCOUNT_CODE = discountCode;
        }

        Money discounted = Money.of(subtotal.asBigDecimal().subtract(discount.asBigDecimal()));

        if (discounted.asBigDecimal().signum() < 0)
            discounted = Money.zero();

        // Primitive Obsession: uses primitive constants instead of a Tax class or object
        var tax = Money.of(discounted.asBigDecimal()
                .multiply(java.math.BigDecimal.valueOf(TAX_PERCENT))
                .divide(java.math.BigDecimal.valueOf(100)));
        var total = discounted.add(tax);

        // Primitive Obsession: using String instead of a PaymentType enum
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

        StringBuilder receipt = new StringBuilder();
        receipt.append("Order (").append(recipe).append(")x").append(qty).append("\n");
        receipt.append("Subtotal: ").append(subtotal).append("\n");
        if (discount.asBigDecimal().signum() > 0) {
            receipt.append("Discount: -").append(discount).append("\n");
        }
        receipt.append("Tax (").append(TAX_PERCENT).append("%):").append(tax).append("\n");
        receipt.append("Total: ").append(total);
        String out = receipt.toString();

        if (printReceipt) {
            System.out.println(out);
        }
        return out;
    }
}