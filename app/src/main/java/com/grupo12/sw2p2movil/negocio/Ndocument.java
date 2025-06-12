package com.grupo12.sw2p2movil.negocio;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.grupo12.sw2p2movil.dato.Ddocument;

import org.json.JSONObject;

public class Ndocument {

    private final Ddocument ddocument = new Ddocument();

    public String solicitarHistorial(Context context) {
        try {
            SharedPreferences prefs = context.getSharedPreferences("session", Context.MODE_PRIVATE);
            String jwt = prefs.getString("jwt", null);
            int patientId = prefs.getInt("patientId", -1);

            if (jwt == null || patientId == -1) {
                Log.e("Ndocumento", "No hay sesión activa");
                return null;
            }

            JSONObject response = ddocument.solicitarHistorial(patientId, jwt);

            if (response != null && response.has("data")) {
                // Devuelve la ruta del PDF generado (que llega del backend)
                return response.getJSONObject("data").getString("generateMedicalRecordPdf");
            }
        } catch (Exception e) {
            Log.e("Ndocumento", "Error solicitando historial", e);
        }
        return null;
    }

}
