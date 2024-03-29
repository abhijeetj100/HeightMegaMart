package util;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.HashMap;

import heights.megamart.LoginActivity;
import heights.megamart.MainActivity;

import static Config.BaseURL.IS_LOGIN;
import static Config.BaseURL.KEY_ACCOUNT;
import static Config.BaseURL.KEY_BankName;
import static Config.BaseURL.KEY_DATE;
import static Config.BaseURL.KEY_EMAIL;
import static Config.BaseURL.KEY_HOUSE;
import static Config.BaseURL.KEY_ID;
import static Config.BaseURL.KEY_IFSC;
import static Config.BaseURL.KEY_IMAGE;
import static Config.BaseURL.KEY_KYC;
import static Config.BaseURL.KEY_MOBILE;
import static Config.BaseURL.KEY_NAME;
import static Config.BaseURL.KEY_PASSWORD;
import static Config.BaseURL.KEY_PINCODE;
import static Config.BaseURL.KEY_REG;
import static Config.BaseURL.KEY_SOCITY_ID;
import static Config.BaseURL.KEY_SOCITY_NAME;
import static Config.BaseURL.KEY_SPONSERID;
import static Config.BaseURL.KEY_TIME;
import static Config.BaseURL.PREFS_NAME;
import static Config.BaseURL.PREFS_NAME2;



public class Session_management {

    SharedPreferences prefs;
    SharedPreferences prefs2;

    SharedPreferences.Editor editor;
    SharedPreferences.Editor editor2;

    Context context;

    int PRIVATE_MODE = 0;

    public Session_management(Context context) {
        this.context = context;
        prefs = context.getSharedPreferences(PREFS_NAME, PRIVATE_MODE);
        editor = prefs.edit();

        prefs2 = context.getSharedPreferences(PREFS_NAME2, PRIVATE_MODE);
        editor2 = prefs2.edit();
    }



