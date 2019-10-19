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

    private EditText editText2;                                                                     //TODO
    private Button btnConfirmCode;                                                                  //TODO
    private Button btnConfirmNum;                                                                   //TODO

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone);
        editText = findViewById(R.id.editText);
        editText2 = findViewById(R.id.editText2);
        btnConfirmCode = findViewById(R.id.btnConfirmCode);
        btnConfirmNum = findViewById(R.id.btnConfirmNum);

        callbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                Log.e("ololo", "onVerificationCompleted");
                if(isCodeSent) {
                    //signIn(phoneAuthCredential);
                } else {
                    signIn(phoneAuthCredential);
                }
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Log.e("ololo", "onVerificationFailed" + e.getMessage());
            }

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                isCodeSent = false; //если смс придет то пользователю нужно ввести код              //TODO
                //TODO: show EditText for add code-sms
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
                    startActivity(new Intent(PhoneActivity.this, MainActivity.class));
                } else {
                    Log.e("ololo", "Ошибка авторизации");
                }
            }
        });
        //TODO 2 и 3: Если после нажатия на кнопку EditText пусто - показываем Toast, иначе signIn
        btnConfirmCode.setOnClickListener(new View.OnClickListener() {                              //TODO: 2
            @Override                                                                               //TODO: 2
            public void onClick(View v) {                                                           //TODO: 2
                //String s = editText2.getText().toString().trim();                                 //TODO: 2
                if(editText2 == null) {                                                             //TODO: 2  if(TextUtils.isEmpty(s))
                    Toast.makeText(PhoneActivity.this, "Введите код!", Toast.LENGTH_SHORT);
                    Log.e("ololo", "Toast");
                } else {                                                                            //TODO: 3
                    signIn(phoneAuthCredential);                                                    //TODO: 3
                    Log.e("ololo", "signIn(phoneAuthCredential)");
                }                                                                                   //TODO: 3
            }                                                                                       //TODO: 3
        });                                                                                         //TODO: 3
    }

    public void onClick(View view) {
        String phone = editText.getText().toString().trim();
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phone,
                30,
                TimeUnit.SECONDS,
                this,
                callbacks);
        Log.e("ololo", "onClick");

        //TODO: 1. Если после нажатия на копку приходит смс с кодом, то убираем текущие View и показываем новые:
        if(isCodeSent == true) {                                                                    //TODO: 1
        btnConfirmNum.setVisibility(View.GONE);                                                     //TODO: 1
        editText.setVisibility(View.GONE);                                                          //TODO: 1

        editText2.setVisibility(View.VISIBLE);                                                      //TODO: 1
        btnConfirmCode.setVisibility(View.VISIBLE);                                                 //TODO: 1
        Log.e("ololo", "isCodeSent == true");
        }
    }
}
