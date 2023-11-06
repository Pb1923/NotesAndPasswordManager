package com.example.notesandpassword;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class menuactivity extends AppCompatActivity {

    private Button mnotesmenu,mpasswordsmenu;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menuactivity);


        getSupportActionBar().hide();

        mnotesmenu=findViewById(R.id.notesmenu);
        mpasswordsmenu=findViewById(R.id.passwordsmenu);



        mnotesmenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(menuactivity.this,notesActivity.class));
            }
        });

        mpasswordsmenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(menuactivity.this,passwordsActivity.class));
            }
        });




    }
}