package com.example.demo.repository;

import com.example.demo.entity.PortEventEntity;
import com.example.demo.model.EventType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.testcontainers.utility.TestcontainersConfiguration;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Import(TestcontainersConfiguration.class)
public class PortEventRepositoryTest {

    @Autowired
    private PortEventRepository portEventRepository;

    @Test
    void shouldBeAbleToSaveAndFetchData() {

        portEventRepository.deleteAllInBatch();

        List<PortEventEntity> initial = portEventRepository.findAll();
        assertEquals(0, initial.size());

        PortEventEntity portEventEntity1 = new PortEventEntity();
        portEventEntity1.setPortName("ROTTERDAM");
        portEventEntity1.setEventType(EventType.ENTER);
        portEventEntity1.setShipId("76576374");
        portEventEntity1.setShipName("ICE EAGLE");
        portEventEntity1.setUpdatedAt(1621004496000L);

        portEventRepository.save(portEventEntity1);

        PortEventEntity portEventEntity2 = new PortEventEntity();
        portEventEntity2.setPortName("ROTTERDAM");
        portEventEntity2.setEventType(EventType.LEAVE);
        portEventEntity2.setShipId("76576374");
        portEventEntity2.setShipName("ICE EAGLE");
        portEventEntity2.setUpdatedAt(1621008496000L);

        portEventRepository.save(portEventEntity2);

        List<PortEventEntity> listPortEvent = portEventRepository.findAll();
        assertEquals(2, listPortEvent.size());
        assertEquals(EventType.ENTER, listPortEvent.get(0).getEventType());
        assertEquals(EventType.LEAVE, listPortEvent.get(1).getEventType());

    }
}
