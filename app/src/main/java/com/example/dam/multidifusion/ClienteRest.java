package com.example.dam.multidifusion;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface ClienteRest {
    //https://.../persona - GET -> listado personas
    @GET("persona")
    Call<ArrayList<Telefono>> getPersonas();

    //https://.../persona - POST - telefono -> telefono
    @POST("telefono")
    Call<Telefono> postPersona(@Body Telefono telefono);

    //https://.../persona - POST - telefono -> telefono
    @PUT("telefono")
    Call<Telefono> putPersona(@Body Telefono telefono);

    //https://.../persona - POST - persona -> persona
    //@DELETE("persona")
    //Call<Telefono> deletePersona(@Body Telefono persona);
}
