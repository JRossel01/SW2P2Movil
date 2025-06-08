package com.grupo12.sw2p2movil.dato;


import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import static com.grupo12.sw2p2movil.util.Constantes.GRAPHQL_URL;

public class Dlogout {

    public boolean logout(String jwt) {
        try {
            URL url = new URL(GRAPHQL_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", "Bearer " + jwt);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            String payload = String.format("{\"query\": \"mutation { logout { message } }\"}");
            OutputStream os = conn.getOutputStream();
            os.write(payload.getBytes());
            os.flush();

            Scanner in = new Scanner(conn.getInputStream());
            StringBuilder response = new StringBuilder();
            while (in.hasNextLine()) {
                response.append(in.nextLine());
            }
            in.close();

            return response.toString().contains("message");

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
