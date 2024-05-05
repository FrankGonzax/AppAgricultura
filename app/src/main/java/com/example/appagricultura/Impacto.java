package com.example.appagricultura;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Impacto extends AppCompatActivity {
    private EditText temp;
    private RadioButton op1, op2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_impacto);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        temp = (EditText) findViewById(R.id.t);
        op1 = (RadioButton) findViewById(R.id.r1);
        op2 = (RadioButton) findViewById(R.id.r2);
    }

    public void Aceptar(View v){
        String t = temp.getText().toString();
        String tipo = "";
        if(op1.isChecked() == true ){
            tipo = "papa";
        } else if (op2.isChecked() == true) {
            tipo = "maiz";
        }else {
            tipo = "";
        }

        if (t.length() > 0 && tipo != ""){
            Intent intent = new Intent(Impacto.this, impactoRespuesta.class);
            intent.putExtra("temp", t);
            intent.putExtra("tipo", tipo);
            startActivity(intent);
        }else {
            Toast.makeText(getApplicationContext(), "Ingrese los datos correctamente.", Toast.LENGTH_SHORT).show();
        }


    }
}