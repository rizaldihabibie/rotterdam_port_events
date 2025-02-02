package com.example.demo.model;

public record ShipData(
        String id,
        String name,
        String destination,
        double[] coordinates,
        boolean isOutsideRotterdam
) {
}
