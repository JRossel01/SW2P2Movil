package com.grupo12.sw2p2movil.presentacion.Pappointment;

import android.os.Bundle;

import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.grupo12.sw2p2movil.R;
import com.grupo12.sw2p2movil.negocio.Nappointment.NindexAppointment;
import com.grupo12.sw2p2movil.negocio.Ndoctor.NdoctorIndex;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PindexAppointment extends AppCompatActivity {

    ListView listView;
    NindexAppointment nappointment = new NindexAppointment();
    NdoctorIndex ndoctor = new NdoctorIndex();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pindex_appointment);

        listView = findViewById(R.id.appointmentList);

        new Thread(() -> {
            JSONArray citas = nappointment.obtenerCitasPendientes(this);
            List<String> lista = new ArrayList<>();

            if (citas != null) {
                for (int i = 0; i < citas.length(); i++) {
                    JSONObject cita = citas.optJSONObject(i);
                    if (cita != null) {
                        int nro = cita.optInt("id", -1);
                        String fecha = cita.optString("date");
                        String hora = cita.optString("time");
                        String motivo = cita.optString("reason");

                        int doctorId = cita.optInt("doctorId", -1);
                        String doctorInfo = "Doctor desconocido";

                        if (doctorId != -1) {
                            JSONObject doctor = ndoctor.obtenerDoctorPorId(this, doctorId);
                            if (doctor != null) {
                                String nombre = doctor.optString("name", "Sin nombre");
                                String especialidad = doctor.optString("specialty", "Sin especialidad");
                                doctorInfo = "Doctor: " + nombre + "\nEspecialidad: " + especialidad + "";
                            }
                        }

                        String item = "Nro. " + nro + "\nFecha " + fecha + " - " + hora + "\n" + doctorInfo + "\nMotivo: " + motivo;
                        lista.add(item);
                    }
                }
            }

            runOnUiThread(() -> {
                if (lista.isEmpty()) {
                    Toast.makeText(this, "No hay citas pendientes", Toast.LENGTH_SHORT).show();
                } else {
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, lista);
                    listView.setAdapter(adapter);
                }
            });
        }).start();
    }
}