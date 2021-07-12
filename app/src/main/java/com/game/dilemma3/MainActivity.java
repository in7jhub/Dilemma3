package com.game.dilemma3;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.VideoView;import android.view.WindowManager.LayoutParams;import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;import android.graphics.Matrix;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {
    Button fullpageButton;
    VideoView videoView[] = new VideoView[5];
    ImageView shrinkImg;
    ImageView bg;
    boolean main_loop_visit = true;
    boolean isFPButtonPressed = false;
    boolean isDraggingToLeft = false;
    boolean activateGesture = false;

    int iPage = 0;
    boolean bSeq[] = new boolean[10];
    void InitializeSeq(){
        for(int i = 0; i < 10; i++){
            bSeq[i] = false;
        }
        bSeq[0] = true;
    }
    void startVideo(int iVid, boolean isLoopingVideo){
        videoView[iVid].setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.start();
                if(isLoopingVideo) {
                    mp.setLooping(true);
                }
            }
        });
        for(int i = 0; i < videoView.length; i++){
            videoView[i].setVisibility(View.INVISIBLE);
        }
        videoView[iVid].setVisibility(View.VISIBLE);
    }
    void main_loop() {
        if(main_loop_visit) {
            Handler mainloophandler = new Handler();
            Thread mainloopThd = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        try {
                            mainloophandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    main_loop_content();
                                }
                            });
                            Thread.sleep(5);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            mainloopThd.start();
            main_loop_visit = false;
        }
    }
    long timer_vid = 0;

    void main_loop_content(){
        //iPage's initial value : 0, increases by 1 as the game progresses.
        switch (iPage) {
            case 0 :
                //Start a looping video
                if (bSeq[0]) {
                    //video 'c1'
                    startVideo(0, true);
                    bSeq[0] = false;
                }

                //Start button input only once.
                //Button will be reactivated when the second video is over.
                if(isFPButtonPressed){
                    ignoreButton(true);
                    iPage++;
                }
                break;

            case 1 :
                if (bSeq[1]) {
                    startVideo(1, false);
                    bSeq[1] = false;
                }

                //Video plays without button interference until timer_vid reaches 100
                //Timer duration should be same as length of video 'c2'
                timer_vid++;
                if(timer_vid == 100) {
                    ignoreButton(false);
                }

                //In the final version, when the udp signal is received, the iPage++.
                if(isFPButtonPressed){
                    ignoreButton(true);
                    //Reset timer to use it again
                    timer_vid = 0;
                    iPage++;
                }
                break;

            case 2 :
                if (bSeq[2]){
                    startVideo(2, false);
                    bSeq[2] = false;
                }

                //same as length of video 'c3'
                timer_vid++;
                if(timer_vid == 100){
                    ignoreButton(false);
                }

                if(isFPButtonPressed){
                    ignoreButton(true);
                    timer_vid = 0;
                    iPage++;
                }
                break;

            case 3 :
                if (bSeq[3]){
                    startVideo(3, false);
                    bSeq[3] = false;
                }

                //same as length of video 'c4'
                timer_vid++;
                if(timer_vid == 100){
                    activateGesture = true;
                }

                if(isDraggingToLeft){
                    timer_vid = 0;
                    iPage++;
                }
                break;

            case 4 :
                if(bSeq[4]){
                    ignoreButton(false);
                    bSeq[4] = false;
                }

                shrinkImg.setVisibility(View.VISIBLE);
                if(isFPButtonPressed){
                    ignoreButton(true);
                    //temporarily test
                    iPage++;
                    shrinkImg.setVisibility(View.INVISIBLE);
                    //shrink animation activate
                }
                //shrink animation go if activated
                //iPage++; if animation ended
                break;

            case 5 :
                if(bSeq[5]){
                    //video 'c5'
                    startVideo(4, false);
                    bSeq[5] = false;
                }
                break;
        }
        isDraggingToLeft = true; // temporarily jump !SHOULD BE SET AS FALSE!
        isFPButtonPressed = false;
    }

    void fetchVideoSrc(){
        videoView[0] = (VideoView) findViewById(R.id.vid1);
        Uri uri1 = Uri.parse("android.resource://com.game.dilemma2/raw/c1");
        videoView[0].setVideoURI(uri1);

        videoView[1] = (VideoView) findViewById(R.id.vid2);
        Uri uri2 = Uri.parse("android.resource://com.game.dilemma2/raw/c2");
        videoView[1].setVideoURI(uri2);

        videoView[2] = findViewById(R.id.vid3);
        Uri uri3 = Uri.parse("android.resource://com.game.dilemma2/raw/c3");
        videoView[2].setVideoURI(uri3);

        videoView[3] = (VideoView) findViewById(R.id.vid4);
        Uri uri4 = Uri.parse("android.resource://com.game.dilemma2/raw/c4");
        videoView[3].setVideoURI(uri4);

        videoView[4] = (VideoView) findViewById(R.id.vid5);
        Uri uri5 = Uri.parse("android.resource://com.game.dilemma2/raw/c5");
        videoView[4].setVideoURI(uri5);
    }
    void fetchImgSrc(){
        shrinkImg = findViewById(R.id.shrinkImg);
        bg = findViewById(R.id.bg);
    }
    void ignoreButton(boolean ignore){
        if(ignore){
            fullpageButton.setVisibility(View.INVISIBLE);
        } else {
            fullpageButton.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fullpageButton = findViewById(R.id.fullpageButton);

        InitializeSeq();
        fetchImgSrc();
        fetchVideoSrc();
        fullpageButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                isFPButtonPressed = true;
            }
        });

        videoView[0] = (VideoView) findViewById(R.id.vid1);
        Uri uri1 = Uri.parse("android.resource://com.game.dilemma2/raw/c1");
        videoView[0].setVideoURI(uri1);
        videoView[0].setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.start();
                mp.setLooping(true);
            }
        });
        videoView[0].setVisibility(View.VISIBLE);
        main_loop();
    }
}