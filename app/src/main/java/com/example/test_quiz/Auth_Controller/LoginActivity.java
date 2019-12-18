package com.example.test_quiz.Auth_Controller;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.test_quiz.View.MainActivity;
import com.example.test_quiz.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.wang.avi.AVLoadingIndicatorView;

import es.dmoral.toasty.Toasty;

public class LoginActivity extends AppCompatActivity {

    private EditText inputEmail, inputPassword;
    private FirebaseAuth auth;
    private AVLoadingIndicatorView avLoadingIndicatorView;
    private Button btnSignup, btnLogin, btnReset;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser() != null) {

            if(auth.getCurrentUser().isEmailVerified()) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
            }else {
                Toasty.warning(getApplicationContext(), R.string.email_unverified, Toasty.LENGTH_SHORT).show();
                FirebaseAuth.getInstance().signOut();
                finish();
            }
        }

        // set the view now
        setContentView(R.layout.activity_login);

        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        inputEmail =  findViewById(R.id.email);
        inputPassword =  findViewById(R.id.password);
        avLoadingIndicatorView =  findViewById(R.id.loader1);
        btnSignup =  findViewById(R.id.btn_signup);
        btnLogin =  findViewById(R.id.btn_login);
        btnReset =  findViewById(R.id.btn_reset_password);

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();



        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SignUp.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, ResetPasswordActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = inputEmail.getText().toString();
                final String password = inputPassword.getText().toString();

                if (TextUtils.isEmpty(email)) {
                    Toasty.warning(getApplicationContext(),"Enter email address!", Toasty.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toasty.warning(getApplicationContext(),"Enter password!", Toasty.LENGTH_SHORT).show();
                    return;
                }

                avLoadingIndicatorView.setVisibility(View.VISIBLE);
                avLoadingIndicatorView.smoothToShow();

                //authenticate user
                auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
//                                progressBar.setVisibility(View.GONE);
                                avLoadingIndicatorView.setVisibility(View.GONE);
                                avLoadingIndicatorView.smoothToHide();
                                if (!task.isSuccessful()) {
                                    // there was an error
                                    if (password.length() < 6) {
                                        inputPassword.setError(getString(R.string.minimum_password));
                                    } else {
                                        Toasty.warning(LoginActivity.this,getString(R.string.auth_failed), Toasty.LENGTH_LONG).show();
                                    }
                                } else {
                                    if(auth.getCurrentUser().isEmailVerified()) {
                                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                        finish();
                                    }else {
                                        Toasty.error(LoginActivity.this,R.string.email_unverified, Toasty.LENGTH_SHORT).show();
                                        FirebaseAuth.getInstance().signOut();
                                    }

                                }
                            }
                        });
            }
        });
    }
}