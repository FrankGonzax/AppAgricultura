package com.example.appagricultura;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.FeatureInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private EditText cuenta, password;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        cuenta = (EditText) findViewById(R.id.data_cuenta);
        password = (EditText) findViewById(R.id.data_password);
    }

    public void AbrirCrearCuenta(View v){
        Intent intent = new Intent(MainActivity.this, CrearCuenta.class);
        startActivity(intent);
    }

    public void InicioSesion(View v){
        String cuenta_user = "";
        String password_user = "";
        cuenta_user = cuenta.getText().toString();
        password_user = password.getText().toString();
        postData(cuenta_user, password_user);
        cuenta.setText("");
        password.setText("");
    }

    public void postData(String cuenta, String password) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL("http://10.0.2.2:8000/app/iniciarSesion/");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/json; utf-8");
                    conn.setRequestProperty("Accept", "application/json");
                    conn.setDoOutput(true);

                    JSONObject json = new JSONObject();
                    json.put("cuenta", cuenta);
                    json.put("password", password);
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
                        String temperatura = "";
                        String ubicacion = "";
                        if(mensaje.equals("exito")){
                            temperatura = jsonResponse.getString("temperatura");
                            ubicacion = jsonResponse.getString("ubicacion");
                        }
                        // Lógica basada en la respuesta, ejecutada en el hilo principal
                        // NOTA: Para actualizar la UI u otras tareas en el hilo principal, utiliza runOnUiThread() si estás dentro de una Activity:
                        String finalTemperatura = temperatura;
                        String finalUbicacion = ubicacion;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // Aquí puedes actualizar la UI o mostrar un diálogo, etc.
                                if(mensaje.equals("exito")){
                                    Toast.makeText(getApplicationContext(), "Bienvenido", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(MainActivity.this, MenuUsuario.class);
                                    intent.putExtra("temperatura", finalTemperatura);
                                    intent.putExtra("ubicacion", finalUbicacion);
                                    startActivity(intent);
                                } else if (mensaje.equals("fallo")) {
                                    Toast.makeText(getApplicationContext(), "Datos incorrectos, por favor vuelva a intentar", Toast.LENGTH_SHORT).show();
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