package com.example.demo.service;

import com.example.demo.config.Constant;
import com.example.demo.entity.PortEventEntity;
import com.example.demo.exceptions.TeqplayClientServiceException;
import com.example.demo.model.EventType;
import com.example.demo.model.ShipData;
import com.example.demo.model.teqplay.Ship;
import com.example.demo.model.teqplay.TeqplayError;
import com.example.demo.model.teqplay.TeqplayErrorResponse;
import com.example.demo.repository.PortEventRepository;
import com.example.demo.service.interfaces.ShipService;
import com.example.demo.service.interfaces.TeqplayClientService;
import com.example.demo.util.CommonFunction;
import com.google.gson.Gson;
import okhttp3.ResponseBody;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class TeqplayShipService implements ShipService {

    private TeqplayClientService teqplayClientService;

    @Autowired
    private Retrofit retrofit;

    @Autowired
    private TeqplayTokenService teqplayTokenService;

    @Autowired
    private PortEventRepository portEventRepository;

    private final Logger LOGGER = LogManager.getLogger(TeqplayTokenService.class);

    @Override
    public void getShipsData() {

        try {
            String token = teqplayTokenService.getToken();
            teqplayClientService = retrofit.create(TeqplayClientService.class);
            Call<List<Ship>> teqplayCall = teqplayClientService.getShipsData(token);

            Response<List<Ship>> response = teqplayCall.execute();
            if (response.isSuccessful()) {
                List<Ship> listShips = response.body();
                if (listShips != null && !listShips.isEmpty()) {
                    for(Ship ship : listShips) {
                        boolean isOutsideRotterdamPort = CommonFunction.isOutsideRotterdam(ship.location().coordinates());
                        if (isOutsideRotterdamPort) {
                            ShipData shipData = Constant.SHIPS_DATA.getOrDefault(ship.mmsi(), null);
                            if(shipData != null && !shipData.isOutsideRotterdam()) {
                                PortEventEntity portEventEntity = PortEventEntity.builder()
                                        .shipId(ship.mmsi())
                                        .shipName(ship.name())
                                        .portName("ROTTERDAM")
                                        .updatedAt(ship.timeLastUpdate())
                                        .eventType(EventType.LEAVE)
                                        .build();
                                portEventRepository.save(portEventEntity);
                                List<PortEventEntity> lists = Constant.PORT_EVENTS.getOrDefault(ship.mmsi(), new ArrayList<>());
                                lists.add(portEventEntity);
                                Constant.PORT_EVENTS.put(ship.mmsi(),lists);
                            }
                        } else {
                            PortEventEntity portEventEntity = PortEventEntity.builder()
                                    .shipId(ship.mmsi())
                                    .shipName(ship.name())
                                    .portName("ROTTERDAM")
                                    .updatedAt(ship.timeLastUpdate())
                                    .eventType(EventType.ENTER)
                                    .build();

                            portEventRepository.save(portEventEntity);
                            List<PortEventEntity> lists = Constant.PORT_EVENTS.getOrDefault(ship.mmsi(), new ArrayList<>());
                            lists.add(portEventEntity);
                            Constant.PORT_EVENTS.put(ship.mmsi(),lists);
                        }
                        Constant.SHIPS_DATA.putIfAbsent(ship.mmsi(), new ShipData(
                                ship.mmsi(),
                                ship.name(),
                                ship.destination(),
                                new double[]{
                                        ship.location().coordinates()[0],
                                        ship.location().coordinates()[1],
                                },
                                isOutsideRotterdamPort
                        ));
                    }
                }
            } else {
                checkErrorRequest(response);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    private void checkErrorRequest(Response<?> response) throws TeqplayClientServiceException {
        try (ResponseBody errorBody = response.errorBody()) {
            if (errorBody != null) {
                Gson gson = new Gson();
                TeqplayErrorResponse teqplayErrorResponse = gson.fromJson(errorBody.string(), TeqplayErrorResponse.class);
                if (teqplayErrorResponse.statusCode() == TeqplayError.WRONG_CREDENTIAL.getCode()) {
                    LOGGER.error("Teqplay API call return forbidden. {}", gson.toJson(teqplayErrorResponse));
                    throw new TeqplayClientServiceException(TeqplayError.WRONG_CREDENTIAL);
                } else {
                    LOGGER.error("Teqplay API call return unknown error. {}", gson.toJson(teqplayErrorResponse));
                    throw new TeqplayClientServiceException(TeqplayError.UNKNOWN_ERROR);
                }
            } else {
                LOGGER.error(
                        "Teqplay API call return unknown error. code : {}, response : {}",
                        response.code(),
                        response.body()
                );
                throw new TeqplayClientServiceException(TeqplayError.UNKNOWN_ERROR);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
