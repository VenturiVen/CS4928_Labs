package com.cafepos;

import java.util.List;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import com.cafepos.common.Money;
import com.cafepos.menu.Menu;
import com.cafepos.menu.MenuComponent;
import com.cafepos.menu.MenuItem;
import com.cafepos.state.OrderFSM;

public class WK9Test {

    @Test
    void depth_first_iteration_collects_all_nodes() {
        Menu root = new Menu("ROOT");
        Menu a = new Menu("A");
        Menu b = new Menu("B");
        root.add(a);
        root.add(b);
        a.add(new MenuItem("x", Money.of(1.0), true));
        b.add(new MenuItem("y", Money.of(2.0), false));
        List<String> names = root.allItems().stream().map(MenuComponent::name).toList();
        assertTrue(names.contains("x"));
        assertTrue(names.contains("y"));
    }

    @Test
    void order_fsm_happy_path() {
        OrderFSM fsm = new OrderFSM();
        assertEquals("NEW", fsm.status());
        fsm.pay();
        assertEquals("PREPARING", fsm.status());
        fsm.markReady();
        assertEquals("READY", fsm.status());
        fsm.deliver();
        assertEquals("DELIVERED", fsm.status());
    }

    @Test
    void vegetarian_items_only() {
        Menu root = new Menu("ROOT");
        root.add(new MenuItem("Salad", Money.of(5.0), true));
        root.add(new MenuItem("Steak", Money.of(10.0), false));
        List<String> vegNames = root.vegetarianItems().stream().map(MenuComponent::name).toList();
        assertTrue(vegNames.contains("Salad"));
        assertFalse(vegNames.contains("Steak"));
    }

    @Test
    void depth_first_traversal_order_is_correct() {
        Menu root = new Menu("ROOT");
        Menu a = new Menu("A");
        Menu b = new Menu("B");
        Menu c = new Menu("C");
        root.add(a);
        root.add(b);
        a.add(c);
        a.add(new MenuItem("x", Money.of(1.0), true));
        c.add(new MenuItem("y", Money.of(2.0), true));
        b.add(new MenuItem("z", Money.of(3.0), false));

        List<String> names =
            root.allItems().stream().map(MenuComponent::name).toList();

        assertEquals(List.of("A", "C", "y", "x", "B", "z"), names);
    }

    // @Test
    // void illegal_transition_throws_exception() {
    //     OrderFSM fsm = new OrderFSM();
    //     // Trying to deliver directly from NEW should fail
    //     Exception ex = assertThrows(IllegalStateException.class, fsm::deliver);
    //     assertTrue(ex.getMessage().contains("Cannot deliver from state NEW"));
    // }
}
