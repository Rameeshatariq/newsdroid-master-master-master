package com.example.nimra.newsdroid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class userprofile extends AppCompatActivity implements View.OnClickListener{

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase mfirebaseDatabase;
    private TextView uname;
    private TextView ucity;
    private TextView uphone;
    private Button logout;
    private DatabaseReference mDatabase;
    private String userID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userprofile);

        uname=(TextView)findViewById(R.id.username);
        final TextView uemail=(TextView)findViewById(R.id.useremail);
        ucity=(TextView)findViewById(R.id.usercity);
        uphone=(TextView)findViewById(R.id.userphone);


        firebaseAuth = FirebaseAuth.getInstance();
        mfirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabase = mfirebaseDatabase.getReference();
        if(firebaseAuth.getCurrentUser() == null){
            finish();
            startActivity(new Intent(this, login.class));
        }

        final FirebaseUser user  = firebaseAuth.getCurrentUser();
        userID = user.getUid();


        logout = (Button) findViewById(R.id.logout);

       mDatabase.addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(DataSnapshot dataSnapshot) {
               for(DataSnapshot ds : dataSnapshot.getChildren()){
                   userInfo uInfo = new userInfo();
                   uInfo.setUsername(ds.child(userID).getValue(userInfo.class).getUsername());
                   uInfo.setUsercity(ds.child(userID).getValue(userInfo.class).getUsercity());
                   uInfo.setUserphonenumber(ds.child(userID).getValue(userInfo.class).getUserphonenumber());



                   uname.setText("Name: "+uInfo.getUsername());
                   ucity.setText("City: "+uInfo.getUsercity());
                   uphone.setText("Phone: "+uInfo.getUserphonenumber());
                   uemail.setText("Email: "+user.getEmail());
               }
           }

           @Override
           public void onCancelled(DatabaseError databaseError) {

           }
       });





        logout.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {

        if(view == logout){
            firebaseAuth.signOut();
            finish();
            startActivity(new Intent(this, login.class));
        }

    }
}
