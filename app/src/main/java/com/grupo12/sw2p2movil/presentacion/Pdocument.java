package com.grupo12.sw2p2movil.presentacion;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.grupo12.sw2p2movil.R;
import com.grupo12.sw2p2movil.negocio.Ndocument;
import com.grupo12.sw2p2movil.util.Constantes;


public class Pdocument extends AppCompatActivity {

    Ndocument ndocumento = new Ndocument();
    String ultimaRutaPdf = null; // Guarda la última ruta obtenida

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdocument);

        Button btnSolicitarHistorial = findViewById(R.id.btnSolicitarHistorial);
        Button btnVerHistorial = findViewById(R.id.btnVerHistorial); // Nuevo botón para ver PDF

        btnSolicitarHistorial.setOnClickListener(v -> {
            new Thread(() -> {
                String resultado = ndocumento.solicitarHistorial(Pdocument.this);
                runOnUiThread(() -> {
                    if (resultado != null) {
                        ultimaRutaPdf = Constantes.BASE_URL + resultado;
                        Toast.makeText(this, "Historial generado, puedes previsualizarlo.", Toast.LENGTH_LONG).show();
                        btnVerHistorial.setEnabled(true);
                        btnVerHistorial.setAlpha(1f);
                    } else {
                        Toast.makeText(this, "No se pudo solicitar el historial.", Toast.LENGTH_SHORT).show();
                    }
                });
            }).start();
        });

        btnVerHistorial.setOnClickListener(v -> {
            if (ultimaRutaPdf != null) {
                Intent intent = new Intent(Pdocument.this, PdocumentPreview.class);
                intent.putExtra("pdf_url", ultimaRutaPdf);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Solicita primero tu historial.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}