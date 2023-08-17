package com.example.a2z;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    EditText user;
    EditText pass;
    Button submit;

    TextView register;
    TextView rememberme;
    DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReferenceFromUrl("https://a2zandroid-24d47-default-rtdb.asia-southeast1.firebasedatabase.app/");
    String inuser;
    String inpass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        //
        user=findViewById(R.id.userid);
        pass=findViewById(R.id.pass);
        register=findViewById(R.id.register);

        rememberme=findViewById(R.id.remme);

        //register First
        register.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // switch activity to register first
                Intent activityChangeIntent = new Intent(MainActivity.this, register.class);
                startActivity(activityChangeIntent);
            }
        });
        rememberme.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // switch activity to register first
                Intent activityChangeIntent = new Intent(MainActivity.this, forgetpassword.class);
                startActivity(activityChangeIntent);
            }
        });
        //submit button
        submit=findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                inuser=user.getText().toString();
                inpass=pass.getText().toString();

                if(inuser.isEmpty()==false&&inpass.isEmpty()==false)
                {
                    //dumpy after login
                    //Toast.makeText(MainActivity.this, "input accepted", Toast.LENGTH_SHORT).show();
                    databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.hasChild(inuser))
                            {
                                String getPassword = snapshot.child(inuser).child("password").getValue(String.class);
                                if(getPassword.equals(inpass))
                                {
                                    String phone = "+91"+snapshot.child(inuser).child("phone").getValue(String.class);
                                 Intent inten=new Intent(getApplicationContext(),otpascreen.class);
                                 inten.putExtra("phone",phone);
                                 inten.putExtra("userid",inuser);
                                 startActivity(inten);
                                }
                                else
                                {
                                    Animation shake = AnimationUtils.loadAnimation(MainActivity.this, R.anim.shake);

                                    user.startAnimation(shake);
                                    pass.startAnimation(shake);
                                    Toast.makeText(MainActivity.this, "Wrong Password", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else
                            {
                                Animation shake = AnimationUtils.loadAnimation(MainActivity.this, R.anim.shake);

                                user.startAnimation(shake);
                                pass.startAnimation(shake);
                                Toast.makeText(MainActivity.this, "UserID not found." +
                                        "", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(MainActivity.this, "DATABASE ERROR", Toast.LENGTH_SHORT).show();
                        }
                    });


                }
                else
                {
                    Animation shake = AnimationUtils.loadAnimation(MainActivity.this, R.anim.shake);

                    user.startAnimation(shake);
                    pass.startAnimation(shake);
                    Toast.makeText(MainActivity.this, "Wrong password", Toast.LENGTH_SHORT).show();
                }}
        });
    }




    // signInWithPhoneAuthCredential(credential);



    @Override
    public void onBackPressed() {
        // Create the object of AlertDialog Builder class
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

        // Set the message show for the Alert time
        builder.setMessage("Do you want to exit ?");

        // Set Alert Title
        builder.setTitle("EXIT");

        // Set Cancelable false for when the user clicks on the outside the Dialog Box then it will remain show
        builder.setCancelable(false);

        // Set the positive button with yes name Lambda OnClickListener method is use of DialogInterface interface.
        builder.setPositiveButton("Yes", (DialogInterface.OnClickListener) (dialog, which) -> {
            // When the user click yes button then app will close
            finish();
        });

        // Set the Negative button with No name Lambda OnClickListener method is use of DialogInterface interface.
        builder.setNegativeButton("No", (DialogInterface.OnClickListener) (dialog, which) -> {
            // If user click no then dialog box is canceled.
            dialog.cancel();
        });

        // Create the Alert dialog
        AlertDialog alertDialog = builder.create();
        // Show the Alert Dialog box
        alertDialog.show();
    }
}