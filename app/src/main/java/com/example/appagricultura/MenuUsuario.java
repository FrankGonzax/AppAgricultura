package com.example.appagricultura;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.content.Intent;

public class MenuUsuario extends AppCompatActivity {
    private TextView tubicacion, ttemperatura, tresultadop, tresultadom;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_menu_usuario);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Intent intent = getIntent();
        String u = intent.getStringExtra("ubicacion");
        String t = intent.getStringExtra("temperatura");
        tubicacion = (TextView) findViewById(R.id.txtu);
        ttemperatura = (TextView) findViewById(R.id.txtt);
        tresultadop = (TextView)findViewById(R.id.txtrp);
        tresultadom = (TextView) findViewById(R.id.txtrm);
        tubicacion.setText(u);
        ttemperatura.setText(t + "°C");
        Recomendaciones(t);
    }

    public void Recomendaciones(String temp){
        double temNum = 0;
        temNum = Double.parseDouble(temp);
        String papa = "";
        String maiz = "";
        if(temNum < 10){
            maiz = "Riesgo alto para su crecimiento por frío.";
        } else if (temNum >= 10 && temNum < 15 ) {
            maiz = "Riesgo moderado, temperatura fría.";
        } else if (temNum >= 15 && temNum < 18) {
            maiz = "Bajo riesgo que afecte a los cultivos.";
        } else if (temNum >= 18 && temNum <= 30) {
            maiz = "Temperatura ideal para el desarrollo.";
        } else if (temNum >= 35) {
            maiz = "Riesgo por exceso de calor (estrés térmico).";
        }

        if(temNum < 7){
            papa = "Riesgo alto por alto índice en exceso de frío..";
        } else if (temNum >= 7 && temNum < 10 ) {
            papa = "Riesgo moderado, temperatura fría.";
        } else if (temNum >= 10 && temNum < 15) {
            papa = "Bajo índice de riesgo que afecte a los cultivos.";
        } else if (temNum >= 15 && temNum <= 20) {
            papa = "Temperatura ideal para el desarrollo.";
        } else if (temNum > 20 && temNum < 25) {
            papa = "Temperatura un poco alta, se tiene riesgo moderado.";
        } else if (temNum >= 25) {
            papa = "Riesgo alto por exceso de calor.";
        }
        tresultadop.setText(papa);
        tresultadom.setText(maiz);
    }

    public void Impacto(View v){
        Intent intent = new Intent(MenuUsuario.this, Impacto.class);
        startActivity(intent);
    }
}