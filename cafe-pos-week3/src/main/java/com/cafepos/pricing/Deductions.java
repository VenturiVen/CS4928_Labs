package com.cafepos.pricing;

public class Deductions {
    private static int TAX_PERCENT = 10;

    private static String LAST_DISCOUNT_CODE = null;

    public static int getTaxPercent() {
        return TAX_PERCENT;
    }

    public static void setTaxPercent(int taxPercent) {
        TAX_PERCENT = taxPercent;
    }

    public static String getLastDiscountCode() {
        return LAST_DISCOUNT_CODE;
    }

    public static void setLastDiscountCode(String lastDiscountCode) {
        LAST_DISCOUNT_CODE = lastDiscountCode;
    }

}
