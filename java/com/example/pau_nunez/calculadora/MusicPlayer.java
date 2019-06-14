package com.example.pau_nunez.calculadora;

import android.graphics.Color;
import android.media.Image;
import android.media.MediaPlayer;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class MusicPlayer extends AppCompatActivity {

    ImageButton bDejaVu;
    ImageButton bRunning;
    ImageButton bHeartbeat;

    boolean playingDejaVu;
    boolean playingRunning;
    boolean playingHeartbeat;

    MediaPlayer mpDejaVu;
    MediaPlayer mpRunning;
    MediaPlayer mpHeartbeat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);

        bDejaVu = (ImageButton) findViewById(R.id.bDejaVu);
        bRunning = (ImageButton) findViewById(R.id.bRunning);
        bHeartbeat = (ImageButton) findViewById(R.id.bHeartbeat);

        playingDejaVu = false;
        playingRunning = false;
        playingHeartbeat = false;

        mpDejaVu = MediaPlayer.create(this,R.raw.deja_vu);
        mpRunning = MediaPlayer.create(this, R.raw.running_in_the_90s);
        mpHeartbeat = MediaPlayer.create(this,R.raw.heartbeat);

        bDejaVu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(playingDejaVu) {
                    //Arreglar el botó (que surti play de color verd)
                    bDejaVu.setImageResource(android.R.drawable.ic_media_play);
                    bDejaVu.setBackgroundColor(Color.parseColor("#19c100"));
                    mpDejaVu.pause();
                    playingDejaVu = false;
                }

                else {
                    //Arreglar el botó (que surti pause de color groc)
                    bDejaVu.setImageResource(android.R.drawable.ic_media_pause);
                    bDejaVu.setBackgroundColor(Color.parseColor("#e9ed00"));
                    mpDejaVu.start();
                    playingDejaVu = true;
                }
            }
        });

        bRunning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(playingRunning) {
                    //Arreglar el botó (que surti play de color verd)
                    bRunning.setImageResource(android.R.drawable.ic_media_play);
                    bRunning.setBackgroundColor(Color.parseColor("#19c100"));
                    mpRunning.pause();
                    playingRunning = false;
                }

                else {
                    //Arreglar el botó (que surti pause de color groc)
                    bRunning.setImageResource(android.R.drawable.ic_media_pause);
                    bRunning.setBackgroundColor(Color.parseColor("#e9ed00"));
                    mpRunning.start();
                    playingRunning = true;
                }
            }
        });

        bHeartbeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(playingHeartbeat) {
                    //Arreglar el botó (que surti play de color verd)
                    bHeartbeat.setImageResource(android.R.drawable.ic_media_play);
                    bHeartbeat.setBackgroundColor(Color.parseColor("#19c100"));
                    mpHeartbeat.pause();
                    playingHeartbeat = false;
                }

                else {
                    //Arreglar el botó (que surti pause de color groc)
                    bHeartbeat.setImageResource(android.R.drawable.ic_media_pause);
                    bHeartbeat.setBackgroundColor(Color.parseColor("#e9ed00"));
                    mpHeartbeat.start();
                    playingHeartbeat = true;
                }
            }
        });
    }

    //Botó BACK
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (playingDejaVu) mpDejaVu.stop();
        if (playingRunning) mpRunning.stop();
        if (playingHeartbeat) mpHeartbeat.stop();
    }
}
