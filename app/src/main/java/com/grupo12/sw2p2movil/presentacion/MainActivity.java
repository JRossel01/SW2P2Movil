package com.grupo12.sw2p2movil.presentacion;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.grupo12.sw2p2movil.R;
import com.grupo12.sw2p2movil.negocio.Nlogout;
import com.grupo12.sw2p2movil.presentacion.Pappointment.PindexAppointment;

public class MainActivity extends AppCompatActivity {

    Button logoutBtn;
    Button appointmentsBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Obtener referencias a los elementos (opcional si usarás lógica luego)
        ImageView logo = findViewById(R.id.logoMain);
        TextView welcomeText = findViewById(R.id.welcomeText);
        logoutBtn = findViewById(R.id.logoutBtn);
        appointmentsBtn = findViewById(R.id.appointmentsBtn);

        // Ver citas pendientes
        appointmentsBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, PindexAppointment.class);
            startActivity(intent);
        });

        // Cerrar sesión
        logoutBtn.setOnClickListener(v -> {
            String jwt = getSharedPreferences("auth", MODE_PRIVATE).getString("jwt", null);
            Nlogout nlogout = new Nlogout();

            new Thread(() -> {
                boolean result = nlogout.cerrarSesion(jwt);
                runOnUiThread(() -> {
                    if (result) {
                        getSharedPreferences("auth", MODE_PRIVATE).edit().remove("jwt").apply();
                        Intent intent = new Intent(this, Plogin.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    } else {
                        Toast.makeText(this, "Error al cerrar sesión", Toast.LENGTH_SHORT).show();
                    }
                });
            }).start();
        });
    }
}