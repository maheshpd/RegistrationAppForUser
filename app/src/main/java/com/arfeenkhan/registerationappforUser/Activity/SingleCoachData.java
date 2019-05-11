package com.arfeenkhan.registerationappforUser.Activity;

import android.app.ProgressDialog;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.GridView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.arfeenkhan.registerationappforUser.Adapter.SingleCoachDataAdapter;
import com.arfeenkhan.registerationappforUser.Model.SignleCoachDataModel;
import com.arfeenkhan.registerationappforUser.R;
import com.arfeenkhan.registerationappforUser.Utils.Common;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SingleCoachData extends AppCompatActivity {

    //    private RecyclerView singleCoachData;
    private TextView textView;
    private String singleCoachDataUrl = "http://magicconversion.com/barcodescanner/singleuserdata.php";
    private SingleCoachDataAdapter adapter;
    private ArrayList<SignleCoachDataModel> list = new ArrayList<>();
    ProgressDialog mdialog;

    int total;

    ArrayList<String> listofpeople = new ArrayList<>();

    SwipeRefreshLayout swipeRefreshLayout;
    GridView SingleDataView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_coach_data);

        mdialog = new ProgressDialog(this);
        getData();

        textView = findViewById(R.id.total_no_person);
        swipeRefreshLayout = findViewById(R.id.swipeLayout);
        SingleDataView = findViewById(R.id.single_coach_recycler_data);
        adapter = new SingleCoachDataAdapter(list, this);
        SingleDataView.setAdapter(adapter);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void getData() {
        mdialog.setMessage("Please wait...");
        mdialog.setCanceledOnTouchOutside(false);
        mdialog.show();
        list.clear();
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest sr = new StringRequest(Request.Method.POST, singleCoachDataUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray array = new JSONArray(response);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject c = array.getJSONObject(i);
                        String id = c.getString("id");
                        String uqniceno = c.getString("uqniceno");
                        String name = c.getString("name");
                        String phone = c.getString("phone");
                        String dt = c.getString("dt");
                        String tm = c.getString("tm");
                        String coachname = c.getString("coachname");

                        SignleCoachDataModel signleCoachDataModel = new SignleCoachDataModel(id, coachname, name, uqniceno, dt, tm, phone);
                        list.add(signleCoachDataModel);

                        total = list.size();

                        textView.setText("Total no of Person: " + String.valueOf(total));
                        mdialog.dismiss();
                    }
                    adapter.notifyDataSetChanged();
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
}
