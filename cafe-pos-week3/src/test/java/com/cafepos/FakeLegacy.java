package com.cafepos;

import vendor.legacy.LegacyThermalPrinter;

// Example adapter test idea (pseudo-JUnit)
class FakeLegacy extends LegacyThermalPrinter {
    int lastLen = -1;

    @Override
    public void legacyPrint(byte[] payload) {
        lastLen = payload.length;
    }

}