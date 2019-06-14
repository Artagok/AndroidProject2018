package com.example.pau_nunez.calculadora;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.pau_nunez.calculadora.DataBase.DBHelper;
import com.example.pau_nunez.calculadora.DataBase.DBPuntuacioRow;
import com.example.pau_nunez.calculadora.DataBase.DataBasePuntuacionsContract;

import org.w3c.dom.Text;

import java.security.Identity;
import java.security.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.TimeZone;

public class MemoryGame extends AppCompatActivity implements View.OnClickListener {

    TextView textSegons;
    ArrayList<String> pokeArray;    /* Nom dels 6 Pokemons (duplicats) */
    Boolean gameFinished;           /* Represents whether or not the game has ended */
    int flippedCards;               /* Flipped cards at the moment {0,1} */
    int completedPairs;             /* Already matched pairs of cards {0, ..., 6} */
    String nomCarta1, nomCarta2;    /* Nom del pokemon que cal pintar a la carta */
    ImageButton ib1, ib2;           /* Image buttons per la parella de cartes */
    boolean clickable;              /* Determina si les 12 cartes són clickables o no*/
    CountSeconds cs;                /* Timer */
    Button bStart;                  /* Botó Start */
    ImageButton bScoreboard;        /* Botó Scoreboard */
    String playerName;              /* Name of the person playing */
    DBHelper dbh;                   /* BD */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memory_game);

        dbh = new DBHelper(getApplicationContext());
        textSegons = (TextView) findViewById(R.id.textSegons);
                          bStart = (Button) findViewById(R.id.bStart);
                          bScoreboard = (ImageButton) findViewById(R.id.bScoreboard);
        final ImageButton bCard1 = (ImageButton) findViewById(R.id.bCard1);
        final ImageButton bCard2 = (ImageButton) findViewById(R.id.bCard2);
        final ImageButton bCard3 = (ImageButton) findViewById(R.id.bCard3);
        final ImageButton bCard4 = (ImageButton) findViewById(R.id.bCard4);
        final ImageButton bCard5 = (ImageButton) findViewById(R.id.bCard5);
        final ImageButton bCard6 = (ImageButton) findViewById(R.id.bCard6);
        final ImageButton bCard7 = (ImageButton) findViewById(R.id.bCard7);
        final ImageButton bCard8 = (ImageButton) findViewById(R.id.bCard8);
        final ImageButton bCard9 = (ImageButton) findViewById(R.id.bCard9);
        final ImageButton bCard10 = (ImageButton) findViewById(R.id.bCard10);
        final ImageButton bCard11 = (ImageButton) findViewById(R.id.bCard11);
        final ImageButton bCard12 = (ImageButton) findViewById(R.id.bCard12);

        bCard1.setOnClickListener(this);
        bCard2.setOnClickListener(this);
        bCard3.setOnClickListener(this);
        bCard4.setOnClickListener(this);
        bCard5.setOnClickListener(this);
        bCard6.setOnClickListener(this);
        bCard7.setOnClickListener(this);
        bCard8.setOnClickListener(this);
        bCard9.setOnClickListener(this);
        bCard10.setOnClickListener(this);
        bCard11.setOnClickListener(this);
        bCard12.setOnClickListener(this);

        pokeArray = new ArrayList<>();
        pokeArray.add("articuno");
        pokeArray.add("articuno");
        pokeArray.add("zapdos");
        pokeArray.add("zapdos");
        pokeArray.add("moltres");
        pokeArray.add("moltres");
        pokeArray.add("charizard");
        pokeArray.add("charizard");
        pokeArray.add("blastoise");
        pokeArray.add("blastoise");
        pokeArray.add("venusaur");
        pokeArray.add("venusaur");
        Collections.shuffle(pokeArray);


        /* Botó START */
        bStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flippedCards = 0;
                completedPairs = 0;
                gameFinished = false;
                clickable = true;
                bStart.setClickable(false);
                bStart.setText("Running...");
                bStart.setBackgroundColor(Color.parseColor("#e50000"));
                cs = new CountSeconds();
                cs.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        });


        /* Botó SCOREBOARD */
        bScoreboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String formattedScoreboardString = "";
                ArrayList<DBPuntuacioRow> tablePuntuacio = dbh.getPuntuacionsMemoryGame();
                for (int i = 0; i < tablePuntuacio.size(); ++i) {
                    formattedScoreboardString += tablePuntuacio.get(i).getName();
                    formattedScoreboardString += "  ";
                    formattedScoreboardString += tablePuntuacio.get(i).getPoints();
                    formattedScoreboardString += "  ";
                    formattedScoreboardString += formatDate(Long.valueOf(tablePuntuacio.get(i).getTime()));
                    formattedScoreboardString += "\n";
                }

                AlertDialog.Builder builder;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new AlertDialog.Builder(MemoryGame.this, R.style.MyAlertDialogStyle);
                }
                else {
                    builder = new AlertDialog.Builder(MemoryGame.this);
                }
                builder.setTitle("Memory Game Scoreboard")
                        .setMessage(formattedScoreboardString)
                .show();
            }
        });
    }


    /* OnClick Botó General - Fora del onCreate - [ImageButton bCardX] */
    @Override
    public void onClick(View view) {

        if (clickable) {

            //Primera carta que es gira
            if (flippedCards == 0) {
                ib1 = (ImageButton) view;
                nomCarta1 = establishPokemon(ib1.getId());
                ib1.setImageResource(getResources().getIdentifier(nomCarta1, "drawable", getPackageName()));
                flippedCards++;
                ib1.setClickable(false); //el desactivem fins que no es giri boca abaix de nou
            }

            //Segona carta que es gira
            else if (flippedCards == 1) {
                ib2 = (ImageButton) view;
                nomCarta2 = establishPokemon(ib2.getId());
                ib2.setImageResource(getResources().getIdentifier(nomCarta2, "drawable", getPackageName()));
                ib2.setClickable(false);

                //Hi ha match entre les 2 cartes
                if (nomCarta1 == nomCarta2) {
                    ++completedPairs;
                    //Game ENDED
                    if (completedPairs == 6)
                        gameFinished = true;
                }

                //No hi ha match
                else {
                    clickable = false;
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            ib1.setImageResource(R.drawable.pokemon_card_back);
                            ib2.setImageResource(R.drawable.pokemon_card_back);
                            ib1.setClickable(true);
                            ib2.setClickable(true);
                            clickable = true;
                        }
                    }, 2000);
                }
                flippedCards = 0;
            }
        }
    }

    /* Associa una imatge a un ImageButton */
    private String establishPokemon (int bId) {
        switch (bId) {
            case R.id.bCard1:
                return pokeArray.get(0);
            case R.id.bCard2:
                return pokeArray.get(1);
            case R.id.bCard3:
                return pokeArray.get(2);
            case R.id.bCard4:
                return pokeArray.get(3);
            case R.id.bCard5:
                return pokeArray.get(4);
            case R.id.bCard6:
                return pokeArray.get(5);
            case R.id.bCard7:
                return pokeArray.get(6);
            case R.id.bCard8:
                return pokeArray.get(7);
            case R.id.bCard9:
                return pokeArray.get(8);
            case R.id.bCard10:
                return pokeArray.get(9);
            case R.id.bCard11:
                return pokeArray.get(10);
            case R.id.bCard12:
                return pokeArray.get(11);
            default:
                return pokeArray.get(11);
        }
    }

    /* Classe que gestiona el temporitzador en segons */
    private class CountSeconds extends AsyncTask<Void,Integer,Integer> {

        @Override
        protected void onPostExecute(Integer intgr) {
            super.onPostExecute(intgr);
            textSegons.setText("Elapsed time : "+String.valueOf(intgr)+" s");
            bStart.setText("Game Ended!");
            bStart.setBackgroundColor(Color.parseColor("#009fcc"));
            flippedCards = 0;
            completedPairs = 0;
            Intent i = getIntent();
            playerName = i.getStringExtra("PlayerName");
            Long tsLong = System.currentTimeMillis();
            String ts = tsLong.toString();
            String DBquery = "INSERT INTO " + DataBasePuntuacionsContract.TABLE_NAME + " VALUES ( '" + playerName + "', '" + String.valueOf(intgr) + "s' ,'" + ts + "')";
            SQLiteDatabase db = dbh.getWritableDatabase();
            if (db != null)
                db.execSQL(DBquery);
        }

        @Override
        protected void onProgressUpdate(Integer... intgr) {
            super.onProgressUpdate(intgr);
            textSegons.setText("Time: "+String.valueOf(intgr[0])+" s");
        }

        @Override
        protected Integer doInBackground(Void... voids) {
            int initialSeconds = 0;
            while (!gameFinished) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                initialSeconds++;
                publishProgress(initialSeconds);
            }
            return (initialSeconds-1);
        }
    }

    //Botó BACK
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (!bStart.isClickable())
            cs.cancel(true);
    }

    // Convert TimeStamp in millis to a readable format
    private String formatDate(long milliseconds) /* This is your topStory.getTime()*1000 */ {
        DateFormat sdf = new SimpleDateFormat("dd/MM/yyyy' 'HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliseconds);
        TimeZone tz = TimeZone.getDefault();
        sdf.setTimeZone(tz);
        return sdf.format(calendar.getTime());
    }

}
