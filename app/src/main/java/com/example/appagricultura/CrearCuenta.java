package com.example.appagricultura;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class CrearCuenta extends AppCompatActivity {

    private Spinner spinner;
    private EditText cuenta, password;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_crear_cuenta);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        cuenta = (EditText)findViewById(R.id.cuenta);
        password = (EditText)findViewById(R.id.password);
        spinner = (Spinner)findViewById(R.id.spinner);
        String [] lugares = {"Huancayo", "Chupaca", "Concepcion", "Jauja", "Yauli", "Tarma", "Junin"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, lugares);
        spinner.setAdapter(adapter);
    }

    public void Registrar(View view){
        String dato_cuenta = cuenta.getText().toString();
        String dato_password = password.getText().toString();
        String respuesta;
        String ubicacion;
        int valor = 0;
        respuesta = Filtro(dato_cuenta, dato_password);
        if(respuesta == "e"){
            String opcion = spinner.getSelectedItem().toString();
            if(opcion.equals("Huancayo")){
                valor = 1;
            } else if (opcion.equals("Chupaca")) {
                valor = 2;
            } else if (opcion.equals("Concepcion")) {
                valor = 3;
            } else if (opcion.equals("Jauja")) {
                valor = 4;
            }else if(opcion.equals("Tarma")){
                valor = 5;
            } else if (opcion.equals("Yauli")) {
                valor = 6;
            } else if (opcion.equals("Junin")) {
                valor = 7;
            }else {
                Toast.makeText(this, "Error en Ubicación", Toast.LENGTH_SHORT).show();
            }
            postData(dato_cuenta, dato_password, valor);
        } else if (respuesta == "p") {
            Toast.makeText(this, "La contraseña debe tener minimo 8 caracteres.", Toast.LENGTH_SHORT).show();
        } else if (respuesta == "c") {
            Toast.makeText(this, "La cuenta debe tener minimo 5 caracteres.", Toast.LENGTH_SHORT).show();
        }
    }

    public String Filtro(String c, String p) {
        String respuesta = "";
        if(c.length() >= 5){
            if(p.length() >= 8){
                respuesta = "e";
            }
            else {
                respuesta = "p";
            }
        }
        else{
            respuesta = "c";
        }
        return respuesta;
    }



    public void postData(String cuenta, String password, int u) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL("http://10.0.2.2:8000/app/crear/");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/json; utf-8");
                    conn.setRequestProperty("Accept", "application/json");
                    conn.setDoOutput(true);

                    JSONObject json = new JSONObject();
                    json.put("cuenta", cuenta);
                    json.put("password", password);
                    json.put("u", u);
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

                        // Lógica basada en la respuesta, ejecutada en el hilo principal
                        // NOTA: Para actualizar la UI u otras tareas en el hilo principal, utiliza runOnUiThread() si estás dentro de una Activity:
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // Aquí puedes actualizar la UI o mostrar un diálogo, etc.
                                if(mensaje.equals("exito")){
                                    Toast.makeText(getApplicationContext(), "Cuenta creada exitosamente.", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(CrearCuenta.this, MainActivity.class);
                                    startActivity(intent);
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

}