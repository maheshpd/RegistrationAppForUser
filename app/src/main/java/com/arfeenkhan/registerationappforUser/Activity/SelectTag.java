package com.arfeenkhan.registerationappforUser.Activity;

import android.app.ProgressDialog;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AbsListView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.arfeenkhan.registerationappforUser.Adapter.SelectTagAdapter;
import com.arfeenkhan.registerationappforUser.Model.SelectTagModel;
import com.arfeenkhan.registerationappforUser.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SelectTag extends AppCompatActivity {

    RecyclerView tagRecycler;
    ArrayList<SelectTagModel> taglist;
    SelectTagAdapter tagAdapter;
    TextView loadingTxt;
    String data_url = "http://magicconversion.com/barcodescanner/tagdata.php";

    StringRequest request;
    ProgressDialog progressDialog;

    String place;

    SwipeRefreshLayout swipeRefreshLayout;
    Boolean isScrolling = false;
    int currentItem, totalItems, scrollOutItems;
    LinearLayoutManager llm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_tag);

        taglist = new ArrayList<>();
        progressDialog = new ProgressDialog(this);
        place = getIntent().getExtras().get("place").toString();
        getData();

        swipeRefreshLayout = findViewById(R.id.swipeLayout);
        tagRecycler = findViewById(R.id.select_tags);
        tagAdapter = new SelectTagAdapter(this, taglist);
        loadingTxt = findViewById(R.id.loading_text);
        llm = new LinearLayoutManager(this);
        tagRecycler.setLayoutManager(llm);
        tagRecycler.setAdapter(tagAdapter);
        tagRecycler.setHasFixedSize(true);
        tagAdapter.notifyDataSetChanged();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

//        tagRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//
//                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
//                    isScrolling = true;
//                }
//
//            }
//
//            @Override
//            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//                currentItem = llm.getChildCount();
//                totalItems = llm.getItemCount();
//                scrollOutItems = llm.findFirstVisibleItemPosition();
//
//                if (isScrolling && (currentItem + scrollOutItems == totalItems)) {
//                    isScrolling = false;
//                    fetchData();
//                }
//            }
//        });


    }

//    private void fetchData() {
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//
//                for (int i = 0; i < 20; i++) {
//                    loadingTxt.setVisibility(View.VISIBLE);
//                    getData();
//                    tagAdapter.notifyDataSetChanged();
//                }
//            }
//        }, 4000);
//    }

    private void getData() {
        progressDialog.setMessage("Please wait...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        taglist.clear();
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest sr = new StringRequest(Request.Method.POST, data_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray arr = new JSONArray(response);
                    JSONObject c = null;
                    for (int i = 0; i < arr.length(); i++) {
                        c = arr.getJSONObject(i);
                        String tag = c.getString("tagno");
                        String place = c.getString("place");
                        String name = c.getString("name");
                        String ctf = c.getString("ctf");
                        String time = c.getString("time");
                        String date = c.getString("date");
                        String tf = c.getString("tf");
                        SelectTagModel stm = new SelectTagModel(name, place, tag, time, ctf, date, tf);
                        taglist.add(stm);
                        progressDialog.dismiss();
                        loadingTxt.setVisibility(View.INVISIBLE);
                        tagAdapter.notifyDataSetChanged();

                    }

//                    JSONObject c1=arr.getJSONObject(0);
//                    String message = c1.getString("message");
//
////                    String message = c.getString("message");
//                    Toast.makeText(SelectTags.this, message, Toast.LENGTH_SHORT).show();

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
                params.put("place", place);
                return params;

            }
        };


        queue.add(sr);

    }
}
