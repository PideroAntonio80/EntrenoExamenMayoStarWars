package com.sanvalero.entrenoexamenmayo.service;

import com.sanvalero.entrenoexamenmayo.domain.SwPeople;
import com.sanvalero.entrenoexamenmayo.domain.SwResults;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;

import javax.imageio.IIOException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.sanvalero.entrenoexamenmayo.util.Constants.URL;

/**
 * Creado por @ author: Pedro Orós
 * el 11/05/2021
 */

public class SwService {

    private SwApiService api;

    public SwService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        api = retrofit.create(SwApiService.class);
    }

    public Observable<SwResults> getSwPeople() {
        return api.getSwPeopleList();
    }

    public SwPeople getCharacter(int id) {
        Call<SwPeople> character = api.getSwCharacter(id);
        try {
            Response<SwPeople> response = character.execute();
            if(response.body() != null) {
                return response.body();
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return null;
    }

    public Observable<SwResults> getSwPeoplePage(int page) {
        return api.getSwPeopleListPage(page);
    }
}
