package com.example.notesandpassword;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class editnoteactivity extends AppCompatActivity {


    EditText medittitleofnote,meditcontentofnote;
    FloatingActionButton msaveeditnote;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editnoteactivity);

        medittitleofnote=findViewById(R.id.edittitleofnote);
        meditcontentofnote=findViewById(R.id.editcontentofnote);
        msaveeditnote=findViewById(R.id.saveeditnote);
        Intent data=getIntent();
        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();

        Toolbar toolbar=findViewById(R.id.toolbarofeditnote);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        msaveeditnote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getApplicationContext(),"saved",Toast.LENGTH_SHORT).show();

               String newt=medittitleofnote.getText().toString();
               String newc=meditcontentofnote.getText().toString();

               if(newt.isEmpty() || newc.isEmpty())
               {
                   Toast.makeText(editnoteactivity.this, "not written anything", Toast.LENGTH_SHORT).show();
               }
                else
               {
                   DocumentReference documentReference=firebaseFirestore.collection("notes").document(firebaseUser.getUid()).collection("my notes").document(data.getStringExtra("noteid"));
                   Map<String,Object> note=new HashMap<>();
                   note.put("title",newt);
                   note.put("content",newc);
                   documentReference.set(note).addOnSuccessListener(new OnSuccessListener<Void>() {
                       @Override
                       public void onSuccess(Void unused) {
                           Toast.makeText(editnoteactivity.this, "updated", Toast.LENGTH_SHORT).show();
                           startActivity(new Intent(editnoteactivity.this,notesActivity.class));
                       }
                   }).addOnFailureListener(new OnFailureListener() {
                       @Override
                       public void onFailure(@NonNull Exception e) {
                           Toast.makeText(editnoteactivity.this, "failed to update", Toast.LENGTH_SHORT).show();
                       }
                   });
               }

            }
        });





        String ntitle=data.getStringExtra("title");
        String ncontent=data.getStringExtra("content");
        medittitleofnote.setText(ntitle);
        meditcontentofnote.setText(ncontent);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId()==android.R.id.home)
        {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }
}