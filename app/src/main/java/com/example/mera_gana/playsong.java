package com.example.mera_gana;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

public class playsong extends AppCompatActivity {
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaplayer.stop();
        mediaplayer.release();
    }
    SeekBar seekBar;
    ImageView play,prev,next;
    TextView textView;
    ArrayList<File> songs;
    MediaPlayer mediaplayer;
    String textContent;
    int position;
    Thread updateSeek;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playsong);
        play=findViewById(R.id.play);
        prev=findViewById(R.id.prev);
        next=findViewById(R.id.next);
        textView=findViewById(R.id.textView);
        seekBar=findViewById(R.id.seekBar);

        Intent intent= getIntent();
        Bundle bundle= intent.getExtras();
        songs=(ArrayList)bundle.getParcelableArrayList("songlist");
        textContent=intent.getStringExtra("currentsongs");
        textView.setText(textContent);
        textView.setSelected(true);
        position= intent.getIntExtra("position",0);
        Uri uri=Uri.parse(songs.get(position).toString());
        mediaplayer=mediaplayer.create(this,uri);
        mediaplayer.start();
        seekBar.setMax(mediaplayer.getDuration());

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
             mediaplayer.seekTo(seekBar.getProgress());
            }
        });

        updateSeek=new Thread(){
            @Override
            public void run() {
                super.run();
                int curretposition=0;
                try{
                       while(curretposition<mediaplayer.getDuration()){
                           curretposition= mediaplayer.getCurrentPosition();
                           seekBar.setProgress(curretposition);
                           sleep(800);
                       }
                }
                catch(Exception e){
                    e.printStackTrace();
                }
            }
        };
        updateSeek.start();


        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mediaplayer.isPlaying()){
                    play.setImageResource(R.drawable.play);
                    mediaplayer.pause();
                }
                else{
                    play.setImageResource(R.drawable.pause);
                    mediaplayer.start();
                }
            }
        });
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaplayer.stop();
                mediaplayer.release();
                if(position!=0)
                    position=position-1;
                else
                    position=songs.size()-1;

                Uri uri=Uri.parse(songs.get(position).toString());
                mediaplayer=mediaplayer.create(getApplicationContext(),uri);
                mediaplayer.start();
                play.setImageResource(R.drawable.pause);
                seekBar.setMax(mediaplayer.getDuration());
                textContent=songs.get(position).getName().toString();
                textView.setText(textContent);
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaplayer.stop();
                mediaplayer.release();
                if(position!= songs.size()-1)
                    position=position+1;
                else
                    position=0;
                Uri uri=Uri.parse(songs.get(position).toString());
                mediaplayer=mediaplayer.create(getApplicationContext(),uri);
                mediaplayer.start();
                play.setImageResource(R.drawable.pause);
                seekBar.setMax(mediaplayer.getDuration());
                textContent=songs.get(position).getName().toString();
                textView.setText(textContent);
            }
        });
        }
}