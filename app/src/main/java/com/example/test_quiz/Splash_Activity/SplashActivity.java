package com.example.test_quiz.Splash_Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.test_quiz.R;
import com.example.test_quiz.Auth_Controller.LoginActivity;
import com.wang.avi.AVLoadingIndicatorView;

public class SplashActivity extends AppCompatActivity {

    private AVLoadingIndicatorView avLoadingIndicatorView;

    /** Called when the activity is first created. */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        avLoadingIndicatorView = findViewById(R.id.avi);
        avLoadingIndicatorView.smoothToShow();
        Animation anim = AnimationUtils.loadAnimation(this,R.anim.alpha);
        anim.reset();
        ConstraintLayout l = findViewById(R.id.lin_lay);
        l.clearAnimation();
        anim=AnimationUtils.loadAnimation(this,R.anim.translate);
        anim.reset();
        TextView tv=  findViewById(R.id.textView2);
        tv.clearAnimation();
        tv.startAnimation(anim);
        //fillableLoaders.setProgress(40,1000);
        /* New Handler to start the Menu-Activity
         * and close this Splash-Screen after some seconds.*/

        /** Duration of wait **/
        int SPLASH_DISPLAY_LENGTH = 2090;
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                Intent mainIntent = new Intent(SplashActivity.this, LoginActivity.class);
                mainIntent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                SplashActivity.this.startActivity(mainIntent);
                avLoadingIndicatorView.smoothToHide();
                SplashActivity.this.finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        }, SPLASH_DISPLAY_LENGTH);
    }
}
