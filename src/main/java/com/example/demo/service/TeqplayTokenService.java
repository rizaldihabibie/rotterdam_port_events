package com.example.demo.service;

import com.example.demo.exceptions.TeqplayClientServiceException;
import com.example.demo.model.teqplay.TeqplayError;
import com.example.demo.model.teqplay.TeqplayErrorResponse;
import com.example.demo.model.teqplay.TeqplayToken;
import com.example.demo.model.teqplay.TeqplayTokenRequestBody;
import com.example.demo.service.interfaces.TeqplayClientService;
import com.example.demo.service.interfaces.TokenService;
import com.google.gson.Gson;
import okhttp3.ResponseBody;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

import java.io.IOException;

@Service
public class TeqplayTokenService implements TokenService {

    @Value("${teqplay.client.username}")
    private String teqplayClientUsername;

    @Value("${teqplay.client.password}")
    private String teqplayClientPassword;

    private TeqplayClientService teqplayClientService;

    @Autowired
    private Retrofit retrofit;

    private final Logger LOGGER = LogManager.getLogger(TeqplayTokenService.class);


    @Override
    public String getToken() throws Exception {
        TeqplayTokenRequestBody tokenRequestBody = new TeqplayTokenRequestBody(
                teqplayClientUsername,
                teqplayClientPassword
        );

        teqplayClientService = retrofit.create(TeqplayClientService.class);
        Call<TeqplayToken> teqplayCall = teqplayClientService.getToken(tokenRequestBody);

        try {
            Response<TeqplayToken> response = teqplayCall.execute();
            if (response.isSuccessful()) {
                TeqplayToken teqplayToken = response.body();
                if (teqplayToken == null) {
                    LOGGER.error("Error occurred when getting token. Status success but response body is null");
                    throw new TeqplayClientServiceException(TeqplayError.UNKNOWN_ERROR);
                }
                return teqplayToken.token();
            } else {
                checkErrorRequest(response);
            }
            return null;
        } catch (TeqplayClientServiceException e) {
            throw new TeqplayClientServiceException(e.getTeqplayError());
        } catch (Exception ex) {
            LOGGER.error("Error occurred when getting token. {}", ex.getMessage());
            throw new Exception(ex);
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
