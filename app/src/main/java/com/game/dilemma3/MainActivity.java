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
    VideoView videoView[] = new VideoView[10];
    ImageView shrinkImg;
    ImageView bg;
    boolean main_loop_visit = true;
    boolean isFPButtonPressed = false;
    boolean isDraggingToLeft = false;
    boolean activateGesture = false;

    int iPage = 0;
    boolean bSeq[] = new boolean[10];
    boolean tb = true;

    void logAll(){
        boolean visible;
        if(fullpageButton.getVisibility() == View.VISIBLE){
            visible = true;
        } else {
            visible = false;
        }

        Log.d("----------","-----------");
        Log.d("iPage     :  ", Integer.toString(iPage));
        Log.d("Btn_pres  :  ", Boolean.toString(isFPButtonPressed));
        Log.d("Btn_show  :  ", Boolean.toString(visible));
    }

    int timer_vid = 0;
    boolean playVideo = true;
    boolean c1 = true;
    long c1_timer = 0;
    void controlVideo(boolean state){
        playVideo = state;
        if(state){ // pause
            videoView[0].start();
            if(c1){
                c1_timer++;
                // rewind video every 200sec(200000msec)
                if(c1_timer > 200000){
                    videoView[0].seekTo(0);
                    c1_timer = 0;
                }
            }
        }
        else if (!state){ // play
            videoView[0].pause();
        }
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
                            Thread.sleep(1);
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

    void main_loop_content(){
        //iPage's initial value : 0, increases by 1 as the game progresses.
        switch (iPage) {
            case 0 :
                //Start a looping video
                if (bSeq[0]) {
                    //video 'c1'
                    bSeq[0] = false;
                }

                //Start button input only once.
                //Button will be reactivated when the second video is over.
                if(isFPButtonPressed){
//                    ignoreButton(true);
//                    iPage++;
                }
                break;

            case 1 :
                if (bSeq[1]) {
                    bSeq[1] = false;
                }

                //Video plays without button interference until timer_vid reaches 100
                //Timer duration should be same as length of video 'c2'
                timer_vid++;
                if(timer_vid == 100){
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
                    bSeq[5] = false;
                }
                break;
        }
        logAll();
        isDraggingToLeft = true; // temporarily jump !SHOULD BE /SET AS FALSE!
        isFPButtonPressed = false;
    }
    void InitializeSeq(){
        for(int i = 0; i < 10; i++){
            bSeq[i] = true;
        }
    }
    void fetchVideoSrc(){
        videoView[0] = (VideoView) findViewById(R.id.vid1);
        Uri uri1 = Uri.parse("android.resource://com.game.dilemma3/raw/c2");
        videoView[0].setVideoURI(uri1);
        videoView[0].setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.start();
                mp.setLooping(true);
            }
        });
//
//        videoView[1] = (VideoView) findViewById(R.id.vid2);
//        Uri uri2 = Uri.parse("android.resource://com.game.dilemma3/raw/c2");
//        videoView[1].setVideoURI(uri2);
//
//        videoView[2] = (VideoView) findViewById(R.id.vid3);
//        Uri uri3 = Uri.parse("android.resource://com.game.dilemma3/raw/c3");
//        videoView[2].setVideoURI(uri3);
//
////        videoView[3] = (VideoView) findViewById(R.id.vid4);
////        Uri uri4 = Uri.parse("android.resource://com.game.dilemma3/raw/c4");
////        videoView[3].setVideoURI(uri4);
//
//        videoView[4] = (VideoView) findViewById(R.id.vid5);
//        Uri uri5 = Uri.parse("android.resource://com.game.dilemma3/raw/c5");
//        videoView[4].setVideoURI(uri5);
//
//        videoView[8] = (VideoView) findViewById(R.id.vid4);
//        Uri uri8 = Uri.parse("android.resource://com.game.dilemma3/raw/c1");
//        videoView[8].setVideoURI(uri8);
//        videoView[8].start();
//        videoView[8].setVisibility(View.VISIBLE);
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
                tb = !tb;
            }
        });

        main_loop();
    }
}