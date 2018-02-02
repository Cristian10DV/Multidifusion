package com.example.dam.multidifusion;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServicioClienteRest extends Service {
    public ServicioClienteRest() {
        //1, una vez
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //2, una vez
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //3, muchas veces
        //START_NOT_STICKY, START_REDELIVER_INTENT, START_STICKY
        String numero = intent.getStringExtra("numero");
        String fecha = intent.getStringExtra("fecha");
        String tipo = intent.getStringExtra("tipo");
        //1, creo el objeto retrofit
        Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl("https://ios-cristian10dv.c9users.io/")
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
        //2, creo un objeto cliente de la interfaz
        ClienteRest cliente = retrofit.create(ClienteRest.class);

        //3, objeto Call para hacer la llamada
        Call<Telefono> call = cliente.postPersona(new Telefono(numero, fecha, tipo));
        //4, encolar la petición
        call.enqueue(new Callback<Telefono>() {
            @Override
            public void onResponse(Call<Telefono> call, Response<Telefono> response) {
                Telefono p = response.body();
                Log.v("xyzyx", "Telefono: " + p.toString());
            }
            @Override
            public void onFailure(Call<Telefono> call, Throwable t) {
                //t.getLocalizedMessage()
                Log.v("xyzyx", "Telefono: " + t.getLocalizedMessage());

            }
        });

        Call<ArrayList<Telefono>> call2 = cliente.getPersonas();

        call2.enqueue(new Callback<ArrayList<Telefono>>() {
            @Override
            public void onResponse(Call<ArrayList<Telefono>> call, Response<ArrayList<Telefono>> response) {
                ArrayList<Telefono> ap = response.body();
                Log.v("xyzyx", "Personas: " + ap.toString());
            }

            @Override
            public void onFailure(Call<ArrayList<Telefono>> call, Throwable t) {
                Log.v("xyzyx", "Telefono: " + t.getLocalizedMessage());
            }
        });

        return START_REDELIVER_INTENT;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
