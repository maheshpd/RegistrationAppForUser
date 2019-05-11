package com.arfeenkhan.registerationappforUser.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.arfeenkhan.registerationappforUser.Adapter.CityAdapter;
import com.arfeenkhan.registerationappforUser.Model.CityModel;
import com.arfeenkhan.registerationappforUser.R;
import com.arfeenkhan.registerationappforUser.Utils.CheckInternet;
import com.arfeenkhan.registerationappforUser.Utils.MySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CityName extends AppCompatActivity {

    SwipeRefreshLayout swipeRefreshLayout;
    GridView cityView;
    CityAdapter cityAdapter;
    ArrayList<CityModel> citylist = new ArrayList<>();

    StringRequest request;
    ProgressDialog progressDialog;


    String getCity_url = "http://magicconversion.com/barcodescanner/selectcity.php";

    CheckInternet checkInternet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_name);

//        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        progressDialog = new ProgressDialog(this);

        swipeRefreshLayout = findViewById(R.id.swipeLayout);
        cityView = findViewById(R.id.cityView);

        cityAdapter = new CityAdapter(this, citylist);
        cityView.setAdapter(cityAdapter);

        //check internet connection
        checkInternet = new CheckInternet(this);
        if (checkInternet.isConnected()) {
            getCityData();
        } else {
            Toast.makeText(this, "No Internet", Toast.LENGTH_SHORT).show();
        }

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getCityData();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        cityView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(CityName.this, SelectTag.class);
                intent.putExtra("place", citylist.get(position).getName());
                startActivity(intent);
            }
        });
    }

    private void getCityData() {
        progressDialog.setMessage("Please wait for city name...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        citylist.clear();
        request = new StringRequest(Request.Method.GET, getCity_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray arr = new JSONArray(response);

                    for (int i = 0; i < arr.length(); i++) {
                        JSONObject c = arr.getJSONObject(i);
                        String cityname = c.getString("cityname");
                        CityModel cityModel = new CityModel("mumbai", cityname);
                        citylist.add(cityModel);
                        cityAdapter.notifyDataSetChanged();
                        progressDialog.dismiss();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        MySingleton.getmInstance(this).addToRequestque(request);
    }
}
