package com.sanvalero.entrenoexamenmayo.service;

import com.sanvalero.entrenoexamenmayo.domain.SwPeople;
import com.sanvalero.entrenoexamenmayo.domain.SwResults;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Creado por @ author: Pedro Orós
 * el 11/05/2021
 */
public interface SwApiService {

    @GET("people")
    Observable<SwResults> getSwPeopleList();

    @GET("people/{id}")
    Call<SwPeople> getSwCharacter(@Path("id") int id);

    @GET("people/")
    Observable<SwResults> getSwPeopleListPage(@Query("page") int page);
}
