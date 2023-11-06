package com.example.notesandpassword;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.w3c.dom.Text;

public class passworddetail extends AppCompatActivity {

    private TextView mtitleofpassdetail,mshowpassword;
    FloatingActionButton mgotoeditpassword;
    CheckBox mcheckbox3;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passworddetail);

        mtitleofpassdetail=findViewById(R.id.titleofpassdetail);
        mshowpassword=findViewById(R.id.showpassword);
        mgotoeditpassword=findViewById(R.id.gotoeditpassword);
        mcheckbox3=findViewById(R.id.checkBox3);

        Toolbar toolbar=findViewById(R.id.toolbarofpassdetail);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent data=getIntent();

        mcheckbox3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonview, boolean ischecked) {
                if(ischecked)
                {
                    mshowpassword.setTransformationMethod(null);
                }
                else
                {
                    mshowpassword.setTransformationMethod( new PasswordTransformationMethod());
                }
            }
        });

        mgotoeditpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(v.getContext(),editpassactivity.class);
                intent.putExtra("title",data.getStringExtra("title"));
                intent.putExtra("password",data.getStringExtra("password"));
                intent.putExtra("passId",data.getStringExtra("passId"));
                v.getContext().startActivity(intent);
            }
        });

        mshowpassword.setText(data.getStringExtra("password"));
        mtitleofpassdetail.setText(data.getStringExtra("title"));

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