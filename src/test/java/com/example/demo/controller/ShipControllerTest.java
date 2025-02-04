package com.example.demo.controller;

import com.example.demo.entity.PortEventEntity;
import com.example.demo.model.ShipData;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.testcontainers.utility.TestcontainersConfiguration;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.testcontainers.shaded.org.awaitility.Awaitility.await;

@SpringBootTest
@Import(TestcontainersConfiguration.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class ShipControllerTest {
    private MockMvc mockMvc;

    @Autowired
    private ShipController shipController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(shipController).build();
    }

    @Test
    void shouldBeAbleToGetPortEvent() {

        Gson gson = new Gson();

        await().atMost(70, TimeUnit.SECONDS).until(() -> {
            ResultActions resultActions = mockMvc.perform(get("/ships/rotterdam/events"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType("application/json"));

            List<PortEventEntity> lists = gson.fromJson(resultActions.andReturn().getResponse().getContentAsString(), new TypeToken<List<PortEventEntity>>() {}.getType());

            return lists.size() > 5;
        });
    }

    @Test
    void shouldBeAbleToGetAllShipsEvent() throws Exception {

        Gson gson = new Gson();

        await().atMost(70, TimeUnit.SECONDS).until(() -> {
            ResultActions resultActions = mockMvc.perform(get("/ships/rotterdam"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType("application/json"));

            List<ShipData> lists = gson.fromJson(resultActions.andReturn().getResponse().getContentAsString(), new TypeToken<List<ShipData>>() {}.getType());

            return lists.size() > 5;
        });
    }

}
