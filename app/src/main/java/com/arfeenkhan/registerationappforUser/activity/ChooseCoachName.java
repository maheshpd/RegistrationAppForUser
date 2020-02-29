package com.arfeenkhan.registerationappforUser.activity;

import android.app.ProgressDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.arfeenkhan.registerationappforUser.adapter.PersonName1Adapter;
import com.arfeenkhan.registerationappforUser.model.SessionNameModel;
import com.arfeenkhan.registerationappforUser.R;
import com.arfeenkhan.registerationappforUser.utils.Common;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ChooseCoachName extends AppCompatActivity {
    ProgressDialog progressDialog;
    StringRequest sessionrequest;
    ArrayList<SessionNameModel> sessionlist = new ArrayList<>();
    PersonName1Adapter personName1Adapter;
    RecyclerView sessionRecycler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_coach_name);

        sessionRecycler = findViewById(R.id.coach_nameRecyclerview);

        progressDialog = new ProgressDialog(this);
        getSessionName();

        personName1Adapter = new PersonName1Adapter(sessionlist, this);
        LinearLayoutManager manager1 = new LinearLayoutManager(this);
        sessionRecycler.setLayoutManager(manager1);
        sessionRecycler.setAdapter(personName1Adapter);
        sessionRecycler.setHasFixedSize(true);
        personName1Adapter.notifyDataSetChanged();
    }

    private void getSessionName() {
        progressDialog.setMessage("Please wait...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        RequestQueue queue = Volley.newRequestQueue(this);
        sessionlist.clear();
        sessionrequest = new StringRequest(Request.Method.POST, Common.sessionUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray arr = new JSONArray(response);
                    for (int i = 0; i < arr.length(); i++) {
                        JSONObject c = arr.getJSONObject(i);
                        String name = c.getString("name");
                        String id = c.getString("id");
                        SessionNameModel snm = new SessionNameModel(id, name);
                        sessionlist.add(snm);
                        progressDialog.dismiss();
                    }

                    personName1Adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                try {
                    JSONArray array = new JSONArray(response);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject c = array.getJSONObject(i);
                        String message = c.getString("message");
                        progressDialog.dismiss();
                        Toast.makeText(ChooseCoachName.this, message, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("propertyname", Common.tagno);
                return params;
            }
        };
        queue.add(sessionrequest);
    }


}