    public void createLoginSession(String id, String email, String name
            , String mobile, String image, String date, String sponserId, String ifsc, String account, String kyc, String socity_id,
                                   String socity_name, String house, String password, String regType, String acc_holder_name) {

        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_ID, id);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_NAME, name);
        editor.putString(KEY_MOBILE, mobile);
        editor.putString(KEY_IMAGE, image);
        editor.putString(KEY_DATE, date);
        editor.putString(KEY_SPONSERID,sponserId);
        editor.putString(KEY_KYC,kyc);
        editor.putString(KEY_ACCOUNT,account);
        editor.putString(KEY_IFSC,ifsc);
        editor.putString(KEY_REG,regType);
        // editor.putString(KEY_PINCODE, pincode);
        editor.putString(KEY_SOCITY_ID, socity_id);
        editor.putString(KEY_SOCITY_NAME, socity_name);
        editor.putString(KEY_HOUSE, house);
        editor.putString(KEY_PASSWORD,password);
        editor.putString(KEY_BankName,acc_holder_name);

        editor.commit();
    }




    /*  public void createLoginSession(String id, String email, String name
            , String mobile, String image, String date,String sponserId,String ifsc,String account,String kyc, String socity_id,
                                   String socity_name, String house,String password) {

        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_ID, id);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_NAME, name);
        editor.putString(KEY_MOBILE, mobile);
        editor.putString(KEY_IMAGE, image);
        editor.putString(KEY_DATE, date);
        editor.putString(KEY_SPONSERID,sponserId);
        editor.putString(KEY_KYC,kyc);
        editor.putString(KEY_ACCOUNT,account);
        editor.putString(KEY_IFSC,ifsc);
      //  editor.putString(KEY_REG,regType);
       // editor.putString(KEY_PINCODE, pincode);
        editor.putString(KEY_SOCITY_ID, socity_id);
        editor.putString(KEY_SOCITY_NAME, socity_name);
        editor.putString(KEY_HOUSE, house);
        editor.putString(KEY_PASSWORD,password);

        editor.commit();
    }
*/
    public void checkLogin() {

        if (!this.isLoggedIn()) {
            Intent loginsucces = new Intent(context, LoginActivity.class);
            // Closing all the Activities
            loginsucces.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            loginsucces.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            context.startActivity(loginsucces);
        }
        else {
            Intent loginsucces = new Intent(context, MainActivity.class);
            context.startActivity(loginsucces);

        }
    }

    /**
     * Get stored session data
     */
    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<String, String>();

        user.put(KEY_ID, prefs.getString(KEY_ID, null));
        // user email id
        user.put(KEY_EMAIL, prefs.getString(KEY_EMAIL, null));
        // user name
        user.put(KEY_NAME, prefs.getString(KEY_NAME, null));

        user.put(KEY_MOBILE, prefs.getString(KEY_MOBILE, null));

        user.put(KEY_IMAGE, prefs.getString(KEY_IMAGE, null));

        user.put(KEY_DATE,prefs.getString(KEY_DATE,null));

        user.put(KEY_SPONSERID,prefs.getString(KEY_SPONSERID,null));

        user.put(KEY_KYC,prefs.getString(KEY_KYC,null));

        user.put(KEY_ACCOUNT,prefs.getString(KEY_ACCOUNT,null));

        user.put(KEY_IFSC,prefs.getString(KEY_IFSC,null));

       // user.put(KEY_REG,prefs.getString(KEY_REG,null));

        user.put(KEY_PINCODE, prefs.getString(KEY_PINCODE, null));

        user.put(KEY_SOCITY_ID, prefs.getString(KEY_SOCITY_ID, null));

        user.put(KEY_SOCITY_NAME, prefs.getString(KEY_SOCITY_NAME, null));

        user.put(KEY_HOUSE, prefs.getString(KEY_HOUSE, null));

        user.put(KEY_PASSWORD, prefs.getString(KEY_PASSWORD, null));

        // return user
        return user;
    }

   /* public void updateData(String name, String mobile, String date ,String sponser_id
            , String socity_id, String image,String house) {*/

  /*  public void updateData(String id,String name, String mobile, String date ,String sponser_id) {

        editor.putString(KEY_NAME, name);
        editor.putString(KEY_MOBILE, mobile);
        editor.putString(KEY_DATE, date);
        editor.putString(KEY_SPONSERID, sponser_id);
        editor.putString(KEY_ID,id);
       // editor.putString(KEY_PINCODE, pincode);
      *//*  editor.putString(KEY_SOCITY_ID, socity_id);
        editor.putString(KEY_IMAGE, image);
        editor.putString(KEY_HOUSE, house);*//*

        editor.apply();
    }*/


    public void updateData(String user_id, String getName, String getPhone, String getdate, String getsponser, String reg_type, String getbankname, String getifsc, String getaccount) {

        editor.putString(KEY_NAME, getName);
        editor.putString(KEY_MOBILE, getPhone);
        editor.putString(KEY_DATE, getdate);
        editor.putString(KEY_SPONSERID, getsponser);
        editor.putString(KEY_ID,user_id);
        editor.putString(KEY_REG,reg_type);
        editor.putString(KEY_BankName,getbankname);
        editor.putString(KEY_IFSC,getifsc);
        editor.putString(KEY_ACCOUNT,getaccount);
        // editor.putString(KEY_PINCODE, pincode);
      /*  editor.putString(KEY_SOCITY_ID, socity_id);
        editor.putString(KEY_IMAGE, image);
        editor.putString(KEY_HOUSE, house);*/
        editor.commit();
        editor.apply();
    }


    public String getUserId() {
//        SharedPreferences preferences = context.getSharedPreferences(PREFS_NAME, PRIVATE_MODE);
        return prefs.getString(KEY_ID, "");
    }

    public String getUserType() {
        return prefs.getString(KEY_REG, "");
    }

    public String getName() {
        return prefs.getString(KEY_NAME, "");
    }

    public String getPhone() {
        return prefs.getString(KEY_MOBILE, "");
    }

    public String getEmail() {
        return prefs.getString(KEY_EMAIL, "");
    }

    public String getDate() {
        return prefs.getString(KEY_DATE, "");
    }

    public String getSponser() {
        return prefs.getString(KEY_SPONSERID, "");
    }

    public String getKyc() {
        return prefs.getString(KEY_KYC, "");
    }

    public String getAccount() {
        return prefs.getString(KEY_ACCOUNT, "");
    }

    public String getIfsc() {
        return prefs.getString(KEY_IFSC, "");
    }

    public String getBankname() {
        return prefs.getString(KEY_BankName, "");
    }




    public void updateSocity(String socity_name,String socity_id){
        editor.putString(KEY_SOCITY_NAME, socity_name);
        editor.putString(KEY_SOCITY_ID, socity_id);

        editor.apply();
    }

    public void logoutSession() {
        editor.clear();
        editor.commit();

        cleardatetime();

        Intent logout = new Intent(context, MainActivity.class);
        // Closing all the Activities
        logout.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        logout.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        context.startActivity(logout);
    }

    public void logoutSessionwithchangepassword() {
        editor.clear();
        editor.commit();

        cleardatetime();

        Intent logout = new Intent(context, LoginActivity.class);
        // Closing all the Activities
        logout.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        logout.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        context.startActivity(logout);
    }

    public void creatdatetime(String date,String time){
        editor2.putString(KEY_DATE, date);
        editor2.putString(KEY_TIME, time);

        editor2.commit();
    }

    public void cleardatetime(){
        editor2.clear();
        editor2.commit();
    }

    public HashMap<String, String> getdatetime() {
        HashMap<String, String> user = new HashMap<String, String>();

        user.put(KEY_DATE, prefs2.getString(KEY_DATE, null));
        user.put(KEY_TIME, prefs2.getString(KEY_TIME, null));

        return user;
    }

    // Get Login State
    public boolean isLoggedIn() {
        return prefs.getBoolean(IS_LOGIN, false);
    }

}
