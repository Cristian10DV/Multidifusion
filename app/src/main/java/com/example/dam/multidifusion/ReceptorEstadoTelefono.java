package com.example.dam.multidifusion;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ReceptorEstadoTelefono extends BroadcastReceiver {

    private String fecha = new SimpleDateFormat("dd/MM/yyyy - hh:mm:ss").format(new Date());
    private String tipo = "";

    /*@Override
    public void onReceive(final Context context, Intent intent) {
        final TelephonyManager tm = (TelephonyManager)
                context.getSystemService(Context.TELEPHONY_SERVICE);
        tm.listen(new PhoneStateListener() {
            public void onCallStateChanged(int state, String phoneNumber) {
                //Servicio: Ejecución larga de código sin interfaz de usuario.
                //Estado y número
                Log.v("xyzyx", state + " " + phoneNumber);
                Log.v("xyzyx", fecha);
                //esto estaria mal ya que manda muchos registros de una llamada y solo necesitamos uno
                Intent intent = new Intent(context, ServicioClienteRest.class);
                intent.putExtra("estado", state);
                intent.putExtra("numero", phoneNumber);
                intent.putExtra("fecha", fecha);
                intent.putExtra("tipo", tipo);
                //mandar tambien la fecha y la hora
                context.startService(intent);

            }
        }, PhoneStateListener.LISTEN_CALL_STATE);
    }*/

    private static int lastState = TelephonyManager.CALL_STATE_IDLE;
    private static Date callStartTime;
    private static boolean isIncoming;
    private static String savedNumber;


    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals("android.intent.action.NEW_OUTGOING_CALL")) {
            savedNumber = intent.getExtras().getString("android.intent.extra.PHONE_NUMBER");
        }
        else{
            String stateStr = intent.getExtras().getString(TelephonyManager.EXTRA_STATE);
            String number = intent.getExtras().getString(TelephonyManager.EXTRA_INCOMING_NUMBER);
            int state = 0;
            if(stateStr.equals(TelephonyManager.EXTRA_STATE_IDLE)){
                state = TelephonyManager.CALL_STATE_IDLE;
            }
            else if(stateStr.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)){
                state = TelephonyManager.CALL_STATE_OFFHOOK;
            }
            else if(stateStr.equals(TelephonyManager.EXTRA_STATE_RINGING)){
                state = TelephonyManager.CALL_STATE_RINGING;
            }

            onCallStateChanged(context, state, number);
        }
    }

    protected void onIncomingCallStarted(Context ctx, String number, Date start){}
    protected void onOutgoingCallStarted(Context ctx, String number, Date start){}
    protected void onIncomingCallEnded(Context ctx, String number, Date start, Date end){}
    protected void onOutgoingCallEnded(Context ctx, String number, Date start, Date end){}
    protected void onMissedCall(Context ctx, String number, Date start){}

    public void onCallStateChanged(Context context, int state, String number) {
        if(lastState == state){
            return;
        }
        switch (state) {
            case TelephonyManager.CALL_STATE_RINGING:
                isIncoming = true;
                callStartTime = new Date();
                savedNumber = number;
                onIncomingCallStarted(context, number, callStartTime);
                break;
            case TelephonyManager.CALL_STATE_OFFHOOK:
                //Transition of ringing->offhook are pickups of incoming calls.  Nothing done on them
                if(lastState != TelephonyManager.CALL_STATE_RINGING){
                    isIncoming = false;
                    callStartTime = new Date();
                    onOutgoingCallStarted(context, savedNumber, callStartTime);
                }
                break;
            case TelephonyManager.CALL_STATE_IDLE:
                //Went to idle-  this is the end of a call.  What type depends on previous state(s)
                if(lastState == TelephonyManager.CALL_STATE_RINGING){
                    //Ring but no pickup-  a miss
                    onMissedCall(context, savedNumber, callStartTime);
                    Log.v("xyzyx", "LLAMADA ENTRANTE");
                    Log.v("xyzyx", "----------------");
                    Log.v("xyzyx", "numero: " + number);
                    Log.v("xyzyx", "fecha: " + fecha);
                    tipo = "ENTRANTE";
                    Log.v("xyzyx", "Tipo de llamada: " + tipo);

                    //-----
                    Intent intent = new Intent(context, ServicioClienteRest.class);
                    intent.putExtra("numero", number);
                    intent.putExtra("fecha", fecha);
                    intent.putExtra("tipo", tipo);
                    context.startService(intent);
                }
                else if(isIncoming){
                    onIncomingCallEnded(context, savedNumber, callStartTime, new Date());
                    Log.v("xyzyx", "LLAMADA PERDIDA");
                    Log.v("xyzyx", "----------------");
                    Log.v("xyzyx", "numero: " + number);
                    Log.v("xyzyx", "fecha: " + fecha);
                    tipo = "PERDIDA";
                    Log.v("xyzyx", "Tipo de llamada: " + tipo);
                    //-----
                    Intent intent = new Intent(context, ServicioClienteRest.class);
                    intent.putExtra("numero", number);
                    intent.putExtra("fecha", fecha);
                    intent.putExtra("tipo", tipo);
                    context.startService(intent);
                }
                else{
                    onOutgoingCallEnded(context, savedNumber, callStartTime, new Date());
                    Log.v("xyzyx", "LLAMADA SALIENTE");
                    Log.v("xyzyx", "----------------");
                    Log.v("xyzyx", "numero: " + number);
                    Log.v("xyzyx", "fecha: " + fecha);
                    tipo = "SALIENTE";
                    Log.v("xyzyx", "Tipo de llamada: " + tipo);
                    //-----
                    Intent intent = new Intent(context, ServicioClienteRest.class);
                    intent.putExtra("numero", number);
                    intent.putExtra("fecha", fecha);
                    intent.putExtra("tipo", tipo);
                    context.startService(intent);
                }
                break;
        }
        lastState = state;
    }

}
