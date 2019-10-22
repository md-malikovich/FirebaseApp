package com.e.firebaseapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class PhoneActivity extends AppCompatActivity {

    private EditText editText;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks; //ждет результат от кликов
    private boolean isCodeSent;

    private EditText editText2;                                                                     //
    private Button btnConfirmCode;                                                                  //
    private Button btnConfirmNum;
    String codeInet, myCode;                                                                        //TODO: A
    FirebaseAuth mAuth;                                                                             //TODO: A

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone);
        editText = findViewById(R.id.editText);
        editText2 = findViewById(R.id.editText2);
        btnConfirmCode = findViewById(R.id.btnConfirmCode);
        btnConfirmNum = findViewById(R.id.btnConfirmNum);
        mAuth=FirebaseAuth.getInstance();                                                           //TODO: A

        btnConfirmCode.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                myCode=editText2.getText().toString();                                              //TODO: A
                PhoneAuthCredential credential=PhoneAuthProvider.getCredential(codeInet,myCode);    //TODO: A
                signIn(credential);                                                                 //TODO: A
            }
        });
        callbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) { //TODO: #3
                //phoneAuthCredential.getSmsCode();                                                   //TODO: D
                //editText2.setText(codeInet);
                Log.e("ololo", "onVerificationCompleted");
//                if(isCodeSent) {
//                    signIn(phoneAuthCredential);
//                } else {
//                    //
//                }
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Log.e("ololo", "onVerificationFailed" + e.getMessage());
            }

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                isCodeSent = true;
//                //TODO: show EditText for add code-sms
//                if(isCodeSent) {                                                                  //TODO: #2
//                    btnConfirmNum.setVisibility(View.GONE);                                       //
//                    editText.setVisibility(View.GONE);                                            //
//                    editText2.setVisibility(View.VISIBLE);                                        //
//                    btnConfirmCode.setVisibility(View.VISIBLE);                                   //
//                    Log.e("ololo", "onCodeSent()");                                               //
//                }
                codeInet = s;                                                                       //TODO: A
            }

            @Override
            public void onCodeAutoRetrievalTimeOut(@NonNull String s) {
                super.onCodeAutoRetrievalTimeOut(s);
                //Toast
            }
        };
    }

    private void signIn(final PhoneAuthCredential phoneAuthCredential) {
        FirebaseAuth.getInstance()
                .signInWithCredential(phoneAuthCredential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()) {
                    Toast.makeText(PhoneActivity.this, "Успешно", Toast.LENGTH_SHORT);
                    Log.e("ololo", "Toast.makeText - Успешно");                           //TODO: #4
                    //if(editText2.length() == 6 || editText2 != null) {
//                        btnConfirmCode.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                //String s = editText2.getText().toString().trim();                 //
//                                if(editText2.length() == 6) {                                       //
                                    startActivity(new Intent(PhoneActivity.this, MainActivity.class));
                                    finish();//TODO: #5
                                    Log.e("ololo", "signIn(phoneAuthCredential) in btnConfirmCode");
//                                } else {                                                            //
//                                    Toast.makeText(PhoneActivity.this, "Введите код!", Toast.LENGTH_SHORT);
//                                    Log.e("ololo", "Toast");
//                                    //startActivity(new Intent(PhoneActivity.this, MainActivity.class));
//                                }
//                            }                                                                     //TODO: 3
//                        });
                        //startActivity(new Intent(PhoneActivity.this, MainActivity.class));
                        //Log.e("ololo", "signIn(phoneAuthCredential) in signIn");
                    //}
                    //startActivity(new Intent(PhoneActivity.this, MainActivity.class));
                } else {
                    Log.e("ololo", "Ошибка авторизации");
                }
            }
        });

//        btnConfirmCode.setOnClickListener(new View.OnClickListener() {                            //
//            @Override                                                                             //
//            public void onClick(View v) {                                                         //
//                //String s = editText2.getText().toString().trim();                               //
//                if(editText2.length() != 6) {                                                     //
//                    Toast.makeText(PhoneActivity.this, "Введите код!", Toast.LENGTH_SHORT);
//                    Log.e("ololo", "Toast");
//                } else {                                                                          //
//                    signIn(phoneAuthCredential);                                                  //
//                    Log.e("ololo", "signIn(phoneAuthCredential) in btnConfirmCode");
//                }                                                                                 //
//            }                                                                                     //
//        });                                                                                       //
    }

    public void onClick(View view) {                                                                //TODO: #1
        String phone = editText.getText().toString().trim();
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phone,
                30,
                TimeUnit.SECONDS,
                this,
                callbacks);
        Log.e("ololo", "onClick");
//      if(isCodeSent) {                                                                            //
        btnConfirmNum.setVisibility(View.GONE);                                                     //
        editText.setVisibility(View.GONE);                                                          //
        editText2.setVisibility(View.VISIBLE);                                                      //
        btnConfirmCode.setVisibility(View.VISIBLE);                                                 //
        Log.e("ololo", "isCodeSent == true");
    //}
    }
}
