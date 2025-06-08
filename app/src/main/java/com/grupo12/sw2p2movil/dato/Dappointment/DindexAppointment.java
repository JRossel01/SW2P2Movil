package com.grupo12.sw2p2movil.dato.Dappointment;

import static com.grupo12.sw2p2movil.util.Constantes.GRAPHQL_URL;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class DindexAppointment {
    public JSONArray obtenerCitasPendientes(Context context) {
        try {
            SharedPreferences prefs = context.getSharedPreferences("session", Context.MODE_PRIVATE);
            String jwt = prefs.getString("jwt", null);
            int patientId = prefs.getInt("patientId", -1);

            if (jwt == null || patientId == -1) {
                Log.e("Dappointment", "JWT o patientId no disponible");
                return null;
            }

            URL url = new URL(GRAPHQL_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", "Bearer " + jwt);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            String query = "query GetAppointmentsByPatient($patientId: Int!) { getAppointmentsByPatient(patientId: $patientId) { id date time status reason patientId doctorId } }";
            String jsonInput = new JSONObject()
                    .put("query", query)
                    .put("variables", new JSONObject().put("patientId", patientId))
                    .toString();

            OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
            writer.write(jsonInput);
            writer.flush();
            writer.close();

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            JSONObject jsonResponse = new JSONObject(response.toString());
            return jsonResponse.getJSONObject("data").getJSONArray("getAppointmentsByPatient");

        } catch (Exception e) {
            Log.e("Dappointment", "Error al obtener citas", e);
            return null;
        }
    }
}
