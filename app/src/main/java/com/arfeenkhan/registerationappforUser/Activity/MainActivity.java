package com.arfeenkhan.registerationappforUser.Activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.AsyncTask;
import android.os.Build;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.arfeenkhan.registerationappforUser.Model.SignleCoachDataModel;
import com.arfeenkhan.registerationappforUser.R;
import com.arfeenkhan.registerationappforUser.Utils.Common;
import com.google.zxing.Result;

import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

import static android.Manifest.permission.CAMERA;
import static com.arfeenkhan.registerationappforUser.Activity.UserDetails.record;
import static com.arfeenkhan.registerationappforUser.Activity.UserDetails.singleCoachDataUrl;
import static com.arfeenkhan.registerationappforUser.Activity.UserDetails.user_details_url;

public class MainActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    public String key;
    public XmlRpcClient client;
    public Object[] contacts;
    public static List parameters, parameters2, parameters3, fields;
    public static Map contactData, contact;
    boolean success = false;
    boolean flag = false;
    public static Integer contactId;
    Object cons;
    private static final String TAG = "MainActivity";
    private static final int REQUEST_CAMERA = 1;
    private ZXingScannerView scannerView;
    private static int camId = Camera.CameraInfo.CAMERA_FACING_BACK;

    //    private JsonArrayRequest data_request;
    private StringRequest data_request;
    String data_url = "http://magicconversion.com/barcodescanner/tagdata.php";
    String insert_data_into_bluehost = "http://magicconversion.com/barcodescanner/barcodeinsert.php";
    String getdatafromInfusionUrl = "http://magicconversion.com/barcodescanner/getcontact.php";
    String sessionUrl = "http://magicconversion.com/barcodescanner/getSessionName.php";
    //current Time
    String timeStamp;

    ProgressDialog progressDialog;
    StringRequest sessionrequest;
    //Settings
    public static final String INFUSION = "infusion";
    public static final String GODADDY = "godaddy";

    //SharedPreferences start here
