package com.grupo12.sw2p2movil.presentacion;

import android.os.Bundle;
import android.os.StrictMode;
import androidx.appcompat.app.AppCompatActivity;
import com.github.barteksc.pdfviewer.PDFView;
import com.grupo12.sw2p2movil.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class PdocumentPreview extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdocument_preview);

        PDFView pdfView = findViewById(R.id.pdfView);

        // Obtén la URL del PDF enviada por Intent
        String pdfUrl = getIntent().getStringExtra("pdf_url");

        // Solo para pruebas rápidas, en producción usa AsyncTask o Thread
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        try {
            URL url = new URL(pdfUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            InputStream is = conn.getInputStream();

            File file = File.createTempFile("document", ".pdf", getCacheDir());
            FileOutputStream fos = new FileOutputStream(file);

            byte[] buffer = new byte[4096];
            int len;
            while ((len = is.read(buffer)) > 0) {
                fos.write(buffer, 0, len);
            }
            fos.close();
            is.close();

            // 2. Carga el PDF desde archivo, con zoom inicial alto
            pdfView.fromFile(file)
                    .enableSwipe(true)
                    .swipeHorizontal(false)
                    .enableDoubletap(true)
                    .spacing(8)
                    .onLoad(nbPages -> {
                        pdfView.zoomTo(1.5f);  // Zoom inicial: 1.5x
                        pdfView.moveTo(0, 0);
                    })
                    .load();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}