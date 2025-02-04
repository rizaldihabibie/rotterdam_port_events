package com.example.demo.config;

import com.example.demo.entity.PortEventEntity;
import com.example.demo.model.ShipData;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Constant {
    public static Map<String, ShipData> SHIPS_DATA = new ConcurrentHashMap<>();
    public static Map<String, List<PortEventEntity>> PORT_EVENTS = new ConcurrentHashMap<>();
}
