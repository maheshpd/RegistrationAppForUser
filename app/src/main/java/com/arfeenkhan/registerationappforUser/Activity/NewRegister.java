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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NewRegister extends AppCompatActivity {


    EditText newName, newEmail, newPhone;
    Button submitBtn;

    String sname, semail, sphone;
    ProgressDialog dialog;
    StringRequest sessionrequest;
    String allocationNum = "http://magicconversion.com/barcodescanner/getallocation.php";
    String sessionUrl = "http://magicconversion.com/barcodescanner/getSessionName.php";
    ArrayList<String> sessionlist = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_register);

        dialog = new ProgressDialog(this);

        newName = findViewById(R.id.NewName);
        newEmail = findViewById(R.id.newEmail);
        newPhone = findViewById(R.id.newPhone);
        submitBtn = findViewById(R.id.newSubmitBtn);

        getSessionName();

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
            getAllocationNum();

        }
    }

    private void uploadFile() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String newRegisterUrl = "http://magicconversion.com/barcodescanner/getregister.php";
        StringRequest sr = new StringRequest(Request.Method.POST, newRegisterUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONArray arr = null;
                try {
                    arr = new JSONArray(response);
                    JSONObject c = arr.getJSONObject(0);
                    String message = c.getString("message");
                    dialog.dismiss();
                    Toast.makeText(NewRegister.this, message, Toast.LENGTH_SHORT).show();
                    finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();
                param.put("name", sname);
                param.put("email", semail);
                param.put("phone", sphone);
                param.put("tagno", Common.tagno);
                param.put("dt", Common.timeStamp);
                param.put("tm", Common.eventTimes);
                param.put("coachname", Common.allocationname);
                param.put("tagno", Common.tagno);
                param.put("allocation", String.valueOf(Common.sessionValue));
                return param;
            }
        };
        queue.add(sr);
    }

    public void getAllocationNum() {
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest sr = new StringRequest(Request.Method.POST, allocationNum, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray array = new JSONArray(response);
                    JSONObject c = array.getJSONObject(0);
                    Common.sessionValue = Integer.parseInt(c.getString("allocation"));
                    if (Common.sessionValue < sessionlist.size()) {
                        Common.allocationname = sessionlist.get(Common.sessionValue);
                        Common.sessionValue++;
                        uploadFile();
                    } else {
                        Common.sessionValue = 0;
                        Common.allocationname = sessionlist.get(Common.sessionValue);
                        Common.sessionValue++;
                        uploadFile();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();
                param.put("tagno", Common.tagno);
                return param;
            }
        };

        queue.add(sr);
    }

    private void getSessionName() {
        RequestQueue queue = Volley.newRequestQueue(this);
        sessionlist.clear();
        sessionrequest = new StringRequest(Request.Method.POST, sessionUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray arr = new JSONArray(response);
                    for (int i = 0; i < arr.length(); i++) {
                        JSONObject c = arr.getJSONObject(i);
                        String name = c.getString("name");
                        sessionlist.add(name);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("tagno", Common.tagno);
                return params;
            }
        };
        queue.add(sessionrequest);
    }
}
