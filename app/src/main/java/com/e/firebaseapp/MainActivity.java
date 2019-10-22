package com.e.firebaseapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.internal.Constants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import org.w3c.dom.Text;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private TextView textName;
    static final int GALLERY_REQUEST = 1;
    static final int NAME_REQUEST = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        SharedPreferences preferences = getSharedPreferences("saving", MODE_PRIVATE);
//        preferences.edit().putString("name", textName.getText().toString()).apply();
//        preferences.edit().apply();

//        SharedPreferences preferences = getSharedPreferences("saving", MODE_PRIVATE);
//        textName.setText(preferences.getString("name", ""));


        if(FirebaseAuth.getInstance().getCurrentUser() == null) {
            startActivity(new Intent(this, PhoneActivity.class));
            finish();
            return;
        }
        setContentView(R.layout.activity_main);
        textName = findViewById(R.id.textName);
        getUserInfo();
        //getUserInfoListener();
    }

    //TODO #1
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_signOut) {
            Intent intent = new Intent(this, PhoneActivity.class);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void getUserInfo() {
        FirebaseFirestore.getInstance().collection("users")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid()).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful() && task.getResult() != null) {
                            String name = task.getResult().getString("name");
                            textName.setText(name);
                        }
                    }
                });
    }

//    private void getUserInfoListener() {
//        FirebaseFirestore.getInstance().collection("users")
//                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
//                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
//                    @Override
//                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
//                        if(documentSnapshot != null && documentSnapshot.exists()) {
//                            String name = documentSnapshot.getString("name");
//                            textName.setText(name);
//                        }
//                    }
//                });
//    }

    //TODO #2
    public void onClickEdit(View view) {
        SharedPreferences preferences = getSharedPreferences("saving", MODE_PRIVATE);
        preferences.edit().putString("name", textName.getText().toString()).apply();
        preferences.edit().apply();

        Intent intent = new Intent(this, EditNameActivity.class);
        startActivityForResult(intent, NAME_REQUEST);
    }

    public void onImageClick(View view) {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, GALLERY_REQUEST);
    }

    //TODO #4
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        Bitmap bitmap = null;

        switch(requestCode) {
            case GALLERY_REQUEST:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = data.getData();
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    imageView.setImageBitmap(bitmap);
                }
            //TODO #3
            case NAME_REQUEST:
                if(resultCode == RESULT_OK){

                    String name = data.getStringExtra("name");
                    textName.setText(name);

                }
        }
    }
}
//1. Menu Sign Out
//TODO: 2. Хранить имя в SharedPreferences
//TODO: 3. Показывать новое имя (через intent)
//TODO: 4. При нажатии на ImageView открыть галерею и поставить картинку в ImageView (READ_EXTERNAL_STORAGE)
