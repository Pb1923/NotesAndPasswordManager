package com.example.notesandpassword;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
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

public class editpassactivity extends AppCompatActivity {

    Intent data;
    EditText medittitleofpassword,meditpassword;
    FloatingActionButton msaveeditpassword;
    CheckBox mcheckbox2;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editpassactivity);

        medittitleofpassword=findViewById(R.id.edittitleofpassword);
        meditpassword=findViewById(R.id.editpassword);
        msaveeditpassword=findViewById(R.id.saveeditpassword);
        mcheckbox2=findViewById(R.id.checkBox2);

        data=getIntent();

        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();

        Toolbar toolbar=findViewById(R.id.toolbarofeditpassword);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mcheckbox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonview, boolean ischecked) {
                if(ischecked)
                {
                    meditpassword.setTransformationMethod(null);
                }
                else
                {
                    meditpassword.setTransformationMethod( new PasswordTransformationMethod());
                }
            }
        });


        msaveeditpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getApplicationContext(),"saved",Toast.LENGTH_SHORT).show();

                String newtitle=medittitleofpassword.getText().toString();
                String newpassword=meditpassword.getText().toString();

                if(newtitle.isEmpty() || newpassword.isEmpty())
                {
                    Toast.makeText(getApplicationContext(),"some fields may be missing",Toast.LENGTH_SHORT).show();
                    return;
                }

                else
                {
                    DocumentReference documentReference=firebaseFirestore.collection("allpasswords").document(firebaseUser.getUid()).collection("mypasswords").document(data.getStringExtra("passId"));
                    Map<String,Object> pass= new HashMap<>();
                    pass.put("title",newtitle);
                    pass.put("password",newpassword);
                    documentReference.set(pass).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(getApplicationContext(),"updated",Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(),"failed",Toast.LENGTH_SHORT).show();
                        }
                    });

                }


            }
        });


        String passtitle=data.getStringExtra("title");
        String pass2=data.getStringExtra("password");
        medittitleofpassword.setText(passtitle);
        meditpassword.setText(pass2);

    }
}