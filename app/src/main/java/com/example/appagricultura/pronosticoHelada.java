package com.example.appagricultura;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class pronosticoHelada extends AppCompatActivity {

    private List<String> values = Arrays.asList("Humedad", "Temperatura", "2dsadsa", "dsdasda");
    private TextView tx;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_pronostico_helada);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        tx = findViewById(R.id.pre);
        postData("1");

    }

    public void otro(String temp, String hum, String prediccion){
        float max = 0f;
        int predic = Integer.parseInt(prediccion);
        if (predic == 1){
            tx.setText("Hay presencia de helada.");
        }
        else {
            tx.setText("No hay presencia de helada.");
        }
        float temperatura = Float.parseFloat(temp);
        float humedad = Float.parseFloat(hum);
        if(temperatura > humedad){
            if(temperatura <= 20){
                max = 30f;
            } else if (temperatura > 20 && temperatura <= 40) {
                max = 50f;
            } else if (temperatura > 40 && temperatura <= 70) {
                max = 80f;
            } else if (temperatura > 70 && temperatura <= 100) {
                max = 100f;
            } else {
                max = 200f;
            }
        } else {
            if(humedad <= 20){
                max = 30f;
            } else if (humedad > 20 && humedad <= 40) {
                max = 50f;
            } else if (humedad > 40 && humedad <= 70) {
                max = 80f;
            } else if (humedad > 70 && humedad <= 100) {
                max = 100f;
            } else {
                max = 200f;
            }
        }
        List<String> values2 = Arrays.asList("Humedad", "Temperatura");
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) BarChart barChart = findViewById(R.id.chart);
        barChart.getAxisRight().setDrawLabels(false);
        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry( 0, humedad));
        entries.add(new BarEntry(1, temperatura));
        YAxis yAxis = barChart.getAxisLeft();
        yAxis.setAxisMaximum(0f);
        yAxis.setAxisMaximum(max);
        yAxis.setAxisLineWidth(2f);
        yAxis.setAxisLineColor(Color.BLACK);
        yAxis.setLabelCount(10);
        BarDataSet dataSet = new BarDataSet(entries, "Subjects");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);

        BarData bardata = new BarData(dataSet);
        barChart.setData(bardata);

        barChart.getDescription().setEnabled(false);
        barChart.invalidate();

        barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(values2));
        barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        barChart.getXAxis().setGranularity(1f);
        barChart.getXAxis().setGranularityEnabled(true);
    }

    public void postData(String dia) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL("http://10.0.2.2:8000/app/heladas/");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/json; utf-8");
                    conn.setRequestProperty("Accept", "application/json");
                    conn.setDoOutput(true);

                    JSONObject json = new JSONObject();
                    json.put("dia", dia);
                    try (OutputStream os = conn.getOutputStream()) {
                        byte[] input = json.toString().getBytes("utf-8");
                        os.write(input, 0, input.length);
                    }

                    try (BufferedReader br = new BufferedReader(
                            new InputStreamReader(conn.getInputStream(), "utf-8"))) {
                        StringBuilder response = new StringBuilder();
                        String responseLine = null;
                        while ((responseLine = br.readLine()) != null) {
                            response.append(responseLine.trim());
                        }
                        final String responseStr = response.toString();
                        System.out.println(responseStr);

                        // Parsear la respuesta JSON
                        JSONObject jsonResponse = new JSONObject(responseStr);

                        // Ejemplo de cómo acceder a los valores de la respuesta
                        String mensaje = jsonResponse.getString("mensaje");
                        String temperatura = jsonResponse.getString("temperatura");
                        String humedad = jsonResponse.getString("humedad");
                        String prediccion = jsonResponse.getString("prediccion");

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // Aquí puedes actualizar la UI o mostrar un diálogo, etc.
                                if(mensaje.equals("exito")){
                                    otro(temperatura, humedad, prediccion);
                                } else if (mensaje.equals("fallo")) {
                                    Toast.makeText(getApplicationContext(), "La cuenta, ya existe, por favor use otro nombre.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                    conn.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }
    public void hoy(View v){
        postData("1");
    }
    public void dia1(View v){
        postData("2");
    }
    public void dia2(View v){
        postData("3");
    }
    public void dia3(View v){
        postData("4");
    }






}