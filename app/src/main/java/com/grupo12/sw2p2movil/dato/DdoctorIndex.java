package com.grupo12.sw2p2movil.dato;

import static com.grupo12.sw2p2movil.util.Constantes.GRAPHQL_URL;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class DdoctorIndex {
    public JSONObject obtenerDoctorPorId(Context context, int doctorId) {
        try {
            SharedPreferences prefs = context.getSharedPreferences("session", Context.MODE_PRIVATE);
            String jwt = prefs.getString("jwt", null);

            if (jwt == null) {
                Log.e("Ddoctor", "JWT no disponible");
                return null;
            }

            // Construir la query GraphQL
            String query = "query GetDoctorWithUserById($doctorId: Int!) { getDoctorWithUserById(doctorId: $doctorId) { name specialty } }";
            String jsonInput = new JSONObject()
                    .put("query", query)
                    .put("variables", new JSONObject().put("doctorId", doctorId))
                    .toString();

            // Hacer la conexión
            URL url = new URL(GRAPHQL_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", "Bearer " + jwt);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
            writer.write(jsonInput);
            writer.flush();
            writer.close();

            // Leer la respuesta
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            JSONObject jsonResponse = new JSONObject(response.toString());
            return jsonResponse.getJSONObject("data").getJSONObject("getDoctorWithUserById");

        } catch (Exception e) {
            Log.e("Ddoctor", "Error al obtener doctor", e);
            return null;
        }
    }
}
