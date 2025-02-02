package com.example.demo.model.teqplay;

public record Ship(
        String _id,
        String mmsi,
        ShipLocation location,
        Double courseOverGround,
        long timeLastUpdate,
        String imoNumber,
        String name,
        String shipType,
        String status,
        String speedOverGround,
        Integer coms,
        String destination,
        String trueDestination,
        ShipExtras extras
) {
}
