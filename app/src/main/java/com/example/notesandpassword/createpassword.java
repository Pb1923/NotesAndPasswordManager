package com.example.notesandpassword;

import static java.util.logging.Logger.global;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class createpassword extends AppCompatActivity {

    EditText mcreatetitleofpassword,musername,mcreatepassword;
    FloatingActionButton msavepassword;
    CheckBox mcheckbox;

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseFirestore firebaseFirestore;

    ProgressBar mprogressbarofcreatepassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createpassword);

        msavepassword=findViewById(R.id.savepassword);
        mcreatetitleofpassword=findViewById(R.id.createtitleofpassword);
        musername=findViewById(R.id.username);
        mcreatepassword=findViewById(R.id.createpassword);
        mcheckbox=findViewById(R.id.checkBox);
        mprogressbarofcreatepassword=findViewById(R.id.progressbarofcreatepassword);

        Toolbar toolbar=findViewById(R.id.toolbarofcreatepassword);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        mcheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonview, boolean ischecked) {
                if(ischecked)
                {
                    mcreatepassword.setTransformationMethod(null);
                }
                else
                {
                    mcreatepassword.setTransformationMethod( new PasswordTransformationMethod());
                }
            }
        });

        msavepassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title=mcreatetitleofpassword.getText().toString();
                String username=musername.getText().toString().trim();
                String password=mcreatepassword.getText().toString().trim();
                if(title.isEmpty() || password.isEmpty())
                {
                    Toast.makeText(getApplicationContext(), "Fields Required", Toast.LENGTH_SHORT).show();
                }

                else
                {

                    mprogressbarofcreatepassword.setVisibility(View.VISIBLE);

                    DocumentReference documentReference = firebaseFirestore.collection("allpasswords").document(firebaseUser.getUid()).collection("mypasswords").document();
                    Map<String,Object>pass= new HashMap<>();
                    pass.put("title",title);
                    pass.put("password",password);

                    documentReference.set(pass).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void avoid) {
                            Toast.makeText(getApplicationContext(), "created", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(createpassword.this,passwordsActivity.class));

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), "failed", Toast.LENGTH_SHORT).show();
                            mprogressbarofcreatepassword.setVisibility(View.INVISIBLE);
                        }
                    });




                }
            }
        });


    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem i) {

        if(i.getItemId()==android.R.id.home)
        {
            onBackPressed();
        }

        return super.onOptionsItemSelected(i);
    }
}