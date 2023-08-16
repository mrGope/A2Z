package com.example.a2z;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

public class register extends AppCompatActivity {
    ToggleButton accept;
    TextView otpsentmsg;
    EditText regname,reguserid,regpass,regphone,inotp;
    Button sendotp,subreg;
    private FirebaseAuth mAuth;
    DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReferenceFromUrl("https://a2zandroid-24d47-default-rtdb.asia-southeast1.firebasedatabase.app/");
    Animation shake ;

    String verificationId;
    //datavalues
    String code,dname,duserid,dpass,dphone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //shake animation
        shake = AnimationUtils.loadAnimation(register.this, R.anim.shake);
        regname=findViewById(R.id.regname);
        reguserid=findViewById(R.id.reguserid);
        regpass=findViewById(R.id.regpass);
        regphone=findViewById(R.id.phone);
        inotp=findViewById(R.id.inotp);
        sendotp=findViewById(R.id.sendotp);
        subreg=findViewById(R.id.subreg);
        otpsentmsg=findViewById(R.id.otpsentmsg);
        accept=findViewById(R.id.togglepolicy);// or get it from the layout by ToggleButton Btn=(ToggleButton) findViewById(R.id.IDofButton);

        mAuth=FirebaseAuth.getInstance();
        sendotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dphone=regphone.getText().toString();



                if(dphone!=null&&dphone.length()==10)
                {
                    Toast.makeText(register.this, "Please wait...", Toast.LENGTH_SHORT).show();
                   sendVerificationCode("+91"+dphone);
                }
                else
                    Toast.makeText(register.this, "Enter Correct Phone no.", Toast.LENGTH_SHORT).show();

            }
        });
        subreg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                code=inotp.getText().toString();
                dname=regname.getText().toString();
                duserid=reguserid.getText().toString();
                dpass=regpass.getText().toString();
                Boolean ToggleButtonState = accept.isChecked();
                if(dname.equals("")||duserid.equals("")||dpass.equals(""))
                    Toast.makeText(register.this, "Enter all details to signup.", Toast.LENGTH_SHORT).show();
                else if(code.isEmpty()||code.length()!=6)
                {
                    Toast.makeText(register.this, "Enter Correct OTP", Toast.LENGTH_SHORT).show();
                }
                else if(ToggleButtonState)
                {
                    Toast.makeText(register.this, "Accept the Terms and condition", Toast.LENGTH_SHORT).show();
                }
                else
                    verifyCode(code);

            }
        });

    }
    private void signInWithCredential(PhoneAuthCredential credential) {
        // inside this method we are checking if
        // the code entered is correct or not.
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if(snapshot.hasChild(duserid))
                                    {
                                        Toast.makeText(register.this, "User Id Already registered", Toast.LENGTH_SHORT).show();
                                    }
                                    else
                                    {
                                        //Toast.makeText(register.this, dphone+" "+dname+" "+duserid, Toast.LENGTH_SHORT).show();
                                        databaseReference.child("users").child(duserid).child("fullname").setValue(dname);
                                        databaseReference.child("users").child(duserid).child("phone").setValue(dphone);
                                        databaseReference.child("users").child(duserid).child("password").setValue(dpass);
                                        Toast.makeText(register.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                            // if the code is correct and the task is successful


                            // we are sending our user to new activity.
                            //Toast.makeText(register.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(register.this, MainActivity.class);
                            startActivity(i);
                            finish();
                        } else {
                            // if the code is not correct then we are
                            // displaying an error message to the user.
                            Toast.makeText(register.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }


    private void sendVerificationCode(String number) {
        // this method is used for getting
        // OTP on user phone number.
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(number)            // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallBack)           // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    // callback method is called on Phone auth provider.
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks

            // initializing our callbacks for on
            // verification callback method.
            mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {


        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);

            Toast.makeText(register.this, "OTP SENT", Toast.LENGTH_SHORT).show();
            otpsentmsg.setVisibility(View.VISIBLE);
            inotp.setVisibility(View.VISIBLE);
            inotp.getParent().requestChildFocus(inotp,inotp);
            verificationId = s;
        }

        // this method is called when user
        // receive OTP from Firebase.
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            // below line is used for getting OTP code
            // which is sent in phone auth credentials.
            final String code = phoneAuthCredential.getSmsCode();

            // checking if the code
            // is null or not.
            if (code != null) {

                verifyCode(code);
            }
        }

        // this method is called when firebase doesn't
        // sends our OTP code due to any error or issue.
        @Override
        public void onVerificationFailed(FirebaseException e) {
            // displaying error message with firebase exception.
            Toast.makeText(register.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    };

    // below method is use to verify code from Firebase.
    private void verifyCode(String code) {
        // below line is used for getting
        // credentials from our verification id and code.
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);

        // after getting credential we are
        // calling sign in method.
        signInWithCredential(credential);
    }
}