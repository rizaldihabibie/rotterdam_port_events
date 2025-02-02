package com.example.demo.config;

import com.example.demo.entity.PortEventEntity;
import com.example.demo.model.ShipData;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Constant {
    public static Map<String, ShipData> SHIPS_DATA = new HashMap<>();
    public static Map<String, List<PortEventEntity>> PORT_EVENTS = new HashMap<>();
}
