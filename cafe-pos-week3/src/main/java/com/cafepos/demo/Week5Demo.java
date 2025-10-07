package com.cafepos.demo;

import com.cafepos.catalog.*;
import com.cafepos.domain.*;
import com.cafepos.factory.ProductFactory;
import java.util.Scanner;

public final class Week5Demo {
    public static void main(String[] args) {
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
            String recipe = scanner.nextLine().trim();
            if (recipe.equalsIgnoreCase("done"))
                break;

            Product product = factory.create(recipe);
            System.out.print("Enter quantity: ");
            int qty = Integer.parseInt(scanner.nextLine().trim());

            order.addItem(new LineItem(product, qty));
            System.out.println("Added: " + product.name() + " x" + qty);
        }

        System.out.println("\nOrder #" + order.id());
        for (LineItem li : order.items()) {
            System.out.println(" - " + li.product().name() + " x" + li.quantity() + " = " + li.lineTotal());
        }
        System.out.println("Subtotal: " + order.subtotal());
        System.out.println("Tax (10%): " + order.taxAtPercent(10));
        System.out.println("Total: " + order.totalWithTax(10));
        scanner.close();

    }
}

