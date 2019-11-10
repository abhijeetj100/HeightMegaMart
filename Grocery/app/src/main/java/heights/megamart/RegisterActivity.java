package heights.megamart;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Locale;

import util.ConnectivityReceiver;
import util.Utilities;

public class RegisterActivity extends AppCompatActivity implements
        AdapterView.OnItemSelectedListener {

    LinearLayout bank_details;
    String[] reg = {"Non-Earning", "Earning"};
    Spinner spin;
    String getphone, getname, getpassword, getemail, getKyc, getAccountNum, getIfsc, getBankName, getSponserId, getPan, getSponserPosition;
    String Selected_product;
    String Sponser_join;
    String reg_type;
    private RadioGroup sponser_join;
    private RadioButton radioSponserButton;
    RadioButton sponser_left, sponser_right;
    String Condition;
    EditText dob;
    String Dob;
    String Id;
    String position;
    private String dateFlage;
    Calendar myCalendar;
    DatePickerDialog.OnDateSetListener date;

    private static String TAG = RegisterActivity.class.getSimpleName();

    private EditText et_phone, et_name, et_password, et_pan, et_email, et_sponser_id, et_kyc, et_bank_name, et_account_no, et_ifsc_code;
    private Button btn_register;
    private TextView tv_phone, tv_name, tv_password, tv_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // remove title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_register);


        spin = (Spinner) findViewById(R.id.spinner);
        spin.setOnItemSelectedListener(this);

        ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, reg);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spin.setAdapter(aa);

        // radioGroup=(RadioGroup)findViewById(R.id.radioGroup);
        bank_details = (LinearLayout) findViewById(R.id.bank_details);
        et_phone = (EditText) findViewById(R.id.et_reg_phone);
        et_name = (EditText) findViewById(R.id.et_reg_name);
        et_password = (EditText) findViewById(R.id.et_reg_password);
        et_email = (EditText) findViewById(R.id.et_reg_email);
        et_pan = (EditText) findViewById(R.id.et_pan);
        et_account_no = (EditText) findViewById(R.id.et_account_no);
        et_bank_name = (EditText) findViewById(R.id.et_bank_name);
        et_ifsc_code = (EditText) findViewById(R.id.et_ifsc_code);
        et_kyc = (EditText) findViewById(R.id.et_ifsc_code);
        et_sponser_id = (EditText) findViewById(R.id.et_sponser_id);
        sponser_join = (RadioGroup) findViewById(R.id.sponser_join);
        sponser_left = (RadioButton) findViewById(R.id.sponser_left);
        sponser_right = (RadioButton) findViewById(R.id.sponser_right);
        dob = (EditText)findViewById(R.id.dob);

        et_ifsc_code.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        et_pan.setFilters(new InputFilter[]{new InputFilter.AllCaps()});


        tv_password = (TextView) findViewById(R.id.tv_reg_password);
        tv_phone = (TextView) findViewById(R.id.tv_reg_phone);
        tv_name = (TextView) findViewById(R.id.tv_reg_name);
        tv_email = (TextView) findViewById(R.id.tv_reg_email);

             picDate();

        sponser_join.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {


                //get RadioButton text
                if(sponser_left.isChecked()) {
                    Condition="member_left";
                    Id = "1";
                    sponser_right.setChecked(false);
                 //   Toast.makeText(RegisterActivity.this,"recommend is Selected" , Toast.LENGTH_LONG).show();
                }
                if(sponser_right.isChecked()) {
                    Condition="member_right";
                    Id = "2";
                    sponser_left.setChecked(false);
                  //  Toast.makeText(RegisterActivity.this,"not recommencd is Selected" , Toast.LENGTH_LONG).show();
                }
            }
        });


        btn_register = (Button) findViewById(R.id.btnRegister);

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptRegister();

                new SendJsonDataToServer().execute();
                //new AwardTask().execute("0", "0", "0");
            }
        });


    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Selected_product = spin.getSelectedItem().toString();

        if (position == 0) {
            bank_details.setVisibility(View.GONE);
            reg_type = "" + 0;
        } else {
            reg_type = "" + 1;
            bank_details.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    private void attemptRegister() {

        tv_phone.setText(getResources().getString(R.string.et_login_phone_hint));
        tv_email.setText(getResources().getString(R.string.tv_login_email));
        tv_name.setText(getResources().getString(R.string.tv_reg_name_hint));
        tv_password.setText(getResources().getString(R.string.tv_login_password));

        tv_name.setTextColor(getResources().getColor(R.color.dark_gray));
        tv_phone.setTextColor(getResources().getColor(R.color.dark_gray));
        tv_password.setTextColor(getResources().getColor(R.color.dark_gray));
        tv_email.setTextColor(getResources().getColor(R.color.dark_gray));

        getphone = et_phone.getText().toString();
        getname = et_name.getText().toString();
        getpassword = et_password.getText().toString();
        getemail = et_email.getText().toString();
        getPan = et_pan.getText().toString();
        getKyc = et_kyc.getText().toString();
        getAccountNum = et_account_no.getText().toString();
        getBankName = et_bank_name.getText().toString();
        getIfsc = et_ifsc_code.getText().toString();
        getSponserId = et_sponser_id.getText().toString();
        Dob = dob.getText().toString();

        // getSponserPosition = radioSponserButton.getText().toString();


        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(getphone)) {
            tv_phone.setTextColor(getResources().getColor(R.color.colorPrimary));
            focusView = et_phone;
            cancel = true;
        } else if (!isPhoneValid(getphone)) {
            tv_phone.setText(getResources().getString(R.string.phone_too_short));
            tv_phone.setTextColor(getResources().getColor(R.color.colorPrimary));
            focusView = et_phone;
            cancel = true;
        }

        if (TextUtils.isEmpty(getname)) {
            tv_name.setTextColor(getResources().getColor(R.color.colorPrimary));
            focusView = et_name;
            cancel = true;
        }

        if (TextUtils.isEmpty(getpassword)) {
            tv_password.setTextColor(getResources().getColor(R.color.colorPrimary));
            focusView = et_password;
            cancel = true;
        } else if (!isPasswordValid(getpassword)) {
            tv_password.setText(getResources().getString(R.string.password_too_short));
            tv_password.setTextColor(getResources().getColor(R.color.colorPrimary));
            focusView = et_password;
            cancel = true;
        }

        if (TextUtils.isEmpty(getemail)) {
            tv_email.setTextColor(getResources().getColor(R.color.colorPrimary));
            focusView = et_email;
            cancel = true;
        } else if (!isEmailValid(getemail)) {
            tv_email.setText(getResources().getString(R.string.invalide_email_address));
            tv_email.setTextColor(getResources().getColor(R.color.colorPrimary));
            focusView = et_email;
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
                // makeRegisterRequest(getname, getphone, getemail, getpassword,getAccountNum,getBankName,getIfsc,getKyc);
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

    private boolean isPhoneValid(String phoneno) {
        //TODO: Replace this with your own logic
        return phoneno.length() > 9;
    }

    /**
     * Method to make json object request where json response starts wtih
     */

    private void picDate() {

        myCalendar = Calendar.getInstance();

        date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dateFlage="1";
                new DatePickerDialog(RegisterActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

    }


    private void updateLabel() {
        if (dateFlage.equalsIgnoreCase("1")) {
            String myFormat = "yyyy-MM-dd"; //In which you need put here
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, new Locale("pt", "BR"));
            dob.setText(sdf.format(myCalendar.getTime()));

        }

    }

    //***********************************************************************


    class SendJsonDataToServer extends AsyncTask<String, String, String> {

        ProgressDialog dialog;

        protected void onPreExecute() {
            dialog = new ProgressDialog(RegisterActivity.this);
            dialog.show();

        }

        protected String doInBackground(String... arg0) {

            String urlParameters = "";
            try {

                //URL url = new URL("http://infocentroid.us/grocery_mlm/Api/signup");

                urlParameters = "member_email=" + URLEncoder.encode(getemail, "UTF-8") +
                        "&member_password=" + URLEncoder.encode(getpassword, "UTF-8") +
                        "&member_mobile=" + URLEncoder.encode(getphone, "UTF-8") +
                        "&member_name=" + URLEncoder.encode(getname, "UTF-8") +
                        "&reg_type=" + URLEncoder.encode(reg_type, "UTF-8") +
                        "&dob=" + URLEncoder.encode(Dob, "UTF-8") +
                        "&member_sponser=" + URLEncoder.encode(getSponserId, "UTF-8") +
                        "&member_bankifsc=" + URLEncoder.encode(getIfsc, "UTF-8") +
                        "&member_bankacno=" + URLEncoder.encode(getAccountNum, "UTF-8") +
                        "&member_bankname=" + URLEncoder.encode(getname, "UTF-8") +
                        "&member_Adhar=" + URLEncoder.encode(getKyc, "UTF-8") +
                        "&member_pan=" + URLEncoder.encode(getPan, "UTF-8") +
                        "&position=" + URLEncoder.encode(Condition, "UTF-8");

                Log.e("urlParameters", urlParameters);
                Log.e("Condition", Condition);
                Log.e("getIfsc",getIfsc);

            } catch (Exception e) {
                return new String("Exception: " + e.getMessage());
            }
            String result = Utilities.postParamsAndfindJSON("http://infocentroid.us/grocery_mlm/Api/reg_sponser", urlParameters);
            //  Toast.makeText(getActivity(),result,Toast.LENGTH_SHORT).show();
            return result;


        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                dialog.dismiss();

                JSONObject jsonObject = null;
                Log.e("SendJsonDataToServer>>>", result.toString());
                try {

                    // JSONObject jsonObject = new JSONObject(result);
                    jsonObject = new JSONObject(result);
                    String response = jsonObject.getString("responce");
                    String message = jsonObject.getString("msg");
                    //String position = jsonObject.getString("");

                    Log.e(">>>>", jsonObject.toString() + " " + response + " " + message);

                    if (response.equalsIgnoreCase("true")) {

                        et_name.setText("");
                        et_phone.setText("");
                        et_email.setText("");
                        et_pan.setText("");
                        et_phone.setText("");
                        et_kyc.setText("");
                        et_bank_name.setText("");
                        et_ifsc_code.setText("");
                        et_account_no.setText("");
                        et_sponser_id.setText("");

                        Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(RegisterActivity.this, "Email Already Exists", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }

        public String getPostDataString(JSONObject params) throws Exception {

            StringBuilder result = new StringBuilder();
            boolean first = true;

            Iterator<String> itr = params.keys();

            while (itr.hasNext()) {

                String key = itr.next();
                Object value = params.get(key);

                if (first)
                    first = false;
                else
                    result.append("&");

                result.append(URLEncoder.encode(key, "UTF-8"));
                result.append("=");
                result.append(URLEncoder.encode(value.toString(), "UTF-8"));

            }
            return result.toString();
        }


    }


    //****************************************************************************


   /* class SendJsonDataToServer extends AsyncTask<String, String, String> {

        ProgressDialog dialog;

        protected void onPreExecute() {
            dialog = new ProgressDialog(RegisterActivity.this);
            dialog.show();

        }

        protected String doInBackground(String... arg0) {

            String urlParameters = "";
            try {

                //URL url = new URL("http://infocentroid.us/grocery_mlm/Api/signup");
                urlParameters = "user_email=" + URLEncoder.encode(getemail, "UTF-8") +
                        "&password=" + URLEncoder.encode(getpassword, "UTF-8") +
                        "&user_mobile=" + URLEncoder.encode(getphone, "UTF-8") +
                        "&user_name=" + URLEncoder.encode(getname, "UTF-8") +
                        "&reg_type=" + URLEncoder.encode(reg_type, "UTF-8") +
                        "&sponser_id=" + URLEncoder.encode(getSponserId, "UTF-8") +
                        "&ifsc=" + URLEncoder.encode(getIfsc, "UTF-8") +
                        "&bank_acc_no=" + URLEncoder.encode(getAccountNum, "UTF-8") +
                        "&acc_holder_name=" + URLEncoder.encode(getname, "UTF-8") +
                        "&kyc=" + URLEncoder.encode(getKyc, "UTF-8");

                Log.e("urlParameters",urlParameters);


//                JSONObject postDataParams = new JSONObject();
//                postDataParams.put("user_name", getname);
//                postDataParams.put("user_mobile", getphone);
//                postDataParams.put("user_email", getemail);
//                postDataParams.put("password", getpassword);
//                postDataParams.put("reg_type", Selected_product);
//                postDataParams.put("kyc", getKyc);
//                postDataParams.put("ifsc", getIfsc);
//                postDataParams.put("bank_acc_no",getAccountNum);
//                postDataParams.put("acc_holder_name",getBankName);
//
//
//                Log.e("postDataParams", postDataParams.toString());
//
//                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//                conn.setReadTimeout(15000 *//* milliseconds*//*);
//                conn.setConnectTimeout(15000  *//*milliseconds*//*);
//                conn.setRequestMethod("POST");
//                conn.setDoInput(true);
//                conn.setDoOutput(true);
//
//                OutputStream os = conn.getOutputStream();
//                BufferedWriter writer = new BufferedWriter(
//                        new OutputStreamWriter(os, "UTF-8"));
//                writer.write(getPostDataString(postDataParams));
//
//                writer.flush();
//                writer.close();
//                os.close();
//
//                int responseCode = conn.getResponseCode();
//
//                if (responseCode == HttpsURLConnection.HTTP_OK) {
//
//                    BufferedReader r = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//                    StringBuilder result = new StringBuilder();
//                    String line;
//                    while ((line = r.readLine()) != null) {
//                        result.append(line);
//                    }
//                    r.close();
//                    return result.toString();
//
//                }  else {
//                    return new String("false : " + responseCode);
//                }



            } catch (Exception e) {
                return new String("Exception: " + e.getMessage());
            }
            String result = Utilities.postParamsAndfindJSON("https://infocentroid.us/grocery_mlm/api/signup", urlParameters);
            //  Toast.makeText(getActivity(),result,Toast.LENGTH_SHORT).show();
            return result;


        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                dialog.dismiss();

                // JSONObject jsonObject = null;
                Log.e("SendJsonDataToServer>>>", result.toString());
                try {


                    // result=getJSONUrl(URL);  //<< get json string from server
                    JSONObject jsonObject = new JSONObject(result);
                    //jsonObject = new JSONObject(result);
                    String response = jsonObject.getString("responce");
                    String message = jsonObject.getString("message");

                    Log.e(">>>>", jsonObject.toString() + " " + response + " " + message);

                    if (response.equalsIgnoreCase("true")) {

                        et_name.setText("");
                        et_phone.setText("");
                        et_email.setText("");
                        et_phone.setText("");
                        et_kyc.setText("");
                        et_bank_name.setText("");
                        et_ifsc_code.setText("");
                        et_account_no.setText("");
                        et_sponser_id.setText("");

                        Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(RegisterActivity.this, "Email Already Exists", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }

        public String getPostDataString(JSONObject params) throws Exception {

            StringBuilder result = new StringBuilder();
            boolean first = true;

            Iterator<String> itr = params.keys();

            while (itr.hasNext()) {

                String key = itr.next();
                Object value = params.get(key);

                if (first)
                    first = false;
                else
                    result.append("&");

                result.append(URLEncoder.encode(key, "UTF-8"));
                result.append("=");
                result.append(URLEncoder.encode(value.toString(), "UTF-8"));

            }
            return result.toString();
        }


    }
*/

    //*************************************************************************


   /* private void makeRegisterRequest( String name, String mobile,
                                     String email, String password, String accountNum, String bankName,String ifsc, String kyc) {

        // Tag used to cancel the request
        String tag_json_obj = "json_register_req";

        Map<String, String> params = new HashMap<String, String>();
        params.put("user_name", name);
        params.put("user_mobile", mobile);
        params.put("user_email", email);
        params.put("password", password);
        params.put("bank_acc_no",accountNum);
        params.put("acc_holder_name",bankName);
        params.put("ifsc",ifsc);
        params.put("kyc",kyc);



        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                BaseURL.REGISTER_URL, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                try {
                    Boolean status = response.getBoolean("responce");
                    if (status) {

                        String msg = response.getString("message");
                        Toast.makeText(RegisterActivity.this, "" + msg, Toast.LENGTH_SHORT).show();

                        Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
                        startActivity(i);
                        finish();

                    } else {
                        String error = response.getString("error");
                        Toast.makeText(RegisterActivity.this, "" + error, Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(RegisterActivity.this, getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }
*/
}
