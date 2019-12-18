package com.example.test_quiz.Splash_Activity;

import android.content.Intent;

import com.daimajia.androidanimations.library.Techniques;
import com.example.test_quiz.Create_Quiz.Custom_quiz;
import com.example.test_quiz.R;
import com.viksaa.sssplash.lib.activity.AwesomeSplash;
import com.viksaa.sssplash.lib.cnst.Flags;
import com.viksaa.sssplash.lib.model.ConfigSplash;

public class Splash_create_quiz extends AwesomeSplash {

    private boolean isAdmin = false;
    @Override
    public void initSplash(ConfigSplash configSplash) {


        isAdmin = getIntent().getBooleanExtra("ChatAdmin",false);
        configSplash.setBackgroundColor(R.color.btn_logout_bg); //any color you want form colors.xml
        configSplash.setAnimCircularRevealDuration(500); //int ms
        configSplash.setRevealFlagX(Flags.REVEAL_RIGHT);  //or Flags.REVEAL_LEFT
        configSplash.setRevealFlagY(Flags.REVEAL_BOTTOM); //or Flags.REVEAL_TOP

        //Choose LOGO OR PATH; if you don't provide String value for path it's logo by default

        //Customize Logo
        configSplash.setLogoSplash(0); //or any other drawable
        configSplash.setAnimLogoSplashDuration(0); //int ms
        configSplash.setAnimLogoSplashTechnique(Techniques.Bounce); //choose one form Techniques (ref: https://github.com/daimajia/AndroidViewAnimations)


        //Customize Path
//        configSplash.setPathSplash(Constants.DROID_LOGO); //set path String
        configSplash.setOriginalHeight(400); //in relation to your svg (path) resource
        configSplash.setOriginalWidth(400); //in relation to your svg (path) resource
        configSplash.setAnimPathStrokeDrawingDuration(0);
        configSplash.setPathSplashStrokeSize(3); //I advise value be <5
        configSplash.setPathSplashStrokeColor(R.color.btn_logout_bg); //any color you want form colors.xml
        configSplash.setAnimPathFillingDuration(50);
        configSplash.setPathSplashFillColor(R.color.strokeColor); //path object filling color


        //Customize Title
        configSplash.setTitleSplash("");
        configSplash.setTitleTextColor(R.color.btn_logout_bg);
        configSplash.setTitleTextSize(30f); //float value
        configSplash.setAnimTitleDuration(0);
        configSplash.setAnimTitleTechnique(Techniques.FlipInX);
//        configSplash.setTitleFont("fonts/.ttf"); //provide string to your font located in assets/fonts/
    }

    @Override
    public void animationsFinished() {

        startActivity(new Intent(Splash_create_quiz.this, Custom_quiz.class));
        finish();

    }
}
