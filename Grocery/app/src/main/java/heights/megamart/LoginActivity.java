package heights.megamart;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import Config.BaseURL;
import util.ConnectivityReceiver;
import util.Session_management;

public class LoginActivity extends AppCompatActivity {

   private static String TAG = LoginActivity.class.getSimpleName();

    private Button btn_continue, btn_register;
    private EditText et_login_pass, et_login_email;
    private TextView btn_forgot;
   // private TextView tv_password, tv_email,btn_forgot;
  //  String URL="http://infocentroid.us/grocery_mlm/Api/login";
    String Email,Password;
    ProgressBar progressBar;
    String sever_url;
    String user_id="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        et_login_email=(EditText)findViewById(R.id.et_login_email);
        et_login_pass=(EditText)findViewById(R.id.et_login_pass);
        btn_continue=(Button)findViewById(R.id.btnContinue);
        btn_register=(Button)findViewById(R.id.btnRegister);
        btn_forgot = (TextView) findViewById(R.id.btnForgot);


        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Email = et_login_email.getText().toString().trim();
                Password = et_login_pass.getText().toString().trim();
                attemptLogin();
                new GetLoginAsynctask().execute();
            }
        });

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });


        btn_forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent startRegister = new Intent(LoginActivity.this, ForgotActivity.class);
                startActivity(startRegister);
            }
        });


        // remove title
       /* requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);*/

      /*  et_login_pass = (EditText) findViewById(R.id.et_login_pass);
        et_login_email = (EditText) findViewById(R.id.et_login_email);
      //  tv_password = (TextView) findViewById(R.id.tv_login_password);
     //   tv_email = (TextView) findViewById(R.id.tv_login_email);
        btn_continue = (Button) findViewById(R.id.btnContinue);
        btn_register = (Button) findViewById(R.id.btnRegister);
     //   btn_forgot = (TextView) findViewById(R.id.btnForgot);

        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Email = et_login_email.getText().toString();
                Password = et_login_pass.getText().toString();

               // attemptLogin();

                new GetLoginAsynctask().execute();
            }
        });

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
              startActivity(intent);
            }
        });*/

       /* btn_forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent startRegister = new Intent(LoginActivity.this, ForgotActivity.class);
                startActivity(startRegister);
            }
        });
*/
    }

    //-----------------------------------------------------------------------------




    class GetLoginAsynctask extends AsyncTask<String, String, String> {
        String output = "";
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(LoginActivity.this);
            dialog.setMessage("Processing");
            dialog.setCancelable(true);
            dialog.show();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            sever_url=BaseURL.LOGIN_FORM + Email + "&password=" + Password;
            Log.e("sever_url>>>>>>>>>", sever_url);
            output = HttpHandler.makeServiceCall(sever_url);
            Log.e("getcomment_url", output);
            System.out.println("getcomment_url" + output);
            return output;
        }

        @Override
        protected void onPostExecute(String output) {
            if (output == null) {
                dialog.dismiss();
            } else {
                try {
                    JSONObject json = new JSONObject(output);
                    String responce=json.getString("responce");
                    JSONObject data=json.getJSONObject("data");
                     user_id = data.getString("user_id");
                    String user_fullname = data.getString("user_fullname");
                    String user_email = data.getString("user_email");
                    String user_phone = data.getString("user_phone");
                    String reg_date = data.getString("reg_date");
                    String sponsor_id = data.getString("sponsor_id");
                    String reg_type = data.getString("reg_type");
                    String kyc = data.getString("kyc");
                    String ifsc = data.getString("ifsc");
                    String bank_acc_no = data.getString("bank_acc_no");
                    String acc_holder_name=data.getString("acc_holder_name");
                    String user_image = data.getString("user_image");

                    Log.e("user_email",user_email);
                    //Log.e("password",pwd);
                    Log.e("json>>>>>>>>>",json.toString());


                    if (responce.equalsIgnoreCase("1")) {
                        dialog.dismiss();
                        Toast.makeText(getApplicationContext(),"Login Successfully",Toast.LENGTH_LONG).show();

                       // AppPreference.setUserId(LoginActivity.this,user_id);

                        Session_management sessionManagement = new Session_management(LoginActivity.this);
                      //  sessionManagement.createLoginSession(user_id,user_email,user_fullname,user_phone,user_image,reg_date,sponsor_id,ifsc,bank_acc_no,kyc,"","","",Password);

                        sessionManagement.createLoginSession(user_id,user_email,user_fullname,user_phone,user_image,reg_date,sponsor_id,ifsc,bank_acc_no,kyc,"","","",Password,reg_type,acc_holder_name);


                        Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                        startActivity(intent);
                    }

                    else
                    {
                        Toast.makeText(LoginActivity.this, "Unsuccessful Login",Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    dialog.dismiss();
                }
                super.onPostExecute(output);
            }
        }


    }


    //====================================================================================

    private void attemptLogin() {

        et_login_email.setText(getResources().getString(R.string.tv_login_email));
        et_login_pass.setText(getResources().getString(R.string.tv_login_password));

        et_login_pass.setTextColor(getResources().getColor(R.color.dark_gray));
        et_login_email.setTextColor(getResources().getColor(R.color.dark_gray));

       // String getpassword = et_login_pass.getText().toString();
       // String getemail = et_login_pass.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(Password)) {
            et_login_pass.setTextColor(getResources().getColor(R.color.colorPrimary));
            focusView = et_login_pass;
            cancel = true;
        } else if (!isPasswordValid(Password)) {
            et_login_pass.setText(getResources().getString(R.string.password_too_short));
            et_login_pass.setTextColor(getResources().getColor(R.color.colorPrimary));
            focusView = et_login_pass;
            cancel = true;
        }

        if (TextUtils.isEmpty(Email)) {
            et_login_email.setTextColor(getResources().getColor(R.color.colorPrimary));
            focusView = et_login_email;
            cancel = true;
        } else if (!isEmailValid(Email)) {
            et_login_email.setText(getResources().getString(R.string.invalide_email_address));
            et_login_email.setTextColor(getResources().getColor(R.color.colorPrimary));
            focusView = et_login_email;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            if (focusView != null)
                focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.

            if (ConnectivityReceiver.isConnected()) {

              //  makeLoginRequest(getemail, getpassword);
            }
        }

    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }






    //*******************************************************************************

    /**
     * Method to make json object request where json response starts wtih
     */
 /*   private void makeLoginRequest(String email, final String password) {

        // Tag used to cancel the request
        String tag_json_obj = "json_login_req";

        Map<String, String> params = new HashMap<String, String>();
        params.put("user_email", email);
        params.put("password", password);

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                BaseURL.LOGIN_URL, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                try {
                    Boolean status = response.getBoolean("responce");
                    if (status) {

                        JSONObject obj = response.getJSONObject("data");
                        String user_id = obj.getString("user_id");
                        String user_fullname = obj.getString("user_fullname");
                        String user_email = obj.getString("user_email");
                        String user_phone = obj.getString("user_phone");
                        String user_image = obj.getString("user_image");

                        Session_management sessionManagement = new Session_management(LoginActivity.this);
                        sessionManagement.createLoginSession(user_id,user_email,user_fullname,user_phone,user_image,"","","","",password);

                        Intent i = new Intent(LoginActivity.this,MainActivity.class);
                        startActivity(i);
                        finish();

                    } else {
                        String error = response.getString("error");
                        Toast.makeText(LoginActivity.this, "" + error, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Toast.makeText(LoginActivity.this, getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }*/

    //******************************************************************************

}
