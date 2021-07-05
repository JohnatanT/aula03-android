package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity implements TextWatcher, View.OnClickListener, TextToSpeech.OnInitListener {

    private Spinner spinnerPeso;
    private Button btnShare, btnOuvir;
    private TextView tvRes;
    private EditText oper1, oper2;
    private String operador;
    private TextToSpeech ttsPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvRes = (TextView) findViewById(R.id.resultado);
        btnShare = (Button) findViewById(R.id.button);
        btnOuvir = (Button) findViewById(R.id.button2);
        oper1 = (EditText) findViewById(R.id.editTextTextPersonName);
        oper2 = (EditText) findViewById(R.id.editTextTextPersonName2);

        oper2.addTextChangedListener(this);
        btnShare.setOnClickListener(this);
        btnOuvir.setOnClickListener(this);

        Intent checkTTS = new Intent();
        checkTTS.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(checkTTS, 1122);

        //btnCalc.setOnClickListener(this);

        //Spinner spinner = (Spinner) findViewById(R.id.spinner);
        // Spinner click listener
        //spinner.setOnItemSelectedListener(this);

        //String[] operadores = getResources().getStringArray(R.array.lista_operadores);
        //ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, operadores);
        // Drop down layout style - list view with radio button
        //dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        //spinner.setAdapter(dataAdapter);
    }

//    @Override
//    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//        // On selecting a spinner item
//        operador = parent.getItemAtPosition(position).toString();
//
//        // Showing selected spinner item
//        Log.i("Item", operador);
//    }

//    @Override
//    public void onNothingSelected(AdapterView<?> parent) {
//
//    }

//    @Override
//    public void onClick(View view) {
//        if (view==btnCalc) {
//            String op1 = oper1.getText().toString();
//            String op2 = oper2.getText().toString();
//            Integer op1I = Integer.valueOf(op1);
//            Integer op2I = Integer.valueOf(op2);
//            Integer result = 0;
//
//            if (operador.equals("somar")) {
//                result = op1I + op2I;
//            }
//
//            if (operador.equals("subtrair")) {
//                result = op1I - op2I;
//            }
//
//            if (operador.equals("multiplicar")) {
//                result = op1I * op2I;
//            }
//
//            if (operador.equals("dividir")) {
//                result = op1I / op2I;
//            }
//
//            tvRes.setText(result.toString());
//        }
//    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        Double quantidadePessoas = 0.00;
        Double valor = 0.00;

        if (oper2.getText().toString() != "") {
            quantidadePessoas = Double.parseDouble(oper2.getText().toString());
        }
        if (oper1.getText().toString() != "") {
            valor = Double.parseDouble(oper1.getText().toString());
        }

        Double result = valor / quantidadePessoas;
        DecimalFormat df = new DecimalFormat("#.00");
        tvRes.setText("R$ " + df.format(result));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1122) {
            if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
                ttsPlayer = new TextToSpeech(this, this);
            } else {
                Intent installIntent = new Intent();
                installIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                startActivity(installIntent);
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (v==btnShare) {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT, "A conta dividida por pessoa deu: " + tvRes.getText().toString());
            startActivity(intent);
        }

        if (v==btnOuvir) {
            if (ttsPlayer != null) {
                ttsPlayer.speak("valor por pessoa: " + tvRes.getText().toString(), TextToSpeech.QUEUE_FLUSH, null, "ID1");
            }
        }
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            Toast.makeText(this, "TTS Ativado...", Toast.LENGTH_LONG).show();
        } else if (status == TextToSpeech.ERROR) {
            Toast.makeText(this, "Sem TTS Ativado...", Toast.LENGTH_LONG).show();
        }
    }
}