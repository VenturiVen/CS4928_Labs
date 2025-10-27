package com.cafepos.demo;

import com.cafepos.catalog.*;
import com.cafepos.checkout.CheckoutService;
import com.cafepos.domain.*;
import com.cafepos.factory.ProductFactory;
import com.cafepos.pricing.FixedRateTaxPolicy;
import com.cafepos.pricing.LoyaltyPercentDiscount;
import com.cafepos.pricing.PricingService;
import com.cafepos.pricing.ReceiptPrinter;
import com.cafepos.payment.CardPayment;
import com.cafepos.payment.CashPayment;
import com.cafepos.payment.WalletPayment;
import com.cafepos.domain.CustomerNotifier;

import java.util.Scanner;

public final class MidtermCLI {

    public static void main(String[] args) {
        int discountPercent = 0;
        String receipt = "";
        String recipe = "";
        int qty = 0;

        Scanner scanner = new Scanner(System.in);
        ProductFactory factory = new ProductFactory();
        Order order = new Order(OrderIds.next());

        System.out.println("Welcome to Cafe POS CLI!");

        System.out.println("Coffee's:");
        System.out.println("  ESP - Espresso");
        System.out.println("  LAT - Latte");
        System.out.println("  CAP - Cappuccino");
        System.out.println("Add on:");
        System.out.println("  SHOT - Extra Shot");
        System.out.println("  OAT  - Oat Milk");
        System.out.println("  SYP  - Syrup");
        System.out.println("  L    - Large Size");
        System.out.println();

        while (true) {
            System.out.print("Enter product recipe (e.g., ESP+SHOT+OAT), or 'done' to finish: ");
            String input = scanner.nextLine().trim();
            if (input.equalsIgnoreCase("done")) {
                break;
            } else {
                recipe = input;
            }

            new CustomerNotifier().updated(order, "Item Added: " + recipe);

            Product product = factory.create(recipe);
            System.out.print("Enter quantity: ");
            qty = Integer.parseInt(scanner.nextLine().trim());

            new CustomerNotifier().updated(order, "Quantity Updated: " + qty);

            order.addItem(new LineItem(product, qty));
            System.out.println("Added: " + product.name() + " x" + qty);

            System.out.print("Do you have a discount code? (y/n): ");
            if (scanner.nextLine().trim().equalsIgnoreCase("y")) {
                System.out.print("Enter discount code: ");

                String inputCode = scanner.nextLine().trim();
                if (inputCode.equalsIgnoreCase("LOYAL5")) {
                    discountPercent = 5;
                    new CustomerNotifier().updated(order, "Discount Applied: " + discountPercent + "%");
                } else {
                    System.out.println("Invalid discount code.");
                }
            }

            System.out.print("Payment Type (Card/Wallet/Cash): ");
            String paymentType = scanner.nextLine().trim();
            if (paymentType.equalsIgnoreCase("card")) {
                System.out.print("Card number: ");
                String cardNum = scanner.nextLine().trim();
                if (cardNum.length() < 4) {
                    System.out.println("Invalid card number. Defaulting to Cash.");
                    order.pay(new CashPayment());
                } else {
                    order.pay(new CardPayment(cardNum));
                }
            } else if (paymentType.equalsIgnoreCase("wallet")) {
                System.out.print("Wallet ID: ");
                String walletID = scanner.nextLine().trim();
                order.pay(new WalletPayment(walletID));
            } else if (paymentType.equalsIgnoreCase("cash")) {
                order.pay(new CashPayment());
            } else {
                System.out.println("Unknown payment type. Defaulting to Cash.");
                order.pay(new CashPayment());
            }
        }

        var pricing = new PricingService(new LoyaltyPercentDiscount(discountPercent), new FixedRateTaxPolicy(10));
        var printer = new ReceiptPrinter();
        var checkout = new CheckoutService(new ProductFactory(), pricing, printer, 10);

        receipt = checkout.checkout(recipe, qty);

        System.out.println("\nOrder #" + order.id());
        for (LineItem li : order.items()) {
            System.out.println(" - " + li.product().name() + " x" + li.quantity() + " = " + li.lineTotal());
        }

        System.out.println(
                "\n========================================\n" +
                        "                 Receipt\n" +
                        "----------------------------------------\n" +
                        receipt +
                        "\n========================================\n");

        new CustomerNotifier().updated(order, "Ready for collection");

        scanner.close();

    }
}
