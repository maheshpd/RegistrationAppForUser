package com.arfeenkhan.registerationappforUser.activity;

import android.app.ProgressDialog;
import android.content.Intent;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.arfeenkhan.registerationappforUser.adapter.CityAdapter;
import com.arfeenkhan.registerationappforUser.model.CityModel;
import com.arfeenkhan.registerationappforUser.R;
import com.arfeenkhan.registerationappforUser.utils.CheckInternet;
import com.arfeenkhan.registerationappforUser.utils.Common;
import com.arfeenkhan.registerationappforUser.utils.MySingleton;

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

    CheckInternet checkInternet;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_name);

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
        request = new StringRequest(Request.Method.GET, Common.getCity_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray arr = new JSONArray(response);

                    for (int i = 0; i < arr.length(); i++) {
                        JSONObject c = arr.getJSONObject(i);
                        String cityname = c.getString("cityname");
                        String cityimgUrl = c.getString("imgUrl");
                        CityModel cityModel = new CityModel(cityimgUrl, cityname);
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
