package com.grupo12.sw2p2movil.dato;

import static com.grupo12.sw2p2movil.util.Constantes.GRAPHQL_URL;



import org.json.JSONObject;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class Dlogin {

    public JSONObject login(String email, String password) {
        try {
            URL url = new URL(GRAPHQL_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            String query = "mutation Login { login(identifier: \\\"" + email + "\\\", password: \\\"" + password + "\\\") { jwt role doctorId patientId } }";
            String payload = "{\"query\": \"" + query + "\"}";

            OutputStream os = conn.getOutputStream();
            os.write(payload.getBytes());
            os.flush();

            Scanner in = new Scanner(conn.getInputStream());
            StringBuilder response = new StringBuilder();
            while (in.hasNextLine()) {
                response.append(in.nextLine());
            }
            in.close();

            return new JSONObject(response.toString());

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
