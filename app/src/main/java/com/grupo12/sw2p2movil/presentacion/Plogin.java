package com.grupo12.sw2p2movil.presentacion;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.grupo12.sw2p2movil.R;
import com.grupo12.sw2p2movil.negocio.Nlogin;

import org.json.JSONObject;

public class Plogin extends AppCompatActivity {

    EditText emailInput, passwordInput;
    Button loginBtn;
    Nlogin nlogin = new Nlogin();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final String savedJwt  = getSharedPreferences("auth", MODE_PRIVATE).getString("jwt", null);
        if (savedJwt  != null && !savedJwt .isEmpty()) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
            return;
        }

        setContentView(R.layout.activity_plogin);

        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        loginBtn = findViewById(R.id.loginBtn);

        loginBtn.setOnClickListener(v -> {
            String email = emailInput.getText().toString();
            String password = passwordInput.getText().toString();

            new Thread(() -> {
                boolean exito = nlogin.autenticar(email, password, Plogin.this);
                runOnUiThread(() -> {
                    if (exito) {
                        Toast.makeText(this, "Login exitoso", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(this, MainActivity.class));
                        finish();
                    } else {
                        Toast.makeText(this, "Credenciales inválidas o rol no permitido", Toast.LENGTH_SHORT).show();
                    }
                });
            }).start();
        });
    }
}