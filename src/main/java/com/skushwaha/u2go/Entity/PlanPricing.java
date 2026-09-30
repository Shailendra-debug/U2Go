package com.skushwaha.u2go.Entity;

public class PlanPricing {
    public static Long getAmountInPaise(UrlPlan plan) {
        return switch (plan) {
            case MONTHLY -> 11L * 100;      // ₹11
            case QUARTERLY -> 21L * 100;    // ₹21
            case HALF_YEARLY -> 35L * 100;  // ₹35
            case YEARLY -> 51L * 100;       // ₹51 (assumed from faint text)
            default -> throw new IllegalArgumentException("Invalid paid plan: " + plan);
        };
    }

    public static int getDurationInMonths(UrlPlan plan) {
        return switch (plan) {
            case MONTHLY -> 1;
            case QUARTERLY -> 3;
            case HALF_YEARLY -> 6;
            case YEARLY -> 12;
            default -> 0;
        };
    }
}
