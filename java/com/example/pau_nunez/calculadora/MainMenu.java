package com.example.pau_nunez.calculadora;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MainMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        final Button bCalculator = (Button) findViewById(R.id.bCalcultor);
        final Button bMemoryGame = (Button) findViewById(R.id.bMemoryGame);
        final Button bMusicPlayer = (Button) findViewById(R.id.bMusicPlayer);
        final Button bLogout = (Button) findViewById(R.id.bLogout);
        final TextView textInfoLogin = (TextView) findViewById(R.id.textInfoLogin);

        Intent i = getIntent();
        final String playerName = i.getStringExtra("Username");

        textInfoLogin.setText("You are currently logged in as " + playerName);

        /* Botó Calculadora -> Calculadora Activity */
        bCalculator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),CalculadoraActivity.class);
                startActivity(i);
            }
        });

        /* Botó MemoryGame -> MemoryGame Activity */
        bMemoryGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ii = new Intent(getApplicationContext(),MemoryGame.class);
                ii.putExtra("PlayerName", playerName);
                startActivity(ii);
            }
        });

        /* Botó MusicPlayer -> MusicPlayer Activity */
        bMusicPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),MusicPlayer.class);
                startActivity(i);
            }
        });

        /* Botó Logout*/
        bLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent iii = new Intent(getApplicationContext(),Login.class);
                startActivity(iii);
            }
        });
    }
}
