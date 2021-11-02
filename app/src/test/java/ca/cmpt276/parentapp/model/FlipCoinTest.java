package ca.cmpt276.parentapp.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

class FlipCoinTest {

    @Test
    void testRandomSelect() {
        ArrayList<String> str = new ArrayList<String>();
        str.add("Able");
        str.add("Betty");
        FlipCoin fc = FlipCoin.getInstance();
        assertEquals("Able",fc.generateCurrentPickChild(str));
    }

}