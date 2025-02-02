package com.example.demo.service;

import com.example.demo.config.Constant;
import com.example.demo.entity.PortEventEntity;
import com.example.demo.model.ShipData;
import com.example.demo.repository.PortEventRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class ShipEventService {

    @Autowired
    private PortEventRepository portEventRepository;

    public List<ShipData> getShipLists(boolean isOutside) {
        Predicate<ShipData> leavingPredicate = shipData -> shipData.isOutsideRotterdam() == isOutside;
        return Constant.SHIPS_DATA.values()
                .stream()
                .filter(leavingPredicate)
                .collect(Collectors.toList());
    }

    public List<PortEventEntity> getRotterdamPortEvents() {
        List<PortEventEntity> allEvents = new ArrayList<>();
        Constant.PORT_EVENTS.values().forEach(allEvents::addAll);
        return allEvents;
    }

    @PostConstruct
    public void initPortEvents() {
        Constant.PORT_EVENTS.clear();
        portEventRepository.findAll()
                .forEach(event -> {
                    List<PortEventEntity> lists = Constant.PORT_EVENTS.getOrDefault(event.getShipId(), new ArrayList<>());
                    lists.add(event);
                    Constant.PORT_EVENTS.put(event.getShipId(), lists);
                });
    }
}
