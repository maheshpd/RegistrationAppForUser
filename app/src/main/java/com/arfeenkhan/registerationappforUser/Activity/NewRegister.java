package com.arfeenkhan.registerationappforUser.Activity;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.arfeenkhan.registerationappforUser.R;
import com.arfeenkhan.registerationappforUser.Utils.Common;

import java.util.HashMap;
import java.util.Map;

public class NewRegister extends AppCompatActivity {


    EditText newName, newEmail, newPhone;
    Button submitBtn;

    String sname, semail, sphone;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_register);

        dialog = new ProgressDialog(this);

        newName = findViewById(R.id.NewName);
        newEmail = findViewById(R.id.newEmail);
        newPhone = findViewById(R.id.newPhone);
        submitBtn = findViewById(R.id.newSubmitBtn);

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InsertData();
            }
        });

    }

    private void InsertData() {

        sname = newName.getText().toString().trim();
        semail = newEmail.getText().toString().trim();
        sphone = newPhone.getText().toString().trim();

        if (TextUtils.isEmpty(sname)) {
            Toast.makeText(this, "Required all Field", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(semail)) {
            Toast.makeText(this, "Required all Field", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(sphone)) {
            Toast.makeText(this, "Required all Field", Toast.LENGTH_SHORT).show();
        } else {
            dialog.setMessage("Please wait...");
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();

            uploadFile();

        }
    }

    private void uploadFile() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String newRegisterUrl = "";
        StringRequest sr = new StringRequest(Request.Method.POST, newRegisterUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();
                param.put("", sname);
                param.put("", semail);
                param.put("", sphone);
                param.put("", Common.tagno);
                return super.getParams();
            }
        };
        queue.add(sr);
    }
}
