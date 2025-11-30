package com.cafepos.demo;

import com.cafepos.app.events.EventBus;
import com.cafepos.app.events.OrderCreated;
import com.cafepos.app.events.OrderPaid;
import com.cafepos.domain.OrderIds;
import com.cafepos.infra.Wiring;
import com.cafepos.ui.OrderController;
import com.cafepos.ui.ConsoleView;

import java.util.Scanner;

public final class FinalDemo {
    public static void main(String[] args) {

        
        long orderID = OrderIds.next(); // [Domain]
        int tax = 10;
        String recipe = "";
        int qty = 1;

        var c = Wiring.createDefault();
        var cont = new OrderController(c.repo(), c.checkout()); // [UI]
        var view = new ConsoleView(); // [UI]
        var bus = new EventBus(); // [App]

        try (Scanner scanner = new Scanner(System.in)) {

        // [UI] prints introduction from ConsoleView
            view.intro();

        // [App]
        // Subscribe to events
        // Whenever an event is emitted, the corresponding handler will be executed
        // good decoupling between event emitters and handlers
            bus.on(OrderCreated.class, e -> view.print("[UI] Order created with ID: #" + e.orderId()));
            bus.on(OrderPaid.class, e -> view.print("[UI] Order #" + e.orderId() + " has been paid."));

            cont.createOrder(orderID);
            bus.emit(new OrderCreated(orderID)); // emits event
            view.sign();

            while (true) {
                System.out.print("Enter product recipe (e.g., ESP+SHOT+OAT), 'done' to finish, or 'quit' to exit: ");
                String input = scanner.nextLine().trim().toUpperCase();

                if (input.equalsIgnoreCase("done")) {
                    break;
                } else if (input.equalsIgnoreCase("quit")) {
                    view.sign();
                    view.print("        Exiting Cafe POS.");
                    view.sign();
                    return;
                } else {
                    recipe = input;

                    try {
                        System.out.print("Enter quantity: ");
                        qty = Integer.parseInt(scanner.nextLine().trim());

                    } catch (NumberFormatException e) {
                        view.print("Invalid quantity. Please enter a number.");
                        continue;
                    } catch (IllegalArgumentException e) {
                        view.print("ERROR: " + e.getMessage());
                        continue;
                    }

                    cont.addItem(orderID, recipe, qty);
                }
            }

        // ==============================
        // [UI] Payment Strategy
        // ==============================
            view.sign();
            view.print("Payment Methods:");
            view.sign();
            view.print("1. Card");
            view.print("2. Cash");
            view.print("3. Wallet");
            System.out.print("Select (1-3): ");

            String paymentChoice = scanner.nextLine().trim();

            switch (paymentChoice) {
                case "1":
                    System.out.print("Card number (> 4): ");
                    String cardNum = scanner.nextLine().trim();
                    if (cardNum.length() < 4) {
                        view.print("Invalid card number. Defaulting to Cash.");
                    } else {
                        view.print("Card payment selected. Using card ending in " + cardNum.substring(cardNum.length() - 4));
                        break;
                    }
                case "2":
                    view.print("Cash payment selected.");
                    break;
                case "3":
                    System.out.print("Wallet ID: ");
                    String walletId = scanner.nextLine().trim();
                    view.print("Wallet payment selected. Using Wallet ID: " + walletId);
                    break;
                default:
                    view.print("Invalid choice. Using Cash.");
            }

        // ==============================
        // [UI] -> [Infra] + [Domain] Repository
        // ==============================
            view.sign();
            var retrieved = c.repo().findById(orderID);
            if (retrieved != null) {
                view.print("[Repo] Order retrieved from repository: #" + retrieved.get().id());
                view.print("[Repo] " + c.repo().getItem(orderID));
            }


            bus.emit(new OrderPaid(orderID)); // emits event for order paid
        // ==============================
        // [UI] -> [App] Receipt
        // ==============================
            view.sign();
            view.sign();
            view.print("          RECEIPT");
            view.dash();
            view.print(cont.checkout(orderID, tax));

            view.sign();
            view.print("Order complete. Exiting Cafe POS.");
            view.sign();

        }
    }
}