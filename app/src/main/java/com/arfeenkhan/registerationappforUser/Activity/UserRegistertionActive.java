package com.arfeenkhan.registerationappforUser.Activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.arfeenkhan.registerationappforUser.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.marcoscg.fingerauth.FingerAuth;
import com.marcoscg.fingerauth.FingerAuthDialog;

public class UserRegistertionActive extends AppCompatActivity {
    EditText edt_password;
    Button btn_login;
    String spassword;

    //Firebase
    FirebaseAuth mAuth;
    ProgressDialog progressDialog;

    String name;
    TextView nameTxt;

    //FingerPrint
    private FingerAuthDialog fingerAuthDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_registertion_active);

        edt_password = findViewById(R.id.userpass);
        btn_login = findViewById(R.id.btnLogin);
        progressDialog = new ProgressDialog(this);
        nameTxt = findViewById(R.id.coachnameusername);
        name = getIntent().getExtras().get("name").toString();
        nameTxt.setText(name);

        //Firebase
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_coach = database.getReference("Coach").child(name);

//        final boolean hasFingerprintSupport = FingerAuth.hasFingerprintSupport(this);
//
//        fingerAuthDialog = null;
//        if (hasFingerprintSupport)
//            createAndShowDialog();
//        else
//            Toast.makeText(UserRegistertionActive.this, "Your device does not support fingerprint authentication", Toast.LENGTH_SHORT).show();


        //Firebase
        mAuth = FirebaseAuth.getInstance();

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spassword = edt_password.getText().toString().trim();

                if (TextUtils.isEmpty(spassword)) {
                    Toast.makeText(UserRegistertionActive.this, "Enter password", Toast.LENGTH_SHORT).show();
                } else {

                    progressDialog.setMessage("Please wait...");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();

                    table_coach.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String password = dataSnapshot.child("password").getValue().toString();
                            if (password.equals(spassword)) {
                                progressDialog.dismiss();
                                edt_password.setText("");
                                startActivity(new Intent(UserRegistertionActive.this, UserDetails.class));
                            } else {
                                progressDialog.dismiss();
                                edt_password.setText("");
                                Toast.makeText(UserRegistertionActive.this, "User not exist", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }
        });
    }

    private void createAndShowDialog() {
        fingerAuthDialog = new FingerAuthDialog(this)
                .setTitle("Sign in")
                .setCancelable(false)
                .setPositiveButton("Use password", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("Cancel", null)
                .setOnFingerAuthListener(new FingerAuth.OnFingerAuthListener() {
                    @Override
                    public void onSuccess() {
                        startActivity(new Intent(UserRegistertionActive.this, UserDetails.class));
                    }

                    @Override
                    public void onFailure() {
                        Toast.makeText(UserRegistertionActive.this, "onFailure", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError() {
                        Toast.makeText(UserRegistertionActive.this, "onError", Toast.LENGTH_SHORT).show();
                    }
                });
        fingerAuthDialog.show();
    }
}
