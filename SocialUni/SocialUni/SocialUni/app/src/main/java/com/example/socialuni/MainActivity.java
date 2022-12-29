package com.example.socialuni;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        Button crtAccount = findViewById(R.id.register);
        TextView login = findViewById(R.id.goLogin);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
            }
        });

        crtAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText name_edit = (EditText) findViewById(R.id.register_name);
                EditText number_edit = (EditText) findViewById(R.id.register_number);
                EditText mail_edit = (EditText) findViewById(R.id.register_mail);
                EditText pass_edit = (EditText) findViewById(R.id.register_password);

                String name = name_edit.getText().toString();
                String number = number_edit.getText().toString();
                String mail = mail_edit.getText().toString();
                String pass = pass_edit.getText().toString();


                progressDialog = new ProgressDialog(MainActivity.this);
                progressDialog.setTitle("Creating");
                progressDialog.setMessage("Account");
                progressDialog.show();

                FirebaseAuth
                        .getInstance()
                        .createUserWithEmailAndPassword(mail.trim(), pass.trim())
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                UserProfileChangeRequest userProfileChangeRequest = new UserProfileChangeRequest.Builder().setDisplayName(name).build();
                                FirebaseAuth.getInstance().getCurrentUser()
                                        .updateProfile(userProfileChangeRequest);
                                new MySharedPreferences(MainActivity.this).setMyData(number);
                                UserModel userModel = new UserModel();
                                userModel.setUserName(name);
                                userModel.setUserNumber(number);
                                userModel.setUserEmail(mail);



                                FirebaseFirestore
                                        .getInstance()
                                        .collection("Users")
                                        .document(FirebaseAuth.getInstance().getUid())
                                        .set(userModel);
                                reset();


                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressDialog.cancel();
                                Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });



    }

    private void reset() {
        progressDialog.cancel();
        Toast.makeText(MainActivity.this, "Account Created Login Please", Toast.LENGTH_SHORT).show();
  //      FirebaseAuth.getInstance().signOut();
    }


}