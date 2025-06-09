package com.grupo12.sw2p2movil.negocio;

import com.grupo12.sw2p2movil.dato.Dassistant;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

public class Nassistant {
    private final Dassistant dassistant;
    private final List<MensajeConversacion> historial;
    private static final String TAG = "Nassistant";

    public Nassistant() {
        this.dassistant = new Dassistant();
        this.historial = new ArrayList<>();
    }

    public AssistantResponse enviarPregunta(String input, String patientId, String jwt) {
        historial.add(new MensajeConversacion("usuario", input));
        String historialCompleto = getHistorialComoTexto();
        Log.i(TAG, "Enviando historial completo al asistente: " + historialCompleto);
        JSONObject response = dassistant.askAssistant(historialCompleto, patientId, jwt);


        if (response == null || !response.has("data")) {
            Log.e(TAG, "Respuesta nula o sin campo 'data': " + response);
            return new AssistantResponse("Error al conectar con el asistente", null);
        }

        try {
            JSONObject data = response.getJSONObject("data").getJSONObject("askAssistant");
            String message = data.optString("message", "Sin respuesta");
            Integer appointmentId = data.has("appointmentId") && !data.isNull("appointmentId")
                    ? data.getInt("appointmentId")
                    : null;

            Log.i(TAG, "Respuesta recibida del asistente: " + message);
            Log.i(TAG, "AppointmentId devuelto: " + appointmentId);

            historial.add(new MensajeConversacion("asistente", message));
            Log.d(TAG, "Historial actualizado: " + historial.size() + " mensajes");

            return new AssistantResponse(message, appointmentId);

        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "Error al interpretar la respuesta del asistente", e);
            return new AssistantResponse("Error al interpretar la respuesta del asistente", null);
        }
    }

    public void enviarPreguntaAsync(String input, String patientId, String jwt, ResultadoCallback callback) {
        new Thread(() -> {
            AssistantResponse resultado = enviarPregunta(input, patientId, jwt);
            callback.onResultado(resultado);
        }).start();
    }

    public interface ResultadoCallback {
        void onResultado(AssistantResponse respuesta);
    }

    public String getHistorialComoTexto() {
        StringBuilder sb = new StringBuilder();
        for (MensajeConversacion mensaje : historial) {
            sb.append(mensaje.autor).append(": ").append(mensaje.mensaje).append("\n");
        }
        return sb.toString().trim(); // Elimina el último salto de línea
    }


    public void limpiarHistorial() {
        historial.clear();
    }

    public List<MensajeConversacion> getHistorial() {
        return historial;
    }

    public static class MensajeConversacion {
        public String autor; // "usuario" o "asistente"
        public String mensaje;

        public MensajeConversacion(String autor, String mensaje) {
            this.autor = autor;
            this.mensaje = mensaje;
        }
    }

    public static class AssistantResponse {
        public String message;
        public Integer appointmentId;

        public AssistantResponse(String message, Integer appointmentId) {
            this.message = message;
            this.appointmentId = appointmentId;
        }
    }
}
