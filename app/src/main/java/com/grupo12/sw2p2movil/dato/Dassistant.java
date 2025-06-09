package com.grupo12.sw2p2movil.dato;

import static com.grupo12.sw2p2movil.util.Constantes.GRAPHQL_URL;

import android.util.Log;

import org.json.JSONObject;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class Dassistant {

    public JSONObject askAssistant(String input, String patientId, String jwt) {
        try {
            URL url = new URL(GRAPHQL_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Authorization", "Bearer " + jwt);
            conn.setDoOutput(true);

            // Escapar correctamente el texto multilínea para GraphQL
            String escapedInput = input
                    .replaceAll("[\"\\\\\\n\\r\\t]", " ")  // elimina ", \, saltos de línea y tabulaciones
                    .replaceAll("\\s+", " ")              // colapsa múltiples espacios en uno solo
                    .trim();                              // elimina espacios al inicio y fin

            String query = "mutation AskAssistant { askAssistant(input: \"" + escapedInput + "\", patientId: \"" + patientId + "\") { message appointmentId } }";
            JSONObject json = new JSONObject();
            json.put("query", query);
            Log.d("Dassistant", "QUERY limpia: " + query);
            String payload = json.toString();
            Log.d("Dassistant", "Payload enviado: " + payload);


            OutputStream os = conn.getOutputStream();
            os.write(payload.getBytes());
            os.flush();

            Scanner in = new Scanner(conn.getInputStream());
            StringBuilder response = new StringBuilder();
            while (in.hasNextLine()) {
                response.append(in.nextLine());
            }
            in.close();
            Log.d("Dassistant", "Respuesta recibida: " + response.toString());

            return new JSONObject(response.toString());

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
