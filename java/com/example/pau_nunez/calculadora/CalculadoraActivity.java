package com.example.pau_nunez.calculadora;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.Image;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pau_nunez.calculadora.DataBase.DBHelper;

import org.mariuszgromada.math.mxparser.Expression;

import java.util.ArrayList;

public class CalculadoraActivity extends AppCompatActivity implements View.OnClickListener {

    String contingut;
    String answer;
    TextView pantalla;
    SharedPreferences sharedPref;

    DBHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculadora);

        sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        contingut = sharedPref.getString("contingut","");
        answer = sharedPref.getString("answer","");

        final Button b1 = (Button) findViewById(R.id.b1);
        final Button b2 = (Button) findViewById(R.id.b2);
        final Button b3 = (Button) findViewById(R.id.b3);
        final Button b4 = (Button) findViewById(R.id.b4);
        final Button b5 = (Button) findViewById(R.id.b5);
        final Button b6 = (Button) findViewById(R.id.b6);
        final Button b7 = (Button) findViewById(R.id.b7);
        final Button b8 = (Button) findViewById(R.id.b8);
        final Button b9 = (Button) findViewById(R.id.b9);
        final Button b0 = (Button) findViewById(R.id.b0);
        final Button bMes = (Button) findViewById(R.id.bMes);
        final Button bMenys = (Button) findViewById(R.id.bMenys);
        final Button bProducte = (Button) findViewById(R.id.bProducte);
        final Button bDivisio = (Button) findViewById(R.id.bDivisio);
        final Button bPunt = (Button) findViewById(R.id.bPunt);
        final Button bIgual = (Button) findViewById(R.id.bIgual);
        final ImageButton bTrucar = (ImageButton) findViewById(R.id.bTrucar);
        final ImageButton bClearAll = (ImageButton) findViewById(R.id.bClearAll);
        final ImageButton bEnrere = (ImageButton) findViewById(R.id.bEnrere);
        final Button bAns = (Button) findViewById(R.id.bAns);

        pantalla = (TextView) findViewById(R.id.pantalla);
        pantalla.setText(contingut);

        b1.setOnClickListener(this);
        b2.setOnClickListener(this);
        b3.setOnClickListener(this);
        b4.setOnClickListener(this);
        b5.setOnClickListener(this);
        b6.setOnClickListener(this);
        b7.setOnClickListener(this);
        b8.setOnClickListener(this);
        b9.setOnClickListener(this);
        b0.setOnClickListener(this);
        bMes.setOnClickListener(this);
        bMenys.setOnClickListener(this);
        bProducte.setOnClickListener(this);
        bDivisio.setOnClickListener(this);
        bPunt.setOnClickListener(this);

        /* DATABASE */
            helper = new DBHelper(getApplicationContext());
        ArrayList<String> users = helper.getAllRegisteredUsers();
        for (int i = 0; i < users.size(); ++i)
            Log.i("DataBase loop", "Traversing DB loop");


        /* OnClick Botó Igual */
        bIgual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Expression e = new Expression(contingut);
                Double resultat = e.calculate();
                String resultToShow = resultat.toString();
                String stringForNoti = contingut;

                if (resultToShow.charAt(resultToShow.length()-1) == '0')
                    resultToShow = resultToShow.substring(0,resultToShow.length()-2);

                if (!resultToShow.equals("NaN")) {
                    pantalla.setText(resultToShow);
                    answer = resultToShow;
                    contingut = "";
                }

                else {
                    contingut = "";
                    pantalla.setText(contingut);
                    answer = "";

                    //NotificationManager myNM = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    NotificationCompat.Builder mBuilder =
                            new NotificationCompat.Builder(getApplicationContext())
                            .setSmallIcon(R.drawable.notification_icon)
                            .setContentTitle("Expression error!")
                            .setContentText("There was an error in the expression: \n" + stringForNoti);
                    int mNotificationId = 001;
                    NotificationManager mNotifyMngr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                    mNotifyMngr.notify(mNotificationId,mBuilder.build());
                }
            }
        });

        /* OnClick Botó Trucar */
        bTrucar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean telfValid = true;
                String telf = pantalla.getText().toString();
                for (int i = 0; i < telf.length() && telfValid; i++) {
                    char c = telf.charAt(i);
                    if (c < '0' || c > '9')
                        telfValid = false;
                }
                if (telfValid) {
                    Intent iTrucar = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + telf));
                    startActivity(iTrucar);
                }
                else {
                    Snackbar.make(view, "Incorrect phone number, only digits!", Snackbar.LENGTH_LONG)
                            .setActionTextColor(Color.parseColor("#00d137"))
                            .setAction("OK", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    contingut = "";
                                    pantalla.setText(contingut);
                                }
                            })
                            .show();
                }
            }

        });

        /* OnClick Botó ClearAll */
        bClearAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                contingut = "";
                pantalla.setText(contingut);
            }
        });

        /* OnClick Boto Enrere */
        bEnrere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String auxiliar = pantalla.getText().toString();
                if (!auxiliar.equals("") && !auxiliar.equals("NaN")) {
                    auxiliar = auxiliar.substring(0, auxiliar.length()-1);
                    pantalla.setText(auxiliar);
                    contingut = auxiliar;
                }
            }
        });

        /* OnClick Botó Answer */
        bAns.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!answer.equals("")) {
                    contingut += answer;
                    pantalla.setText(contingut);
                }
                else {
                    Toast.makeText(getApplicationContext(), "ANS is empty, perform a calculation to give it a value.", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    /* OnClick botó en general - Fora del onCreate */
    @Override
    public void onClick(View view) {
        Button b = (Button) view;
        String buttonText = b.getText().toString();
        contingut = contingut + buttonText;
        pantalla.setText(contingut);
    }


    /* Salvar State de la App quan es gira la pantalla */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("contenido",pantalla.getText().toString());
        outState.putString("answer",answer);
    }

    /* Salvar State de la App quan es gira la pantalla */
    @Override
    protected void onRestoreInstanceState(Bundle inState) {
        super.onRestoreInstanceState(inState);
        pantalla.setText(inState.getString("contenido"));
        answer = inState.getString("answer");
        contingut = pantalla.getText().toString();
    }

    @Override
    protected void onDestroy() {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("contingut", contingut);
        editor.putString("answer", answer);
        editor.commit();
        super.onDestroy();
    }
}
