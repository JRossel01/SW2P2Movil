package com.grupo12.sw2p2movil.negocio.Nappointment;

import android.content.Context;

import com.grupo12.sw2p2movil.dato.Dappointment.DindexAppointment;

import org.json.JSONArray;

public class NindexAppointment {
    private final DindexAppointment dappointment = new DindexAppointment();

    public JSONArray obtenerCitasPendientes(Context context) {
        JSONArray citas = dappointment.obtenerCitasPendientes(context);
        JSONArray citasFiltradas = new JSONArray();

        if (citas != null) {
            for (int i = 0; i < citas.length(); i++) {
                org.json.JSONObject cita = citas.optJSONObject(i);
                if (cita != null && "pending".equalsIgnoreCase(cita.optString("status"))) {
                    citasFiltradas.put(cita);
                }
            }
        }

        return citasFiltradas;
    }
}
