package com.cafepos;

import org.junit.jupiter.api.Test;

import com.cafepos.domain.*;
import com.cafepos.printing.*;
import com.cafepos.command.*;


import static org.junit.jupiter.api.Assertions.*;;

public class WK8Test {

    @Test
    void undo_command_reverts_last_action() {
        Order order = new Order(OrderIds.next());
        OrderService service = new OrderService(order);
        PosRemote remote = new PosRemote(3);
        remote.setSlot(0, new AddItemCommand(service, "ESP+SHOT+OAT", 1));
        remote.setSlot(1, new AddItemCommand(service, "LAT+L", 2));
        remote.press(0);
        remote.press(1);
        assertEquals(2, order.items().size());
        remote.undo();
        assertEquals(1, order.items().size());
    }

    @Test
    void macro_undo() {
        Order order = new Order(OrderIds.next());
        OrderService service = new OrderService(order);
        
        var macro = new MacroCommand(
            new AddItemCommand(service, "ESP+SHOT+OAT", 1),
            new AddItemCommand(service, "LAT+L", 2)
        );
        macro.execute();
        assertEquals(2, order.items().size());
        macro.undo();
        assertEquals(0, order.items().size());
    }

    @Test
    void adapter_converts_text_to_bytes() {
        var fake = new FakeLegacy();
        Printer p = new LegacyPrinterAdapter(fake);
        p.print("ABC");
        assertTrue(fake.lastLen >= 3);
    }

    @Test
    void pos_remote_end_to_end_subtotal() {
        Order order = new Order(OrderIds.next());
        OrderService service = new OrderService(order);
        PosRemote remote = new PosRemote(3);

        remote.setSlot(0, new AddItemCommand(service, "ESP+SHOT+OAT", 1));
        remote.setSlot(1, new AddItemCommand(service, "LAT+L", 1));
        remote.setSlot(2, new PayOrderCommand(service, new com.cafepos.payment.CashPayment(), 10));
        remote.press(0);
        remote.press(1);
        remote.press(2);
        assertEquals(2, order.items().size());

        var factory = new com.cafepos.factory.ProductFactory();
        var p1 = factory.create("ESP+SHOT+OAT");
        var p2 = factory.create("LAT+L");
        com.cafepos.common.Money m1 = (p1 instanceof com.cafepos.decorator.Priced pr1) ? pr1.price() : p1.basePrice();
        com.cafepos.common.Money m2 = (p2 instanceof com.cafepos.decorator.Priced pr2) ? pr2.price() : p2.basePrice();
        com.cafepos.common.Money expected = m1.add(m2);
        assertEquals(expected, order.subtotal());
    }

}
