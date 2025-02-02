package com.example.demo.service;

import com.example.demo.exceptions.TeqplayClientServiceException;
import com.example.demo.model.teqplay.TeqplayError;
import com.example.demo.model.teqplay.TeqplayErrorResponse;
import com.example.demo.model.teqplay.TeqplayToken;
import com.example.demo.service.interfaces.TeqplayClientService;
import com.google.gson.Gson;
import okhttp3.MediaType;
import okhttp3.ResponseBody;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.Call;

import java.io.IOException;

import static okhttp3.ResponseBody.create;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class TeqplayTokenServiceTest {

    @InjectMocks
    private TeqplayTokenService teqplayTokenService;

    @Mock
    private Retrofit retrofit;

    @Mock
    private TeqplayClientService teqplayClientService;

    @Mock
    private Call<TeqplayToken> teqplayCall;

    private final Gson gson = new Gson();

    @BeforeEach
    public void setup() {
        when(retrofit.create(TeqplayClientService.class)).thenReturn(teqplayClientService);
    }

    @Test
    public void failedToLoginBecauseOfWrongPassword() {
        when(teqplayClientService.getToken(any())).thenReturn(teqplayCall);

        TeqplayErrorResponse errorResponse = new TeqplayErrorResponse(
                1401,
                "Login failed: Combination of entered username and password is not correct for username: rizaldihabibie28@gmail.com"
        );

        String errorJsonString = gson.toJson(errorResponse);
        try (ResponseBody responseBody = create(errorJsonString, MediaType.get("application/json"))) {
            Response<TeqplayToken> mockedErrorResponse = Response.error(401, responseBody);
            when(teqplayCall.execute()).thenReturn(mockedErrorResponse);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Exception exception = assertThrows(TeqplayClientServiceException.class, () -> {
            teqplayTokenService.getToken();
        });

        assertEquals(TeqplayError.WRONG_CREDENTIAL.getMessage(), exception.getMessage());

    }

    @Test
    public void loginSuccess() {
        when(teqplayClientService.getToken(any())).thenReturn(teqplayCall);

        TeqplayToken teqplayToken = new TeqplayToken(
                "rizaldihabibie28@gmail.com",
                "yjnksdfsdWEW76385345",
                "theToken12324sdff",
                8600,
                "2021-06-01T08:46:20Z"

        );

        try {
            Response<TeqplayToken> mockedErrorResponse = Response.success(200, teqplayToken);
            when(teqplayCall.execute()).thenReturn(mockedErrorResponse);

            String actualResult = teqplayTokenService.getToken();
            assertEquals(teqplayToken.token(), actualResult);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
