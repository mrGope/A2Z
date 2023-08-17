package com.example.a2z;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class otpascreen  extends AppCompatActivity{
    EditText otp;
    Button subotp;
    TextView msg;
    private FirebaseAuth mAuth;
    String verificationId;
    String phone,userid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otpascreen);
        otp=findViewById(R.id.otpvalue);
        subotp=findViewById(R.id.subotp1);
        msg=findViewById(R.id.textView3);
        Intent intent = getIntent();
        phone=getIntent().getExtras().getString("phone");
        userid=getIntent().getExtras().getString("userid");
        mAuth=FirebaseAuth.getInstance();
        msg.setText("Otp has been Sent to "+phone);
        sendVerificationCode(phone);
        subotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(otp.getText().toString().isEmpty()==false){
                    verifyCode(otp.getText().toString());
                    findViewById(R.id.loadingPanel).setVisibility(View.VISIBLE);}
                else
                    Toast.makeText(otpascreen.this, "Enter OTP Please", Toast.LENGTH_SHORT).show();

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
                        findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                        if (task.isSuccessful()) {
                            // if the code is correct and the task is successful
                            // we are sending our user to new activity.
                            Toast.makeText(otpascreen.this, "OTP SUCCESS", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(otpascreen.this,HomeActivity.class);
                            i.putExtra("phone",phone);
                            i.putExtra("userid",userid);
                            startActivity(i);
                            finish();
                            Toast.makeText(otpascreen.this, "OTP SUCCESS BUT CAME BACK", Toast.LENGTH_SHORT).show();
                        } else {
                            // if the code is not correct then we are
                            // displaying an error message to the user.
                            Toast.makeText(otpascreen.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
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
                        .setActivity(otpascreen.this)                 // Activity (for callback binding)
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

            Toast.makeText(otpascreen.this, "OTP SENT", Toast.LENGTH_SHORT).show();
            verificationId = s;
            findViewById(R.id.loadingPanel).setVisibility(View.GONE);
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
            Toast.makeText(otpascreen.this, e.getMessage(), Toast.LENGTH_LONG).show();
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


