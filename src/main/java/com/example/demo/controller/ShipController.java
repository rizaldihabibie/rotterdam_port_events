package com.example.demo.controller;

import com.example.demo.entity.PortEventEntity;
import com.example.demo.model.ShipData;
import com.example.demo.service.ShipEventService;
import com.example.demo.service.interfaces.ShipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/ships")
public class ShipController {

    @Autowired
    private ShipService teqplayShipService;

    @Autowired
    private ShipEventService shipEventService;

    @GetMapping("/rotterdam")
    public ResponseEntity<List<ShipData>> getShipListInRotterdam(){
        return new ResponseEntity<>(shipEventService.getShipLists(false), HttpStatus.OK);
    }

    @GetMapping("/rotterdam/events")
    public ResponseEntity<List<PortEventEntity>> getRotterdamPortEvent(){
        return new ResponseEntity<>(shipEventService.getRotterdamPortEvents(), HttpStatus.OK);
    }

    @Scheduled(fixedRate = 60000)  // 60000 ms = 1 minute
    public void pollShipsData() {
        teqplayShipService.getShipsData();
    }
}
