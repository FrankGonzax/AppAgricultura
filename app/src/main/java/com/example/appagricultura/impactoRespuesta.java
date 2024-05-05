package com.example.appagricultura;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class impactoRespuesta extends AppCompatActivity {
    private TextView t, tipo, op1, op2, op3, op4, r1, r2, r3, r4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_impacto_respuesta);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        t = (TextView) findViewById(R.id.temperatura);
        tipo = (TextView) findViewById(R.id.tipo);
        op1 = (TextView) findViewById(R.id.opcion1);
        op2 = (TextView) findViewById(R.id.opcion2);
        op3 = (TextView) findViewById(R.id.opcion3);
        op4 = (TextView) findViewById(R.id.opcion4);
        r1 = (TextView)  findViewById(R.id.respuesta1);
        r2 = (TextView)  findViewById(R.id.respuesta2);
        r3 = (TextView)  findViewById(R.id.respuesta3);
        r4 = (TextView)  findViewById(R.id.respuesta4);

        // Dentro del método onCreate() u otro método donde quieras recibir los datos
        Intent intent = getIntent();
        String temper = intent.getStringExtra("temp");
        String type = intent.getStringExtra("tipo");

        t.setText(temper);
        tipo.setText(type);

        if(type.equals("maiz")){
            op1.setText("Germinación:");
            r1.setText("El maíz, siendo un cultivo más tolerante al calor, requiere temperaturas del suelo de al menos 10°C para germinar, pero las temperaturas óptimas de germinación están entre 18°C y 30°C. Temperaturas por debajo de 10°C pueden causar una germinación pobre y aumentar el riesgo de enfermedades fúngicas.");
            op2.setText("Crecimiento Vegetativo:");
            r2.setText("Durante la fase vegetativa, el maíz prospera en temperaturas diurnas de 20°C a 30°C. Temperaturas superiores a 35°C, especialmente combinadas con baja humedad y alta radiación solar, pueden causar estrés térmico que limita la absorción de agua y nutrientes, afectando el crecimiento.");
            op3.setText("Floración y Formación de Granos:");
            r3.setText("Las temperaturas óptimas durante la floración y la polinización son cruciales para el maíz. Si las temperaturas exceden los 35°C durante este período, puede haber una reducción significativa en la fertilización de los óvulos, lo que conduce a una mala formación de los granos y reduce el rendimiento.");
            op4.setText("Maduración:");
            r4.setText("Para la maduración, el maíz necesita menos calor que durante la floración. Las temperaturas elevadas cerca de la cosecha pueden acelerar la maduración pero reducir la calidad del grano.");
        } else if (type.equals("papa")) {
            op1.setText("Germinación y Crecimiento Inicial:");
            r1.setText("La papa es generalmente un cultivo de clima templado-frío. Las temperaturas óptimas para su germinación y crecimiento inicial se sitúan entre 12°C y 16°C. Temperaturas muy bajas pueden retardar la germinación y el crecimiento inicial, mientras que temperaturas altas pueden inhibir estos procesos.");
            op2.setText("Desarrollo de Tubérculos:");
            r2.setText("La formación de tubérculos ocurre mejor a temperaturas del suelo de entre 15°C y 20°C. Si las temperaturas exceden los 25°C, especialmente durante la noche, la producción de tubérculos puede disminuir significativamente, afectando tanto al rendimiento como a la calidad.");
            op3.setText("Maduración y Cosecha:");
            r3.setText("Durante la maduración, temperaturas moderadamente frescas son favorables. Temperaturas demasiado altas hacia el final del ciclo de crecimiento pueden provocar que los tubérculos sean más pequeños y con menos almidón.");
            op4.setText("");
            r4.setText("");
        }

    }
}