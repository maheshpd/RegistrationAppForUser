package com.arfeenkhan.registerationappforUser.utils;

import java.util.ArrayList;

public class Common {
    public static String timeStamp;
    public static String tagno = "";
    public static String sdate = "";
    public static String time = "";
    public static String sessionname = "";

    public static String edtseeesionname = "";
    public static String editsessionid = "";


    public static int longdata;
    public static String ScanNo = null;
    public static ArrayList<Integer> list;

    public static String Place = "";

    public static String isInflusion = "true";
    public static String isGodaddy = "true";

//    public static ArrayList<SessionNameModel> sessionlist = new ArrayList<>();

    public static int sessionValue = 0;
    public static String sessionValues ="";
    public static String coachName = "";
    public static String eventTimes = "";

    public static String userid = "";
    public static int total;
    public static String infusionUsername = "";
    public static String infusionUserEmail = "";
    public static String infusionUserPhone = "";
    public static String allocationname="";
    public static String place="";
    public static String name="";
    public static String ctf="";
    public static String tf="false";

    //URLs
    public static String getCity_url= "http://167.71.229.74/barcodescanner/selectcity.php";
    public static String gettag_url= "http://167.71.229.74/barcodescanner/tagdata.php";
    public static String singleCoachDataUrl= "http://167.71.229.74/barcodescanner/singleuserdata.php";

    //This is the main usl where user data will go into godaddy database
    //Please check this
    public static String user_details_url= "http://167.71.229.74/api/index.php/Welcome/bulkdata";
    //////////////////////////



    public static String sessionUrl= "http://167.71.229.74/barcodescanner/getSessionName.php";


    //get data from infusion
    public static String getdatafromInfusionUrl= "http://167.71.229.74/barcodescanner/getcontact.php";

    //insert tagno in infusion
    public static String allocationNum= "http://167.71.229.74/barcodescanner/getallocation.php";


    public static String newRegisterUrl= "http://167.71.229.74/barcodescanner/getregister.php";

}
