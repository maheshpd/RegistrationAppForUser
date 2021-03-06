package com.arfeenkhan.registerationappforUser.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.SearchView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.arfeenkhan.registerationappforUser.R;
import com.arfeenkhan.registerationappforUser.adapter.SelectTagAdapter;
import com.arfeenkhan.registerationappforUser.model.SelectTagModel;
import com.arfeenkhan.registerationappforUser.utils.Common;
import com.arfeenkhan.registerationappforUser.utils.MySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SelectTag extends AppCompatActivity implements SearchView.OnQueryTextListener {

    RecyclerView tagRecycler;
    ArrayList<SelectTagModel> taglist;
    SelectTagAdapter tagAdapter;

    StringRequest request;
    ProgressDialog progressDialog;

    String place;

    LinearLayoutManager llm;
    SearchView search_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_tag);

        taglist = new ArrayList<>();
        progressDialog = new ProgressDialog(this);
        place = getIntent().getExtras().get("place").toString();
        getData();

        search_view = findViewById(R.id.searchView);
        search_view.setOnQueryTextListener(this);
        tagRecycler = findViewById(R.id.select_tags);
        tagAdapter = new SelectTagAdapter(this, taglist);
        llm = new LinearLayoutManager(this);
        tagRecycler.setLayoutManager(llm);
        tagRecycler.setAdapter(tagAdapter);
        tagRecycler.setHasFixedSize(true);
        tagAdapter.notifyDataSetChanged();


    }

    private void getData() {
        progressDialog.setMessage("Please wait...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        taglist.clear();
        StringRequest sr = new StringRequest(Request.Method.POST, Common.gettag_url, new Response.Listener<String>() {
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
                        String sessionname = c.getString("ss_name");
                        SelectTagModel stm = new SelectTagModel(name, place, tag, time, ctf, date, tf, sessionname);
                        taglist.add(stm);
                        progressDialog.dismiss();
                        tagAdapter.notifyDataSetChanged();

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
                params.put("place", place);
                return params;

            }
        };
        MySingleton.getmInstance(this).addToRequestque(sr);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        getData();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        newText = newText.toLowerCase();
        ArrayList<SelectTagModel> newList = new ArrayList<>();
        for (SelectTagModel tgm : taglist) {
            String name = tgm.getName().toLowerCase();
            String time = tgm.getTime().toLowerCase();
            String ctf = tgm.getCtf().toLowerCase();
            String tagno = tgm.getTagno().toLowerCase();
            String date = tgm.getDate().toLowerCase();
            if (name.contains(newText)) {
                newList.add(tgm);
            } else if (time.contains(newText)) {
                newList.add(tgm);
            } else if (ctf.contains(newText)) {
                newList.add(tgm);
            } else if (tagno.contains(newText)) {
                newList.add(tgm);
            } else if (date.contains(newText)) {
                newList.add(tgm);
            }
        }
        tagAdapter.setFilter(newList);
        return true;
    }
}
