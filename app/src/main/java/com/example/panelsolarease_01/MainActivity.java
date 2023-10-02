package com.example.panelsolarease_01;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;

import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.time.LocalDate;

public class MainActivity extends AppCompatActivity {

    private EditText latitudeEditText;
    private EditText longitudeEditText;
    private EditText areaEditText;
    private SeekBar inclinationSeekBar;
    private TextView inclinationTextView;
    Button calculateButton;
    private TextView resultTextView;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        latitudeEditText = findViewById(R.id.latitude_edittext);
        longitudeEditText = findViewById(R.id.longitude_edittext);
        areaEditText = findViewById(R.id.area_edittext);
        inclinationSeekBar = findViewById(R.id.inclination_seekbar);
        inclinationTextView = findViewById(R.id.inclination_textview);
        resultTextView = findViewById(R.id.result_textview);

        inclinationSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {



            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                String string = getString(R.string.Inclinacion2);
                inclinationTextView.setText(string  + " " + progress + " ");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        calculateButton.setOnClickListener(view -> {
            String txLat = latitudeEditText.getText().toString();
            String txLog = longitudeEditText.getText().toString();
            String txArea = areaEditText.getText().toString();

            if (txLat.isEmpty() || txLog.isEmpty() || txArea.isEmpty()){
                Toast.makeText(getBaseContext(), "Todos los campos se deben diligenciar", Toast.LENGTH_LONG).show();
            }else {
                double lat = Double.parseDouble(txLat);
                double longitud = Double.parseDouble(txLog);
                double area = Double.parseDouble(txArea);
                int inclination = inclinationSeekBar.getProgress();
                double produccionEnergia = calcularProduccionEnergia(lat, longitud, area, inclination);
                resultTextView.setText("Producción de energía: " + produccionEnergia + " kWh");
            }
        });

    }

    private double calcularProduccionEnergia(double latitud, double longitud, double area, int inclinacion) {
        // Convertir latitud, longitud e inclinación a radianes
        double latitudRad = Math.toRadians(latitud);
        double longitudRad = Math.toRadians(longitud);
        double inclinacionRad = Math.toRadians(inclinacion);

        // Obtener día del año actual
        int diaDelAnio = LocalDate.now().getDayOfYear();

        // Calcular ángulo de incidencia de la radiación solar
        double anguloIncidencia = Math.acos(Math.sin(latitudRad) * Math.sin(inclinacionRad) + Math.cos(latitudRad) * Math.cos(inclinacionRad) * Math.cos(longitudRad));

        // Calcular radiación solar incidente
        final double CONSTANTE_SOLAR = 0.1367; // kWh/m²
        double radiacion = CONSTANTE_SOLAR * Math.cos(anguloIncidencia) * (1 + 0.033 * Math.cos(Math.toRadians(360 * diaDelAnio / 365.0)));

        // Calcular producción de energía
        double areaPanel = area / 10000.0; // convertir a m²
        double eficienciaPanel = 0.16; // 16% de eficiencia
        double factorPerdidas = 0.9; // pérdida del 10%
        double produccionEnergia = areaPanel * radiacion * eficienciaPanel * factorPerdidas;

        return produccionEnergia;
    }

    private double calcularProduccionEnergia(double latitud, double longitud, int areaInt, int inclinacion) {
        // Redondear inclinación al entero más cercano
        double area = Math.round(areaInt);

        // Llamar al otro método
        return calcularProduccionEnergia(latitud, longitud, area, inclinacion);
    }

}