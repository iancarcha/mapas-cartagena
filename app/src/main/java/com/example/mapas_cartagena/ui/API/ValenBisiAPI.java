package com.example.mapas_cartagena.ui.API;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ValenBisiAPI {
    @GET("valenbisi")
    Call<CitibikesResult> status();
}