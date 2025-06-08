package com.grupo12.sw2p2movil.negocio;


import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.grupo12.sw2p2movil.dato.Dlogin;
import org.json.JSONObject;

public class Nlogin {
    private final Dlogin dlogin = new Dlogin();

    public boolean autenticar (String email, String password, Context context) {
        try {
            JSONObject response = dlogin.login(email, password);

            if (response != null && response.has("data")) {
                JSONObject loginData = response.getJSONObject("data").getJSONObject("login");
                String role = loginData.getString("role");

                if ("PATIENT".equalsIgnoreCase(role)) {
                    String jwt = loginData.getString("jwt");
                    int patientId = loginData.getInt("patientId");

                    SharedPreferences prefs = context.getSharedPreferences("session", Context.MODE_PRIVATE);
                    prefs.edit().putString("jwt", jwt).putInt("patientId", patientId).apply();
                    //Obtener patientId con:
                    // int patientId = getSharedPreferences("session", MODE_PRIVATE).getInt("patientId", -1);

                    return true;
                } else {
                    Log.w("Nlogin", "El rol no es paciente: " + role);
                }
            }
        } catch (Exception e) {
            Log.e("Nlogin", "Error al procesar la respuesta JSON", e);
        }

        return false;
    }

}
