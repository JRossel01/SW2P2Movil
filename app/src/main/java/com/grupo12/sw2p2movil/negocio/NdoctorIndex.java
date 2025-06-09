package com.grupo12.sw2p2movil.negocio;

import android.content.Context;
import android.util.Log;

import com.grupo12.sw2p2movil.dato.DdoctorIndex;

import org.json.JSONObject;

public class NdoctorIndex {

    private final DdoctorIndex ddoctor = new DdoctorIndex();

    public JSONObject obtenerDoctorPorId(Context context, int doctorId) {
        try {
            return ddoctor.obtenerDoctorPorId(context, doctorId);
        } catch (Exception e) {
            Log.e("NdoctorIndex", "Error al obtener doctor", e);
            return null;
        }
    }

}
