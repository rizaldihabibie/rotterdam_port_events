package com.example.demo.service;

import com.example.demo.config.Constant;
import com.example.demo.entity.PortEventEntity;
import com.example.demo.model.ShipData;
import com.example.demo.model.teqplay.Ship;
import com.example.demo.repository.PortEventRepository;
import com.example.demo.service.interfaces.TeqplayClientService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.testcontainers.utility.TestcontainersConfiguration;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@Import(TestcontainersConfiguration.class)
public class TeqplayShipServiceTest {

    @InjectMocks
    private TeqplayShipService teqplayShipService;

    @Mock
    private Retrofit retrofit;

    @Mock
    private TeqplayTokenService teqplayTokenService;

    @Mock
    private PortEventRepository portEventRepository;

    @Mock
    private TeqplayClientService teqplayClientService;

    @Mock
    private Call<List<Ship>> teqplayCall;

    private final Gson gson = new Gson();

    @BeforeEach
    public void setup() {
        when(retrofit.create(TeqplayClientService.class)).thenReturn(teqplayClientService);
        when(portEventRepository.save(any())).thenReturn(new PortEventEntity());
    }

    @Test
    public void ableToFetchAndFillInMemoryStorage() {
        Constant.SHIPS_DATA.clear();
        when(teqplayClientService.getShipsData(any(String.class))).thenReturn(teqplayCall);

        String expectedJsonStringResponse = """
                [
                  {
                    "_id": "273436640",
                    "mmsi": "273436640",
                    "location": {
                      "type": "Point",
                      "coordinates": [
                        33.05247166666667,
                        69.05372333333334
                      ]
                    },
                    "courseOverGround": 243.2,
                    "timeLastUpdate": 1621004496000,
                    "imoNumber": "9171175",
                    "name": "ICE EAGLE",
                    "shipType": "TANKER_HAZCAT_A",
                    "status": "UNDER_WAY_USING_ENGINE",
                    "speedOverGround": 2.1,
                    "coms": 0,
                    "destination": "MURMANSK",
                    "trueDestination": "RUMMK",
                    "extras": {
                      "coms": 0
                    }
                  },
                  {
                    "_id": "273436641",
                    "mmsi": "273436641",
                    "location": {
                      "type": "Point",
                      "coordinates": [
                        4.5000,
                        51.9200
                      ]
                    },
                    "courseOverGround": 243.2,
                    "timeLastUpdate": 1621004496000,
                    "imoNumber": "9171175",
                    "name": "ICE EAGLE 2",
                    "shipType": "TANKER_HAZCAT_A",
                    "status": "UNDER_WAY_USING_ENGINE",
                    "speedOverGround": 2.1,
                    "coms": 0,
                    "destination": "MURMANSK",
                    "trueDestination": "RUMMK",
                    "extras": {
                      "coms": 0
                    }
                  }
                ]
                """;

        List<Ship> listExpected = gson.fromJson(expectedJsonStringResponse, new TypeToken<List<Ship>>() {}.getType());

        try {

            when(teqplayTokenService.getToken()).thenReturn("SOMETOKEN");
            Response<List<Ship>> mockedSuccessResponse = Response.success(200, listExpected);
            when(teqplayCall.execute()).thenReturn(mockedSuccessResponse);
            teqplayShipService.getShipsData();
            assertEquals(2, Constant.SHIPS_DATA.size());
            ShipData shipData = Constant.SHIPS_DATA.get(listExpected.get(0).mmsi());
            assertEquals(listExpected.get(0).name(), shipData.name());
            assertEquals(listExpected.get(0).destination(), shipData.destination());
            assertEquals(listExpected.get(0).location().coordinates()[0], shipData.coordinates()[0]);
            assertEquals(listExpected.get(0).location().coordinates()[1], shipData.coordinates()[1]);
            assertTrue(shipData.isOutsideRotterdam());
            ShipData shipData2 = Constant.SHIPS_DATA.get(listExpected.get(1).mmsi());
            assertEquals(listExpected.get(1).name(), shipData2.name());
            assertEquals(listExpected.get(1).destination(), shipData2.destination());
            assertEquals(listExpected.get(1).location().coordinates()[0], shipData2.coordinates()[0]);
            assertEquals(listExpected.get(1).location().coordinates()[1], shipData2.coordinates()[1]);
            assertFalse(shipData2.isOutsideRotterdam());

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

}
