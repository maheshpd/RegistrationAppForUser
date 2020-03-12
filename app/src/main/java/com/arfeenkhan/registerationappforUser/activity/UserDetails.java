package com.arfeenkhan.registerationappforUser.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.arfeenkhan.registerationappforUser.adapter.RecentAdapter;
import com.arfeenkhan.registerationappforUser.model.SignleCoachDataModel;
import com.arfeenkhan.registerationappforUser.R;
import com.arfeenkhan.registerationappforUser.utils.Common;
import com.arfeenkhan.registerationappforUser.utils.MySingleton;

import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserDetails extends AppCompatActivity {

    private static final String TAG = "UserDetails";

    ///////Start Widget
    private EditText edt_id, edt_name, edt_phone;
    private Button btn_submit, addSessionName;
    private String sid, sname, sphone;
    private ProgressDialog progressDialog;
    RecyclerView eventPeople;
    ////end Widget

    ArrayList<String> sessionlist = new ArrayList<>();
    public static ArrayList<SignleCoachDataModel> record = new ArrayList<>();
    //Array List

    ArrayList<String> no0fpeoplelist = new ArrayList<>();
    /////start infusion
    public String key;
    public XmlRpcClient client;
    public Object[] contacts;
    public static List parameters, parameters2, parameters3, fields;
    public static Map contactData;
    boolean success = false;
    //End infusion

    DateFormat sdf;
    TextView tagsNo, totalNo, placename;
    ImageButton barcode_btn;
    public static RecentAdapter adapter;
    StringRequest sessionrequest;

    SwipeRefreshLayout swipeRefreshLayout;
    SharedPreferences preferences;
    Button newRegisterBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);

        SingleData();
        getSessionName();
        getData();

        //start Widget
        edt_id = findViewById(R.id.user_id);
        edt_name = findViewById(R.id.user_name);
        edt_phone = findViewById(R.id.user_phone);
        btn_submit = findViewById(R.id.newSubmitBtn);
        addSessionName = findViewById(R.id.add_person);
        tagsNo = findViewById(R.id.txt_tag_no);
        totalNo = findViewById(R.id.txt_total_bo);
        placename = findViewById(R.id.txt_place_name);
        barcode_btn = findViewById(R.id.barcode_btn);
        swipeRefreshLayout = findViewById(R.id.swipeLayout);
        newRegisterBtn = findViewById(R.id.new_people);
        eventPeople = findViewById(R.id.eventpeople);
        adapter = new RecentAdapter(record, this);
        LinearLayoutManager lm = new LinearLayoutManager(this);
        lm.setOrientation(LinearLayoutManager.VERTICAL);
        eventPeople.setLayoutManager(lm);
        eventPeople.setHasFixedSize(true);
        eventPeople.setAdapter(adapter);
        //start Widget

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                SingleData();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        Date now = new Date();
        sdf = new SimpleDateFormat("MMM dd, yyyy");
        Common.timeStamp = sdf.format(now);
        placename.setText("Place:" + Common.place);
        tagsNo.setText("Tag No: " + Common.tagno);

        //End Allocation Spinner

        if (Common.tf.equals("False")) {
            addSessionName.setVisibility(View.INVISIBLE);
        } else if (Common.tf.equals("True")) {
            addSessionName.setVisibility(View.VISIBLE);
        }

        addSessionName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserDetails.this, SingleCoachData.class));
            }
        });

        newRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UserDetails.this, NewRegister.class));
            }
        });

        progressDialog = new ProgressDialog(this);
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sid = edt_id.getText().toString().trim();
                sname = edt_name.getText().toString().trim();
                sphone = edt_phone.getText().toString();
                Log.d(TAG, "Session Value " + Common.sessionValue);
                if (TextUtils.isEmpty(sid)) {
                    Toast.makeText(UserDetails.this, "Enter id", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(sname)) {
                    Toast.makeText(UserDetails.this, "Enter name", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(sphone)) {
                    Toast.makeText(UserDetails.this, "Enter phoneno", Toast.LENGTH_SHORT).show();
                } else {

                    if (sname.equals("null") && sphone.equals("null")) {
                        Toast.makeText(UserDetails.this, "Enter valid name and phone number", Toast.LENGTH_SHORT).show();
                    } else {

                        progressDialog.setMessage("Please wait...");
                        progressDialog.setCanceledOnTouchOutside(false);
                        progressDialog.show();

                        getAllocationNum();
                    }
                }
            }
        });

        DB_Conn conn = new DB_Conn();
        conn.execute();
        conn.doInBackground();

        barcode_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserDetails.this, MainActivity.class));
            }
        });

        edt_id.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    // Your action on done
                    sid = edt_id.getText().toString();
                    getDataFromInfusion();
                    return true;
                }
                return false;
            }
        });

        edt_name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    sid = edt_id.getText().toString();
                    edt_id.requestFocus();

                    if (edt_id.length() == 0) {
                        Toast.makeText(UserDetails.this, "Enter all field", Toast.LENGTH_SHORT).show();
                    } else {
                        getDataFromInfusion();
                    }
                }
            }
        });
    }

    public void getDataFromInfusion() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        StringRequest sr = new StringRequest(Request.Method.POST, Common.getdatafromInfusionUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray array = new JSONArray(response);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject c = array.getJSONObject(i);
                        Common.infusionUsername = c.getString("name");
                        Common.infusionUserEmail = c.getString("Email");
                        Common.infusionUserPhone = c.getString("phone");
                        edt_name.setText(Common.infusionUsername);
                        edt_phone.setText(Common.infusionUserPhone);

                        InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        in.hideSoftInputFromWindow(edt_phone.getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
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
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("contactid", sid);
                return params;
            }
        };
        MySingleton.getmInstance(this).addToRequestque(sr);
    }

    private void getSessionName() {
        sessionlist.clear();
        sessionrequest = new StringRequest(Request.Method.POST, Common.sessionUrl, new Response.Listener<String>() {
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
                params.put("propertyname", Common.tagno);
                return params;
            }
        };
        MySingleton.getmInstance(this).addToRequestque(sessionrequest);
    }

    private void uploadFile() {

        StringRequest sr = new StringRequest(Request.Method.POST, Common.user_details_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray arr = new JSONArray(response);
                    JSONObject c = arr.getJSONObject(0);
                    String message = c.getString("message");
                    progressDialog.dismiss();
                    Toast.makeText(UserDetails.this, message, Toast.LENGTH_SHORT).show();

                    edt_id.setText("");
                    edt_name.setText("");
                    edt_phone.setText("");
                    if (message.equals("User Already Exists")) {
                        Log.d(TAG, "onResponse: " + "User already Exists");
                    } else {
//                        insertData();
                        getData();
                        SingleData();

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            //123564724
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("uqniceno", sid);
                params.put("name", sname);
                params.put("phone", sphone);
                params.put("dt", Common.timeStamp);
                params.put("tm", Common.eventTimes);
                params.put("coachname", Common.allocationname);
                params.put("propertyname", Common.tagno);
                params.put("allocation", String.valueOf(Common.sessionValue));
                return params;
            }
        };
        MySingleton.getmInstance(this).addToRequestque(sr);
    }

    class DB_Conn extends AsyncTask<String, Void, String> {
        @Override
        public String doInBackground(String... arg)//compulsory to implement
        {
            String s = "";
            try {
                //Sets up the java client, including the api url
                XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
                config.setServerURL(new URL("https://ad129.infusionsoft.com:443/api/xmlrpc"));
                client = new XmlRpcClient();
                client.setConfig(config);

                //The secure encryption key
                key = "e76b428d25a0c1c740b868a66323a2cc";

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return s;
        }


        @Override
        public void onProgressUpdate(Void... arg0) {
        }

        @Override
        public void onPostExecute(String result) {

        }

        @Override
        public void onPreExecute() {
            super.onPreExecute();
        }
    }

    private void insertData() {
        progressDialog.setTitle("Scann No");
        progressDialog.setMessage("Please wait a moment");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);

                    //List All Contacts in Group
                    fields = new ArrayList(); //What fields we will be selecting
                    fields.add("Id");

                    parameters3 = new ArrayList();
                    parameters3.add(key); //Secure key
                    parameters3.add("Contact");  //What table we are looking in
                    parameters3.add(1000); //How many records to return
                    parameters3.add(0); //Which page of results to display
                    parameters3.add("Groups"); //The field we are querying on
                    parameters3.add(sid); //THe data to query on
                    parameters3.add(fields); //what fields to select on return

                    addContact();
                    //Make call - the result is an array of structs
                    contacts = (Object[]) client.execute("DataService.findByField", parameters3);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void addContact() {
        try {
            /*************************************************
             *                                               *
             ADD CONTACT TO DATABASE
             *                                               *
             *************************************************/

            parameters = new ArrayList();
            contactData = new HashMap();
            contactData.put("Id", sid);


            parameters.add(key); //The secure key
            parameters.add("Contact"); //The table we will be adding to
            parameters.add(contactData); //The data to be added


            /*************************************************
             *                                               *
             ADD CONTACT TO GROUP
             *                                               *
             *************************************************/
            parameters2 = new ArrayList();
            parameters2.add(key); //Secure key
            parameters2.add(sid); //Id of the contact we just added
            parameters2.add(Common.tagno); //Id of the group we want to add to

            success = (Boolean) client.execute("ContactService.addToGroup", parameters2);
            success = true;

            try {
                if (success) {
                    progressDialog.dismiss();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(UserDetails.this, "Data  save", Toast.LENGTH_LONG).show();
                        }
                    });
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.dismiss();
                            Toast.makeText(UserDetails.this, "Data not save", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getData() {
        no0fpeoplelist.clear();
        StringRequest sr = new StringRequest(Request.Method.POST, Common.singleCoachDataUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray array = new JSONArray(response);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject c = array.getJSONObject(i);

                        String name = c.getString("name");
                        no0fpeoplelist.add(name);

                        int total = no0fpeoplelist.size();
                        totalNo.setText("Total No: " + String.valueOf(total));

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
                param.put("propertyname", Common.tagno);
                return param;
            }
        };
        MySingleton.getmInstance(this).addToRequestque(sr);
    }

    private void SingleData() {

        record.clear();
        StringRequest sr = new StringRequest(Request.Method.POST, Common.singleCoachDataUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray array = new JSONArray(response);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject c = array.getJSONObject(i);
                        String uqniceno = c.getString("uqniceno");
                        String name = c.getString("name");
                        Common.sessionValue = Integer.parseInt(c.getString("allocation"));
                        SignleCoachDataModel signleCoachDataModel = new SignleCoachDataModel(name, uqniceno);
                        record.add(signleCoachDataModel);
                    }
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                try {
                    JSONArray array = new JSONArray(response);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject c = array.getJSONObject(i);
                        String message = c.getString("message");
                        Common.sessionValue = Integer.parseInt(c.getString("allocation"));
                        Toast.makeText(UserDetails.this, message, Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "sessionValue " + Common.sessionValue);
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
                param.put("propertyname", Common.tagno);
                return param;
            }
        };
        MySingleton.getmInstance(this).addToRequestque(sr);
    }

    public void getAllocationNum() {
        StringRequest sr = new StringRequest(Request.Method.POST, Common.allocationNum, new Response.Listener<String>() {
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
                param.put("propertyname", Common.tagno);
                return param;
            }
        };
        MySingleton.getmInstance(this).addToRequestque(sr);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