//    public static final String MY_PREFS_NAME = "MySessionValue";
//    SharedPreferences.Editor editor;

    AlertDialog.Builder alert;

    ArrayList<String> sessionlist = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSessionName();
        SingleData();
        scannerView = new ZXingScannerView(this);
        setContentView(scannerView);
        progressDialog = new ProgressDialog(this);
        int currentApiVersion = Build.VERSION.SDK_INT;
        if (currentApiVersion >= Build.VERSION_CODES.P) {
            if (checkPermission()) {
            } else {
                requestPermission();
            }
        }

        DB_Conn conn = new DB_Conn();
        conn.execute();
        conn.doInBackground();
    }

    private boolean checkPermission() {
        return (ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA) == PackageManager.PERMISSION_GRANTED);
    }


    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{CAMERA}, REQUEST_CAMERA);
    }

    private void SingleData() {

        record.clear();
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest sr = new StringRequest(Request.Method.POST, singleCoachDataUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray array = new JSONArray(response);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject c = array.getJSONObject(i);
                        String uqniceno = c.getString("uqniceno");
                        String name = c.getString("name");
//                        Common.sessionValue = Integer.parseInt(c.getString("alocationno"));
                        SignleCoachDataModel signleCoachDataModel = new SignleCoachDataModel(name, uqniceno);
                        record.add(signleCoachDataModel);
                    }
                    UserDetails.adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                try {
                    JSONArray array = new JSONArray(response);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject c = array.getJSONObject(i);
                        String message = c.getString("message");
                        Common.sessionValues = c.getString("allocation");
                        Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
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
                param.put("tagno", Common.tagno);
                return param;
            }
        };

        queue.add(sr);
    }


    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CAMERA:
                if (grantResults.length > 0) {

                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (cameraAccepted) {
                        Toast.makeText(getApplicationContext(), "Permission Granted, Now you can access camera", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Permission Denied, You cannot access and camera", Toast.LENGTH_LONG).show();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (shouldShowRequestPermissionRationale(CAMERA)) {
                                showMessageOKCancel("You need to allow access to both the permissions",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                                    requestPermissions(new String[]{CAMERA},
                                                            REQUEST_CAMERA);
                                                }
                                            }
                                        });
                                return;
                            }
                        }
                    }
                }
                break;
        }
    }

    private void showMessageOKCancel(final String message, DialogInterface.OnClickListener okListener) {
        new android.support.v7.app.AlertDialog.Builder(MainActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                    }
                })
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    @Override
    public void handleResult(Result result) {
        final String myResult = result.getText();
        Common.ScanNo = myResult;
        Log.d(TAG, "Result: " + Common.ScanNo);
//        getDataFromInfusion();


        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("Is this Right Scan no? " +Common.ScanNo);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (Common.sessionValue < sessionlist.size()) {
                    Common.allocationname = sessionlist.get(Common.sessionValue);
                    Common.sessionValue++;
                    //Shared Preference for store session value
//                    editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
//                    editor.putInt("sessionValue", Common.sessionValue);
//                    editor.apply();
                    getDataFromInfusion();
                } else {
                    Common.sessionValue = 0;
                    Common.allocationname = sessionlist.get(Common.sessionValue);
                    Common.sessionValue++;
                    //Shared Preference for store session value
//                    editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
//                    editor.putInt("sessionValue", Common.sessionValue);
//                    editor.apply();
                    getDataFromInfusion();
                }
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(MainActivity.this, "Please scan again", Toast.LENGTH_SHORT).show();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
//        finish();
        scannerView.resumeCameraPreview(MainActivity.this);
    }

    //Connect to infusion
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

    @Override
    public void onResume() {
        super.onResume();


        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        Common.isInflusion = String.valueOf(sharedPreferences.getBoolean(INFUSION, true));
        Common.isGodaddy = String.valueOf(sharedPreferences.getBoolean(GODADDY, true));

        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentapiVersion >= android.os.Build.VERSION_CODES.M) {
            if (checkPermission()) {
                if (scannerView == null) {
                    scannerView = new ZXingScannerView(this);
                    setContentView(scannerView);
                }
                scannerView.setResultHandler(this);
                scannerView.startCamera();
            } else {
                requestPermission();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();


        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        Common.isInflusion = String.valueOf(sharedPreferences.getBoolean(INFUSION, true));
        Common.isGodaddy = String.valueOf(sharedPreferences.getBoolean(GODADDY, true));

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        scannerView.stopCamera();
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
                    parameters3.add(Common.tagno); //THe data to query on
                    parameters3.add(fields); //what fields to select on return

                    //Make call - the result is an array of structs
                    contacts = (Object[]) client.execute("DataService.findByField", parameters3);
                    flag = false;
                    if (flag) {

                    } else {
                        addContact();
                    }

                    for (int i = 0; i < contacts.length; i++) {
                        Map contact = (Map) contacts[i];
                        Common.longdata = contacts.length;
                        Common.list.add(Common.longdata);

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
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
//                        Common.allocationname = sessionlist.get(Common.sessionValue).toUpperCase();
//                        allocationName.setText(Common.allocationname);
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

    private void addContact() {

        try {
            /*************************************************
             *                                               *
             ADD CONTACT TO DATABASE
             *                                               *
             *************************************************/

            parameters = new ArrayList();
            contactData = new HashMap();
            contactData.put("Id", Common.ScanNo);

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
            parameters2.add(Common.ScanNo); //Id of the contact we just added
            parameters2.add(Common.tagno); //Id of the group we want to add to

            success = (Boolean) client.execute("ContactService.addToGroup", parameters2);
            success = true;
//            flag = true;

            try {
                if (success) {

                    this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.dismiss();

                            Toast.makeText(getApplicationContext(), "Data insert in infusion", Toast.LENGTH_LONG).show();
//                            getDataFromInfusion();
                        }
                    });
                } else {

                    this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Data not save", Toast.LENGTH_LONG).show();
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

    public void getDataFromInfusion() {
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest sr = new StringRequest(Request.Method.POST, getdatafromInfusionUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray array = new JSONArray(response);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject c = array.getJSONObject(i);
                        Common.infusionUsername = c.getString("name");
                        Common.infusionUserEmail = c.getString("Email");
                        Common.infusionUserPhone = c.getString("phone");

                        insertData();
                        InsertDataInGodaddy();
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
                params.put("tagname", Common.ScanNo);
                return params;
            }
        };
        queue.add(sr);
    }

    private void InsertDataInGodaddy() {
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest sr = new StringRequest(Request.Method.POST, user_details_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray arr = new JSONArray(response);
                    JSONObject c = arr.getJSONObject(0);
                    String message = c.getString("message");
                    progressDialog.dismiss();
                    Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
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
                params.put("uqniceno", Common.ScanNo);
                params.put("name", Common.infusionUsername);
                params.put("phone", Common.infusionUserPhone);
                params.put("dt", Common.timeStamp);
                params.put("tm", Common.eventTimes);
                params.put("coachname", Common.allocationname);
                params.put("tagno", Common.tagno);
                return params;
            }
        };
        queue.add(sr);
    }

}
