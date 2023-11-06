package com.example.notesandpassword;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
private EditText mloginpassword,mloginemail;
private RelativeLayout mlogin,mgotosinup;
private TextView mgotoforgotpassword;

private FirebaseAuth firebaseAuth;

ProgressBar mprogressbarofmain;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();

        mloginpassword=findViewById(R.id.loginpassword);
        mloginemail=findViewById(R.id.loginemail);
        mlogin=findViewById(R.id.login);
        mgotosinup=findViewById(R.id.gotosignup);
        mgotoforgotpassword=findViewById(R.id.gotoforgotpassword);
        mprogressbarofmain=findViewById(R.id.progressbarofmain);

        firebaseAuth=FirebaseAuth.getInstance();
        FirebaseUser firebaseUser =firebaseAuth.getCurrentUser();

        mgotosinup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,SignUp.class));
            }
        });

        mgotoforgotpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,Forgotpassword.class));
            }
        });

        mlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mail =mloginemail.getText().toString().trim();
                String password=mloginpassword.getText().toString().trim();
                if(mail.isEmpty() || password.isEmpty())
                {
                    Toast.makeText(getApplicationContext(), "Field Required", Toast.LENGTH_SHORT).show();
                }
                else
                {

                    mprogressbarofmain.setVisibility(View.VISIBLE);

                    //login
                    firebaseAuth.signInWithEmailAndPassword(mail,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful())
                            {
                                checkmailVerification();
                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(),"Not Registered",Toast.LENGTH_SHORT).show();
                                mprogressbarofmain.setVisibility(View.INVISIBLE);
                            }
                        }
                    });
                }

            }
        });
    }

    private void checkmailVerification()
    {
        FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();
        if(firebaseUser.isEmailVerified()==true)
        {
            Toast.makeText(getApplicationContext(),"Logged in Successfully",Toast.LENGTH_SHORT).show();
            finish();
            startActivity(new Intent(MainActivity.this,menuactivity.class));
        }
        else
        {
            mprogressbarofmain.setVisibility(View.INVISIBLE);
            Toast.makeText(getApplicationContext(),"Verify your Mail",Toast.LENGTH_SHORT).show();
            firebaseAuth.signOut();
        }
    }




}