package com.example.notesandpassword;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class passwordsActivity extends AppCompatActivity {

    FloatingActionButton mcreatepasswordsfab;
    private FirebaseAuth firebaseAuth;

    RecyclerView mrecyclerview2;
    StaggeredGridLayoutManager staggeredGridLayoutManager;

    FirebaseUser firebaseUser;
    FirebaseFirestore firebaseFirestore;

    FirestoreRecyclerAdapter<firebasemodel2, PassViewHolder> passAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passwords);

        mcreatepasswordsfab =findViewById(R.id.createpasswordsfab);
        firebaseAuth=FirebaseAuth.getInstance();

        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        firebaseFirestore=FirebaseFirestore.getInstance();

        getSupportActionBar().setTitle("All Passwords");

        mcreatepasswordsfab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(passwordsActivity.this,createpassword.class));
            }
        });

        Query query=firebaseFirestore.collection("allpasswords").document(firebaseUser.getUid()).collection("mypasswords").orderBy("title",Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<firebasemodel2> allpass=new FirestoreRecyclerOptions.Builder<firebasemodel2>().setQuery(query,firebasemodel2.class).build();

        passAdapter = new FirestoreRecyclerAdapter<firebasemodel2, PassViewHolder>(allpass) {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            protected void onBindViewHolder(@NonNull PassViewHolder passViewholder, int p, @NonNull firebasemodel2 firebasemodel2) {

                ImageView popupbutton = passViewholder.itemView.findViewById(R.id.menupopbutton2);

                int colorcode=getRandomColor();

                passViewholder.mpasswords.setBackgroundColor(passViewholder.itemView.getResources().getColor(colorcode, null));

                passViewholder.passwordtitle.setText(firebasemodel2.getTitle());

                String docid=passAdapter.getSnapshots().getSnapshot(p).getId();

                passViewholder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        //we have to open password detail activity
                        Intent intent = new Intent(v.getContext(),passworddetail.class);

                        intent.putExtra("title",firebasemodel2.getTitle());
                        intent.putExtra("password",firebasemodel2.getPassword());
                        intent.putExtra("passId",docid);

                        v.getContext().startActivity(intent);
                        //Toast.makeText(getApplicationContext(),"clicked",Toast.LENGTH_SHORT).show();
                    }
                });

                popupbutton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        PopupMenu popupMenu=new PopupMenu(v.getContext(),v);
                        popupMenu.setGravity(Gravity.END);
                        popupMenu.getMenu().add("Edit").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {

                                Intent intent=new Intent(v.getContext(),editpassactivity.class);
                                intent.putExtra("title",firebasemodel2.getTitle());
                                intent.putExtra("password",firebasemodel2.getPassword());
                                intent.putExtra("passId",docid);
                                v.getContext().startActivity(intent);
                                return false;
                            }
                        });

                        popupMenu.getMenu().add("Delete").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem menuItem) {

                                //Toast.makeText(v.getContext(),"Deleted Successfully",Toast.LENGTH_SHORT).show();
                                DocumentReference documentReference=firebaseFirestore.collection("allpasswords").document(firebaseUser.getUid()).collection("mypasswords").document(docid);
                                documentReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(v.getContext(),"Deleted Successfully",Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(v.getContext(),"Something went wrong",Toast.LENGTH_SHORT).show();
                                    }
                                });
                                return false;
                            }
                        });
                        popupMenu.show();
                    }
                });



            }

            @NonNull
            @Override
            public PassViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
               View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.passwords_layout,parent,false);
               return new PassViewHolder(view);
            }
        };

        mrecyclerview2=findViewById(R.id.recyclerview2);
        mrecyclerview2.setHasFixedSize(true);
        staggeredGridLayoutManager=new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        mrecyclerview2.setLayoutManager(staggeredGridLayoutManager);
        mrecyclerview2.setAdapter(passAdapter);

    }

    public class PassViewHolder extends RecyclerView.ViewHolder
    {
        private TextView passwordtitle;
        LinearLayout mpasswords;

        public PassViewHolder(@NonNull View itemView) {
            super(itemView);

            passwordtitle=itemView.findViewById(R.id.passwordtitle);
            mpasswords=itemView.findViewById(R.id.passwords);

        }
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch(item.getItemId())
        {
            case R.id.logout:
                firebaseAuth.signOut();
                finish();
                startActivity(new Intent(passwordsActivity.this,MainActivity.class));
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        passAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (passAdapter != null) {
            passAdapter.stopListening();
        }

    }

        private int getRandomColor()
        {

            List<Integer> colorcode=new ArrayList<>();
            colorcode.add(R.color.c1);
            colorcode.add(R.color.c2);
            colorcode.add(R.color.c3);
            colorcode.add(R.color.c4);
            colorcode.add(R.color.c5);
            colorcode.add(R.color.c6);
            colorcode.add(R.color.c7);
            colorcode.add(R.color.c8);
            colorcode.add(R.color.c9);
            colorcode.add(R.color.c10);

            Random r=new Random();
            int n= r.nextInt(colorcode.size());
            return colorcode.get(n);



        }

    }
