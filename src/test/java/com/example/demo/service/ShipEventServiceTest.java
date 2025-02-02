package com.example.demo.service;

import com.example.demo.config.Constant;
import com.example.demo.entity.PortEventEntity;
import com.example.demo.model.EventType;
import com.example.demo.model.ShipData;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class ShipEventServiceTest {

    @Autowired
    private ShipEventService shipEventService;

    private final Gson gson = new Gson();

    @BeforeEach
    void setup() {
        String expectedJsonStringData = """
                [
                  {
                    "id": "273436640",
                    "name": "ICE EAGLE",
                    "mmsi": "273436640",
                    "coordinates": [
                        33.05247166666667,
                        69.05372333333334
                    ],
                    "destination": "MURMANSK",
                    "isOutsideRotterdam": true
                  },
                  {
                    "id": "273436641",
                    "name": "ICE EAGLE 2",
                    "mmsi": "273436641",
                    "coordinates": [
                        4.5000,
                        51.9200
                    ],
                    "destination": "MURMANSK",
                    "isOutsideRotterdam": false
                  }
                ]
                """;

        Constant.SHIPS_DATA.clear();
        List<ShipData> expected = gson.fromJson(expectedJsonStringData, new TypeToken<List<ShipData>>() {}.getType());
        expected.forEach(shipData -> Constant.SHIPS_DATA.put(shipData.id(), shipData));

        String jsonPortEventData = """
                [
                  {
                    "id": 1,
                    "portName": "ROTTERDAM",
                    "shipName": "ICE EAGLE",
                    "shipId": "76576374",
                    "eventType": "ENTER",
                    "updatedAt": 1621004496000
                  },
                  {
                    "id": 2,
                    "portName": "ROTTERDAM",
                    "shipName": "ICE EAGLE",
                    "shipId": "76576374",
                    "eventType": "LEAVE",
                    "updatedAt": 1621008496000
                  }
                ]
                """;

        Constant.PORT_EVENTS.clear();
        List<PortEventEntity> expectedEvents = gson.fromJson(jsonPortEventData, new TypeToken<List<PortEventEntity>>() {}.getType());
        expectedEvents.forEach(event -> {
            List<PortEventEntity> lists = Constant.PORT_EVENTS.getOrDefault(event.getShipId(), new ArrayList<>());
            lists.add(event);
            Constant.PORT_EVENTS.put(event.getShipId(), lists);
        });

    }

    @Test
    public void shouldBeAbleToGetShipsEnteringPort(){
        List<ShipData> list = shipEventService.getShipLists(false);
        assertEquals(1, list.size());
        assertEquals("273436641", list.get(0).id());
    }

    @Test
    public void shouldBeAbleToGetShipsLeavingPort(){
        List<ShipData> list = shipEventService.getShipLists(true);
        assertEquals(1, list.size());
        assertEquals("273436640", list.get(0).id());
    }

    @Test
    public void shouldBeAbleToGetPortEvents(){
        List<PortEventEntity> list = shipEventService.getRotterdamPortEvents();
        assertEquals(2, list.size());
        assertEquals("76576374", list.get(0).getShipId());
        assertEquals(EventType.ENTER, list.get(0).getEventType());
        assertEquals("76576374", list.get(1).getShipId());
        assertEquals(EventType.LEAVE, list.get(1).getEventType());
    }
}
