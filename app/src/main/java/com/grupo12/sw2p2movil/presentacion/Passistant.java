package com.grupo12.sw2p2movil.presentacion;

import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.content.Intent;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.grupo12.sw2p2movil.R;
import com.grupo12.sw2p2movil.negocio.Nassistant;
import com.grupo12.sw2p2movil.negocio.Nassistant.AssistantResponse;
import com.grupo12.sw2p2movil.negocio.Nassistant.MensajeConversacion;
import com.grupo12.sw2p2movil.util.MensajeAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Passistant extends AppCompatActivity {

    private EditText inputMensaje;
    private ImageButton btnVoz;
    private Button btnEnviar;
    private ListView listaMensajes;
    private MensajeAdapter adapter;


    private Nassistant nassistant;
    private String jwt;
    private String patientId;

    private static final int REQ_CODE_SPEECH_INPUT = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passistant);

        inputMensaje = findViewById(R.id.inputMensaje);
        btnVoz = findViewById(R.id.btnVoz);
        btnEnviar = findViewById(R.id.btnEnviar);
        listaMensajes = findViewById(R.id.listaMensajes);

        nassistant = new Nassistant();
        adapter = new MensajeAdapter(this, nassistant.getHistorial());
        listaMensajes.setAdapter(adapter);

        jwt = getSharedPreferences("session", MODE_PRIVATE).getString("jwt", "");
        patientId = String.valueOf(getSharedPreferences("session", MODE_PRIVATE).getInt("patientId", -1));

        if (nassistant.getHistorial().isEmpty()) {
            nassistant.getHistorial().add(new MensajeConversacion("asistente", "Hola, ¿cuál es tu problema de salud?"));
            adapter.notifyDataSetChanged();
        }


        btnEnviar.setOnClickListener(v -> enviarMensaje());

        btnVoz.setOnClickListener(v -> iniciarEntradaVoz());
    }

    private void enviarMensaje() {
        String mensaje = inputMensaje.getText().toString().trim();
        if (mensaje.isEmpty()) {
            Toast.makeText(this, "Escribe o dicta un mensaje", Toast.LENGTH_SHORT).show();
            Log.d("Passistant", "Mensaje enviado: " + mensaje);
            return;
        }

        btnEnviar.setEnabled(false);
        btnEnviar.setText("Procesando...");
        inputMensaje.setEnabled(false);
        btnVoz.setEnabled(false);

        nassistant.enviarPreguntaAsync(mensaje, patientId, jwt, respuesta -> runOnUiThread(() -> {
            refrescarHistorial();
            if (respuesta.appointmentId != null) {
                Toast.makeText(this, "Cita registrada con ID: " + respuesta.appointmentId, Toast.LENGTH_LONG).show();

                nassistant.getHistorial().add(new MensajeConversacion("asistente", "Gracias por usar el asistente. Regresando al inicio..."));
                refrescarHistorial();

                new android.os.Handler().postDelayed(() -> {
                    Intent intent = new Intent(Passistant.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }, 3000); // 3 segundos
            }
            inputMensaje.setText("");

            btnEnviar.setEnabled(true);
            btnEnviar.setText("Enviar");
            inputMensaje.setEnabled(true);
            btnVoz.setEnabled(true);
        }));
    }

    private void refrescarHistorial() {
        Log.d("Passistant", "Historial actualizado con " + nassistant.getHistorial().size() + " mensajes.");
        adapter.notifyDataSetChanged();
        listaMensajes.post(() -> listaMensajes.setSelection(adapter.getCount() - 1));
    }

    private void iniciarEntradaVoz() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Habla ahora...");

        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (Exception e) {
            Toast.makeText(this, "No se pudo iniciar la entrada por voz", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQ_CODE_SPEECH_INPUT && resultCode == RESULT_OK && data != null) {
            ArrayList<String> resultado = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (resultado != null && !resultado.isEmpty()) {
                inputMensaje.setText(resultado.get(0));
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        nassistant.limpiarHistorial();
    }
}